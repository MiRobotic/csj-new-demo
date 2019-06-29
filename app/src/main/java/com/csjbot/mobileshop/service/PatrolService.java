package com.csjbot.mobileshop.service;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnDetectPersonListener;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnHeadTouchListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.listener.OnWakeupListener;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.feature.content.ContentActivity;
import com.csjbot.mobileshop.feature.entertainment.EntertainmentActivity;
import com.csjbot.mobileshop.feature.media.MediaPublicizeActivity;
import com.csjbot.mobileshop.feature.navigation.NaviActivity;
import com.csjbot.mobileshop.feature.nearbyservice.NearByActivity;
import com.csjbot.mobileshop.feature.nearbyservice.PoiSearchActivity;
import com.csjbot.mobileshop.feature.payment.PaymentGuideActivity;
import com.csjbot.mobileshop.feature.vipcenter.VipCenterActivity;
import com.csjbot.mobileshop.guide_patrol.patrol.core.RobotProxy;
import com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine.INaviCircleState;
import com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine.StateMachineManager;
import com.csjbot.mobileshop.guide_patrol.patrol.event.PatrolEvent;
import com.csjbot.mobileshop.guide_patrol.patrol.event.PatrolPauseEvent;
import com.csjbot.mobileshop.guide_patrol.patrol.event.PatrolActionEvent;
import com.csjbot.mobileshop.guide_patrol.patrol.patrol_activity.PatrolActivity;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.model.tcp.tts.ISpeak;
import com.iflytek.cloud.SpeechError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 巡视服务
 * 唤醒 摸头 人脸检测 触屏
 */

public class PatrolService extends BaseService {

    private long wakeTime = 0;//记录唤醒时间
    private boolean isCheckPersonLoop = false;//是否需要主动人体检测
    private boolean isChating = false;//是否正在聊天
    private boolean isValidateFace = false;//人脸检测是否有效  开始巡视10s内无效
    private boolean isSpeakEnd = false;
    private boolean isPlayVideo = false;//true 正在播放音视频   false 没有播放或者播放完成音视频
    private Runnable currentRunnable;
    private Runnable runnable;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private ISpeak mSpeak;
    private List<String> mActivitys;//巡航过程中，需要30+30s返回待机页的activity

    @Override
    public void onCreate() {
        super.onCreate();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mActivitys = new ArrayList<>();
        initSpeak();
        init();
        mActivitys.add(NaviActivity.class.getSimpleName());//导航页面
        mActivitys.add(PaymentGuideActivity.class.getSimpleName());//查缴指南
        mActivitys.add(VipCenterActivity.class.getSimpleName());//个人中心
        mActivitys.add(MediaPublicizeActivity.class.getSimpleName());//媒体宣传
        mActivitys.add(EntertainmentActivity.class.getSimpleName());//休闲娱乐
        mActivitys.add(PatrolActivity.class.getSimpleName());//大厅巡视
        mActivitys.add(NearByActivity.class.getSimpleName());//周边服务
        mActivitys.add(PoiSearchActivity.class.getSimpleName());//查看百度地图
        mActivitys.add(ContentActivity.class.getSimpleName());//内容页面
        //缺少用电科普 媒体宣传（全屏播放）
    }

    private void initSpeak() {
        mSpeak = ServerFactory.getSpeakInstance();
    }

    public void speak(String text) {
        if (!TextUtils.isEmpty(text)) {
            mSpeak.startSpeaking(text, null);
        }
    }

    protected void speak(String text, OnSpeakListener listener) {
        if (!TextUtils.isEmpty(text)) {
            mSpeak.startSpeaking(text, listener);
        }
    }

    /**
     * 初始化
     */
    private void init() {
        RobotManager.getInstance().addListener(onWakeupListener);
        RobotManager.getInstance().addListener(onDetectPersonListener);
        Robot.getInstance().addHeadTouchListener(onHeadTouchListener);
        RobotManager.getInstance().addListener(onFaceListener);
        setPatrolListener();
    }

    private OnFaceListener onFaceListener = new OnFaceListener() {
        @Override
        public void personInfo(String json) {

        }

        @Override
        public void personNear(boolean person) {
            NaviTaskStatus status = StateMachineManager.getInstance().getCurrentState();
            Log.e("sunxy", "人脸"+person+status+isValidateFace);
            if (person && (status != NaviTaskStatus.AWAIT/* && status != NaviTaskStatus.PAUSE*/) && isValidateFace) {
                pauseAction();
            }
        }

        @Override
        public void personList(String json) {

        }
    };

    private OnHeadTouchListener onHeadTouchListener = () -> {
        Log.e("sunxy", "摸头");
        NaviTaskStatus status = StateMachineManager.getInstance().getCurrentState();
        if (status != NaviTaskStatus.AWAIT/* && status != NaviTaskStatus.PAUSE*/) {
            pauseAction();
        }
    };

    private OnWakeupListener onWakeupListener = angle -> {
        Log.e("sunxy", "唤醒");
        NaviTaskStatus status = StateMachineManager.getInstance().getCurrentState();
        if (status != NaviTaskStatus.AWAIT/* && status != NaviTaskStatus.PAUSE*/) {
            pauseAction();
        }
    };

    private OnDetectPersonListener onDetectPersonListener = new OnDetectPersonListener() {

        @Override
        public void response(int state) {
            NaviTaskStatus status = StateMachineManager.getInstance().getCurrentState();
            Log.e("sunxy", "人体检测"+status+state+isChating+isSpeakEnd+getTopActivity()+isPlayVideo);
            if ((status == NaviTaskStatus.PAUSE/* || status == NaviTaskStatus.AWAIT*/) && state == 0 && !isChating && isSpeakEnd && !isPlayVideo) {//无人
                if (TextUtils.isEmpty(getTopActivity())) {
                    return;
                }
                if (getTopActivity().contains("AllSceneHomeActivity") && StateMachineManager.getInstance().getCurrentState() != NaviTaskStatus.AWAIT) {//主页 30秒检测不到人继续原有的巡逻任务
                    stopCheckPerson();
                    if (!isChating) {
                        continueGuide();
                    } else {
                        isCheckPersonLoop = true;
                        startCheckPerson();
                    }
                    return;
                } else {
                    for (String activity : mActivitys) {
                        if (getTopActivity().contains(activity) && StateMachineManager.getInstance().getCurrentState() != NaviTaskStatus.AWAIT) {
                            stopCheckPerson();
                            speak("请问还有什么可以帮您?", new OnSpeakListener() {
                                @Override
                                public void onSpeakBegin() {

                                }

                                @Override
                                public void onCompleted(SpeechError speechError) {
                                    currentRunnable = () -> {
                                        if (!isChating) {
                                            continueGuide();
                                        } else {
                                            isCheckPersonLoop = true;
                                            startCheckPerson();
                                        }
                                    };
                                    mainHandler.postDelayed(currentRunnable, 30000);
                                }
                            });
                            return;
                        }
                    }
                }
            } else if (status == NaviTaskStatus.PAUSE && state == 1){
                isCheckPersonLoop = true;
                startCheckPerson();
            } else if (status == NaviTaskStatus.AWAIT) {
                stopCheckPerson();
            }
        }
    };

    private void setPatrolListener() {
        RobotProxy.getInstance().setSendStateListener(new INaviCircleState() {
            @Override
            public void robotStart(int curindex) {
                Log.e("surnxy", "开始工作了");
                runnable = ()->{
                    isValidateFace = true;
                    EventBus.getDefault().post(new PatrolActionEvent("1"));
                };
                mainHandler.postDelayed(runnable, 10000);
            }

            @Override
            public void robotArrive(int curindex) {

            }

            @Override
            public void robotBack() {
                EventBus.getDefault().post(new PatrolActionEvent("2"));
            }

            @Override
            public void robotError(String msg) {}

            @Override
            public void robotPause() {}

            @Override
            public void robotContinue() {

            }

            @Override
            public void robotStop() {
                StateMachineManager.getInstance().setInitState(NaviTaskStatus.AWAIT);
            }

            @Override
            public void robotGoWaiting() {
                currentRunnable = ()->{
                    isValidateFace = true;
                };
                mainHandler.postDelayed(currentRunnable, 10000);
            }

            @Override
            public void robotCircusee() {

            }
        });
    }

    private void pauseAction() {
        if (BatteryService.state != BatteryService.NO_CHARGING) {
            return;
        }
        long lastTime = System.currentTimeMillis() - wakeTime;
        wakeTime = System.currentTimeMillis();
        if (lastTime >= 1000) {
            pauseGuide();
            isCheckPersonLoop = true;
//            isValidateFace = false;
            startCheckPerson();
        }
    }

    /**开始人体检测*/
    private void startCheckPerson() {
        if (currentRunnable != null) {
            mainHandler.removeCallbacks(currentRunnable);
        }
        currentRunnable = () -> {
            if (isCheckPersonLoop) {
                isSpeakEnd = true;
                RobotManager.getInstance().robot.reqProxy.getPerson();
                startCheckPerson();
            }
        };
        mainHandler.postDelayed(currentRunnable, 30000);
    }

    /**
     * 停止查询人体检测
     */
    private void stopCheckPerson() {
        isCheckPersonLoop = false;
        isSpeakEnd = false;
        if (currentRunnable != null) {
            mainHandler.removeCallbacks(currentRunnable);
        }
    }

    private void pauseGuide() {
        Log.e("sunxy","暂停");
        RobotProxy.getInstance().pauseTask();
        if (runnable != null) {
            isValidateFace = true;
            mainHandler.removeCallbacks(runnable);
        }
    }

    private void continueGuide() {
        Log.e("sunxy","继续");
        isValidateFace = true;
        stopSpeak();
        RobotProxy.getInstance().continueTask();
        EventBus.getDefault().post(new PatrolActionEvent("1"));
    }

    public void stopSpeak() {
        if (mSpeak.isSpeaking()) {
            mSpeak.stopSpeaking();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void startPatrol(PatrolEvent event) {
        if (event != null) {
            if (event.getAction() == PatrolEvent.PLAYING) {
                isPlayVideo = true;
            } else if (event.getAction() == PatrolEvent.PLAYEND) {
                isPlayVideo = false;
            } else if (event.getAction() == PatrolEvent.START) {
                isCheckPersonLoop = false;
                isValidateFace = false;
                isSpeakEnd = false;
                EventBus.getDefault().post(new PatrolActionEvent("1"));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void pausePatrol(PatrolPauseEvent event) {
        if (event != null && StateMachineManager.getInstance().getCurrentState() != NaviTaskStatus.AWAIT) {
            pauseAction();
        }
    }

    /**
     * 获得栈中最顶层的Activity
     *
     * @return
     */
    private String getTopActivity() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        assert manager != null;
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            return (runningTaskInfos.get(0).topActivity.getShortClassName());
        } else
            return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        Robot.getInstance().removeFaceListener(onFaceListener);
        Robot.getInstance().removeDetectPersonListener(onDetectPersonListener);
        Robot.getInstance().removeWakeupListener(onWakeupListener);
        Robot.getInstance().removeHeadTouchListener(onHeadTouchListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
