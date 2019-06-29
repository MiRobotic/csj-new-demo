package com.csjbot.mobileshop.service;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.CmdConstants;
import com.csjbot.coshandler.global.NTFConstants;
import com.csjbot.coshandler.global.REQConstants;
import com.csjbot.coshandler.listener.OnInitListener;
import com.csjbot.coshandler.listener.OnLinuxRobotTypeListener;
import com.csjbot.coshandler.listener.OnMapListener;
import com.csjbot.coshandler.listener.OnSNListener;
import com.csjbot.coshandler.listener.OnShutdownListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.listener.OnSyncFaceListener;
import com.csjbot.coshandler.listener.OnWakeupListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.coshandler.service.HandlerMsgService;
import com.csjbot.coshandler.tts.GoogleSpechImpl;
import com.csjbot.coshandler.tts.SpeechFactory;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.manager.AdvertisementManager;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.dialog.LoadMapFileDialog;
import com.csjbot.mobileshop.dialog.NewRetailEdittextDialog;
import com.csjbot.mobileshop.dialog.NewRetailOneBtnDialog;
import com.csjbot.mobileshop.feature.content.ContentActivity;
import com.csjbot.mobileshop.feature.entertainment.EntertainmentActivity;
import com.csjbot.mobileshop.feature.media.MediaPublicizeActivity;
import com.csjbot.mobileshop.feature.nearbyservice.NearByActivity;
import com.csjbot.mobileshop.feature.payment.PaymentGuideActivity;
import com.csjbot.mobileshop.feature.product.ProductDetailsActivity;
import com.csjbot.mobileshop.feature.product.ProductListActivity;
import com.csjbot.mobileshop.feature.product.ProductTypeActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.home.AllSceneHomeActivity;
import com.csjbot.mobileshop.jpush.diaptch.CsjPushDispatch;
import com.csjbot.mobileshop.jpush.diaptch.constants.ConstantsId;
import com.csjbot.mobileshop.jpush.manager.CsjJPushManager;
import com.csjbot.mobileshop.model.http.ApiUrl;
import com.csjbot.mobileshop.model.http.apiservice.proxy.SceneProxy;
import com.csjbot.mobileshop.model.tcp.chassis.IChassis;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.model.tcp.proxy.SpeakProxy;
import com.csjbot.mobileshop.model.tcp.tts.ISpeak;
import com.csjbot.mobileshop.network.ShellUtil;
import com.csjbot.mobileshop.util.BlackgagaLogger;
import com.csjbot.mobileshop.util.SharePreferenceTools;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.iflytek.cloud.SpeechError;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * HomeService,主要作用:
 * 负责与底层通信的连接
 * 接受底层返回的数据分发处理
 */
public class HomeService extends BaseService {

    /* 启动Activity标识 */
    public static final int START_ACTIVITY = 10;

    public static final int LINUX_CONNECTED = 0;
    public static final int LINUX_DISCONNECT = 1;


    public static boolean IS_SWITCH_LANGUAGE;

    /* 广播接收者 */
    private HomeBroadcast mHomeBroadcast;

    private volatile boolean speechRecognition = false;

    public static int autoCloseSpeechTime;

    private IChassis mChassis;//机器人肢体控制操作
    private boolean isLoadMapSuccess = false;//是否加载地图成功

    private static class MyHandler extends Handler {
        WeakReference<HomeService> myService;

        MyHandler(HomeService service) {
            myService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case START_ACTIVITY:/* 启动指定的Activity */
                    myService.get().startActivity(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }

    }

    /* Handle消息处理 */
    private Handler mHandle = new MyHandler(this);

    /**
     * 根据className启动对应的activity
     *
     * @param className
     */
    private void startActivity(String className) {
        BlackgagaLogger.error("chenqi 是获取到了什么className" + className);
        Class<?> c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
        }
        if (c != null) {
            startActivity(new Intent(this, c).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public HomeService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CsjlogProxy.getInstance().debug("class:HomeService method:onCreate");
        /* 初始化机器人管理类 */
        initRobot();
        /* 注册广播接收器 */
        regBroadcast();
        //打开远程更新
//        new UpdateApkManagerUtil(getApplicationContext(), false).checkUpdateInfo();

        mChassis = ServerFactory.getChassisInstance();

        /**
         * 注册场景监听回调
         */
        CsjPushDispatch.getInstance().addSceneEvent((id, sid, data) -> {
            CsjlogProxy.getInstance().info("SceneEvent:id" + id);
            if (id.equals(ConstantsId.Scene.SCENE_CHANGE)) {
                CsjlogProxy.getInstance().info("SceneEvent:SCENE_CHANGE");
                SceneProxy.newInstance().getScene(new SceneProxy.SceneListener() {
                    @Override
                    public void onSuccess() {
                        ServerFactory.getSpeakInstance().startSpeaking("正在切换场景!机器人应用即将重启！", new OnSpeakListener() {
                            @Override
                            public void onSpeakBegin() {

                            }

                            @Override
                            public void onCompleted(SpeechError speechError) {
                            }
                        });

                        mHandle.postDelayed(() -> ShellUtil.execCmd("am force-stop com.csjbot.mobileshop\n  " +
                                        "am start -n com.csjbot.mobileshop/com.csjbot.mobileshop.SplashActivity",
                                true, false), 5000);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        });

    }

    /**
     * 当选择语言的时候，会调用 onStartCommand
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CsjlogProxy.getInstance().debug("class:HomeService method:onStartCommand");
        if (IS_SWITCH_LANGUAGE) {
            initSpeech();
            switchLanguage();
            IS_SWITCH_LANGUAGE = false;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // 如果是中文，则选择TTS的 口音
    private void switchLanguage() {
        // 切换语音识别语言
        if (!Constants.isI18N()) {
            CsjlogProxy.getInstance().info("切换到中文语言识别环境");
            String accent = SharedPreUtil.getString(SharedKey.CHINESE_LANGUAGE_TYPE, SharedKey.CHINESE_LANGUAGE_TYPE);
            if (TextUtils.isEmpty(accent)) {
                ServerFactory.getSpeechInstance().setLanguage(CmdConstants.LanguageType.ZH_CN);
            } else {
                ServerFactory.getSpeechInstance().setLanguageAndAccent(CmdConstants.LanguageType.ZH_CN, accent);
            }
        }
    }

    /**
     * 初始化语音合成功能
     */
    private void initSpeech() {
        String currentSpeech;
        /**
         * 如果是 i18n
         *      就是用google tts
         * 否则
         *      如果 是英文，
         *          则用 catherine 的声音
         *      否则
         *          如果是小雪，则用 nana  的声音
         *          其他 的用 用户设置的tts
         */
        if (Constants.isI18N()) {
            SpeakProxy.getInstance().initSpeak(this, SpeechFactory.SpeechType.GOOGLE);

            String language_key = CsjLanguage.getISOLanguage() + GoogleSpechImpl.TTS_LANGUAGE_NAME;
            String country_key = CsjLanguage.getISOLanguage() + GoogleSpechImpl.TTS_COUNTRY_NAME;
            String speaker_key = CsjLanguage.getISOLanguage() + GoogleSpechImpl.TTS_VOICE_NAME;
            String language = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, language_key, CsjLanguage.getISOLanguage());
            String country = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, country_key, "");
            String speaker = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, speaker_key, "");


            CsjlogProxy.getInstance().info("Text2Speech language_key:" + language_key);
            CsjlogProxy.getInstance().info("Text2Speech country_key:" + country_key);
            CsjlogProxy.getInstance().info("Text2Speech speaker_key:" + speaker_key);
            CsjlogProxy.getInstance().info("Text2Speech language:" + language);
            CsjlogProxy.getInstance().info("Text2Speech country:" + country);
            CsjlogProxy.getInstance().info("Text2Speech speaker:" + speaker);

            Locale lang;

            // 如果语言是空，就设置成 en_US
            if (TextUtils.isEmpty(language)) {
                lang = new Locale(CsjLanguage.getISOLanguage(), "");
            } else {
                lang = new Locale(language, country);
            }
            mHandle.postDelayed(() -> {
                SpeakProxy.getInstance().setLanguage(lang);
                SpeakProxy.getInstance().setSpeakerName(speaker);
            }, 3000);

            currentSpeech = "google ";
        } else {
            SpeakProxy.getInstance().initSpeak(this, SpeechFactory.SpeechType.IFLY);
            currentSpeech = "IflyTec";

            if (CsjLanguage.isEnglish()) {
                SpeakProxy.getInstance().setSpeakerName("catherine");
                currentSpeech = "IflyTec catherine";

            } else {
                String storedSpeaker = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, SharedKey.SPEAKER_KEY, SharedKey.DEFAULT_SPEAKER);
                SpeakProxy.getInstance().setSpeakerName(storedSpeaker);
                currentSpeech = "IflyTec " + storedSpeaker;

                if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_SNOW)) {
                    SpeakProxy.getInstance().setSpeakerName("nannan");
                    currentSpeech = "IflyTec nannan";
                }
            }

        }

        CsjlogProxy.getInstance().error("Text2Speech currentSpeech is " + currentSpeech);
//        switch (CsjLanguage.CURRENT_LANGUAGE) {
//            case CsjLanguage.CHINESE: {
//                SpeakProxy.getInstance().initSpeak(this, SpeechFactory.SpeechType.IFLY);
//                if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_SNOW)) {
//                    SpeakProxy.getInstance().setSpeakerName("nannan");
//                }
//
//                String storedSpeaker = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, SharedKey.SPEAKER_KEY, SharedKey.DEFAULT_SPEAKER);
//                SpeakProxy.getInstance().setSpeakerName(storedSpeaker);
//            }
//            break;
//
//            case CsjLanguage.JAPANESE:
//                if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_SNOW)) {
//                    SpeakProxy.getInstance().initSpeak(this, SpeechFactory.SpeechType.JAPAN_SNOW);
//                    break;
//                }
//            case CsjLanguage.ENGELISH_US:
//            case CsjLanguage.ENGELISH_UK:
//            case CsjLanguage.ENGELISH_AUSTRALIA:
//            case CsjLanguage.ENGELISH_INDIA:
//            case CsjLanguage.KOREAN:
//            default: {
//                SpeakProxy.getInstance().initSpeak(this, SpeechFactory.SpeechType.GOOGLE);
//
//                String language_key = CsjLanguage.getISOLanguage() + GoogleSpechImpl.TTS_LANGUAGE_NAME;
//                String country_key = CsjLanguage.getISOLanguage() + GoogleSpechImpl.TTS_COUNTRY_NAME;
//                String speaker_key = CsjLanguage.getISOLanguage() + GoogleSpechImpl.TTS_VOICE_NAME;
//                String language = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, language_key, CsjLanguage.getISOLanguage());
//                String country = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, country_key, "");
//                String speaker = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, speaker_key, "");
//
//
//                CsjlogProxy.getInstance().info("language:" + language);
//                CsjlogProxy.getInstance().info("country:" + country);
//                CsjlogProxy.getInstance().info("speaker:" + speaker);
//
//                Locale lang;
//
//                // 如果语言是空，就设置成 en_US
//                if (TextUtils.isEmpty(language)) {
//                    lang = new Locale(CsjLanguage.getISOLanguage(), "");
//                } else {
//                    lang = new Locale(language, country);
//                }
//                mHandle.postDelayed(() -> {
//                    SpeakProxy.getInstance().setLanguage(lang);
//                    SpeakProxy.getInstance().setSpeakerName(speaker);
//                }, 1000);
//            }
//            break;
//        }

        float ttsVolume = SharedPreUtil.getFloat(SharedKey.TTS_VOLUME, SharedKey.TTS_VOLUME, 1.0f);
        CsjlogProxy.getInstance().info("ttsVolume:" + ttsVolume);
        new Handler().postDelayed(() -> SpeakProxy.getInstance().setVolume(ttsVolume), 2000);
    }

    /**
     * 初始化机器人功能
     */
    private void initRobot() {

        autoCloseSpeechTime = SharedPreUtil.getInt(SharedKey.AUTO_SPEECH_RECOGNITION_CLOSE_TIME, SharedKey.AUTO_SPEECH_RECOGNITION_CLOSE_TIME, 30);

        initSpeech();

        /* 获取机器人管理类的实例 */
        RobotManager robotManager = RobotManager.getInstance();
        /* 初始化监听 */
        robotManager.addListener(new OnInitListener() {
            @Override
            public void success() {
                robotManager.robot.reqProxy.getSN();
                mHandle.post(() -> {
                    sendBroadcast(LINUX_CONNECTED);
                });
                CsjlogProxy.getInstance().debug("class:HomeService message:linux success");
                /* 唤醒机器人 */
//                ServerFactory.getSpeechInstance().openMicro();

                if (!Constants.isI18N()) {
                    switchLanguage();
                }

//                ServerFactory.getConfigInstance().setMicroVolume(5);

                if (!Constants.isI18N()) {
                    // 开启语音多次识别
                    if (!Constants.Scene.CurrentScene.equals(Constants.Scene.JiuDianScene)) {
                        ServerFactory.getSpeechInstance().startIsr();
                    }
                } else {
                    ServerFactory.getSpeechInstance().stopIsr();
                    HandlerMsgService.setMsgInterceptor(NTFConstants.SPEECH_ISR_ONLY_RESULT_NTF,
                            NTFConstants.SPEECH_ISR_LAST_RESULT_NTF);
                }


                ServerFactory.getRobotState().getRobotHWVersion();
                ServerFactory.getRobotState().getLinuxRobotType();

                mHandle.postDelayed(() -> RobotManager.getInstance().robot.reqProxy.setExpression(REQConstants.Expression.SMILE, REQConstants.Expression.YES, REQConstants.Expression.NO), 10000);

                robotManager.setListener(state -> {
                    boolean boo = SharedPreUtil.getBoolean(SharedKey.AUTO_SPEECH_RECOGNITION_SWITCH, SharedKey.AUTO_SPEECH_RECOGNITION_SWITCH, true);
                    CsjlogProxy.getInstance().info("boo:" + boo);
                    if (boo) {
                        CsjlogProxy.getInstance().info("OnDetectPersonListener 检测是否有人出现:" + state);
                        if (state == 0) {
                            CsjlogProxy.getInstance().info("OnDetectPersonListener 发送延时关闭语音识别任务,延时:" + autoCloseSpeechTime + "秒");
                            mHandle.postDelayed(runnableCloseSpeechRecognition, 1000 * autoCloseSpeechTime);
                        } else if (state == 1) {
                            if (speechRecognition) {
                                CsjlogProxy.getInstance().info("OnDetectPersonListener 发送取消延时关闭语音识别任务");
                                mHandle.removeCallbacks(runnableCloseSpeechRecognition);
                            } else {
                                CsjlogProxy.getInstance().info("OnDetectPersonListener openSpeechRecognition runnableCloseSpeechRecognition ");
                                mHandle.postDelayed(runnableCloseSpeechRecognition, 1000 * 30);
                                openSpeechRecognition();
                            }
                        }
                    }
                });

                // 1、取得本地时间：
                Calendar cal = Calendar.getInstance();
                // 2、取得时间偏移量：
                int timeOffSet = cal.get(Calendar.ZONE_OFFSET);
                ServerFactory.getRobotState().setTimezone(String.valueOf(timeOffSet), CsjLanguage.getLanguageStrForTimeZone());
                CsjJPushManager.getInstance().sendFailedReqAgain();
            }

            @Override
            public void faild() {
            }

            @Override
            public void timeout() {
            }

            @Override
            public void disconnect() {
                CsjlogProxy.getInstance().debug("class:HomeService message:linux disconnect");
            }
        });

        /**
         * 唤醒通知
         */
        robotManager.addListener((OnWakeupListener) (angle) -> {
            if (Constants.sIsIntoOtherApp) {
                return;
            }
            AdvertisementManager.getInstance().stop();
            sendWakeupBroadcast();
            CsjlogProxy.getInstance().debug("class:HomeService message:wakeup");
            String topActStr = getTopActivity();
            if (topActStr.contains(AllSceneHomeActivity.class.getSimpleName())
                    || topActStr.contains(ContentActivity.class.getSimpleName())
                    || topActStr.contains(PaymentGuideActivity.class.getSimpleName())
                    || topActStr.contains(MediaPublicizeActivity.class.getSimpleName())
                    || topActStr.contains(EntertainmentActivity.class.getSimpleName())
                    || topActStr.contains(NearByActivity.class.getSimpleName())
                    || topActStr.contains(ProductTypeActivity.class.getSimpleName())
                    || topActStr.contains(ProductListActivity.class.getSimpleName())
                    || topActStr.contains(ProductDetailsActivity.class.getSimpleName())
                    ) {
                if (angle > 0 && angle < 360) {
                    if (angle <= 180) {
                        CsjlogProxy.getInstance().debug("向左转:+" + angle);
                        if (BatteryService.state == BatteryService.NO_CHARGING) {
                            RobotManager.getInstance().robot.reqProxy.moveAngle(angle);
                        }
                    } else {
                        CsjlogProxy.getInstance().debug("向右转:-" + (360 - angle));
                        if (BatteryService.state == BatteryService.NO_CHARGING) {
                            RobotManager.getInstance().robot.reqProxy.moveAngle(-(360 - angle));
                        }
                    }
                }
                boolean wakeupStop = SharedPreUtil.getBoolean(SharedKey.WAKEUP_STOP, SharedKey.WAKEUP_STOP, false);
                if (!wakeupStop) {
                    return;
                }
                ISpeak speak = ServerFactory.getSpeakInstance();
                if (speak.isSpeaking()) {
                    speak.stopSpeaking();
                }
                speak.startSpeaking(getString(R.string.hello) + getString(R.string.may_i_help_you), null);
            }

        });

//        robotManager.addListener((OnHeadTouchListener) () -> {
//            ServerFactory.getSpeakInstance()
//                    .startSpeaking(getString(R.string.do_not_touch_head), null);
//        });

        robotManager.addListener((OnLinuxRobotTypeListener) type -> {
            String linuxRobotType;
            switch (BuildConfig.robotType) {
                case BuildConfig.ROBOT_TYPE_DEF_SNOW:
                    linuxRobotType = "snow";
                    break;
                case BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS:
                    linuxRobotType = "alicebig";
                    break;
                case BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS:
                    linuxRobotType = "amybig";
                    break;
                case BuildConfig.ROBOT_TYPE_DEF_ALICE:
                default:
                    linuxRobotType = "alice";
                    break;
            }

            if (!type.equals(linuxRobotType)) {
                ServerFactory.getRobotState().setLinuxRobotType(linuxRobotType);
            }
        });

//        robotManager.addListener(new OnDetectPersonListener() {
//            @Override
//            public void response(int state) {
//                CsjlogProxy.getInstance().info("DetectPerson:state"+state);
//                if(state == 1){
//                    // 开启语音多次识别
//                    ServerFactory.getSpeechInstance().startIsr();
//                }else if(state == 0){
//                    // 关闭语音多次识别
//                    ServerFactory.getSpeechInstance().stopIsr();
//                }
//            }
//        });


        robotManager.addListener((OnShutdownListener) () -> {

            new Handler(Looper.myLooper()).post(() -> {

                NewRetailEdittextDialog dialog = new NewRetailEdittextDialog(HomeService.this);
                dialog.setTitle("关机提示");
                dialog.setHintText("请输入密码");
                dialog.setListener(new NewRetailEdittextDialog.OnDialogClickListener() {
                    @Override
                    public void yes(String text) {

                        SharePreferenceTools sharePreferenceTools = new SharePreferenceTools(HomeService.this);
                        if (sharePreferenceTools.getString("pwd_word") != null) {
                            if (sharePreferenceTools.getString("pwd_word").equals(text.trim())) {
                                ServerFactory.getRobotState().shutdown();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(HomeService.this, HomeService.this.getString(R.string.passWord_error), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (text.trim().equals("csjbot")) {
                                ServerFactory.getRobotState().shutdown();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(HomeService.this, HomeService.this.getString(R.string.passWord_error), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void no() {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            });

        });

        RobotManager.getInstance().addListener(new OnSyncFaceListener() {
            @Override
            public void response(int errorCode) {

            }

            @Override
            public void complete(int errorCode) {

            }
        });

        /*获取Sn监听*/
        robotManager.addListener(snListener);

        /* 连接底层通信 */
        robotManager.connect(this);

        robotManager.cameraConnect(this);

        //地图管理
        robotManager.addListener(mMapListener);

        ServerFactory.getFaceInstance().syncFaceData();
    }

    private void loadMap() {
        if (!Constants.isI18N()) {//国内版本
            SpeakProxy.getInstance().startSpeaking(getString(R.string.load_map_ing), null);
        }
        mChassis.loadMap();
        isLoadMapSuccess = false;
        mHandler.postDelayed(() -> {
            if (!isLoadMapSuccess) {
                mHandler.sendEmptyMessage(3);
            }
        }, Constants.internalCheckLinux);
    }

    private void openSpeechRecognition() {
        if (!Constants.isI18N()) {
            CsjlogProxy.getInstance().info("打开语音识别");
            speechRecognition = true;
            ServerFactory.getSpeechInstance().startIsr();
        }
    }

    private void closeSpeechRecognition() {
        CsjlogProxy.getInstance().info("关闭语音识别");
        speechRecognition = false;
        ServerFactory.getSpeechInstance().stopIsr();
    }

    Runnable runnableCloseSpeechRecognition = this::closeSpeechRecognition;


    @Override
    public void onDestroy() {
        super.onDestroy();
        CsjlogProxy.getInstance().debug("class:service method:onDestroy");
        RobotManager.getInstance().disconnect(this);
        RobotManager.getInstance().removeSnListener(snListener);
        unregBroadcast();
    }

    /**
     * 注册广播
     */
    private void regBroadcast() {
        mHomeBroadcast = new HomeBroadcast(mHandle);
        IntentFilter filter = new IntentFilter();
        filter.addAction(HomeBroadcast.ACTION_NAME);
        registerReceiver(mHomeBroadcast, filter);
    }

    /**
     * 取消广播
     */
    private void unregBroadcast() {
        unregisterReceiver(mHomeBroadcast);
    }

    /**
     * 广播接收者
     */
    public static class HomeBroadcast extends BroadcastReceiver {

        public static final String ACTION_NAME = HomeBroadcast.class.getSimpleName();
        public static final String TYPE = "TYPE";
        public static final String VALUE = "VALUE";

        private Handler mHandle;

        public HomeBroadcast(Handler handler) {
            this.mHandle = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME)) {
                Bundle bundle = intent.getExtras();
                int type = bundle.getInt(TYPE);
                String value = bundle.getString(VALUE);
                mHandle.obtainMessage(type, value).sendToTarget();
            }
        }
    }

    public void sendBroadcast(int state) {
        Intent intent = new Intent();
        intent.setAction(Constants.CONNECT_LINUX_BROADCAST);
        intent.putExtra(Constants.LINUX_CONNECT_STATE, state);
        sendBroadcast(intent);
    }

    public void sendWakeupBroadcast() {
        Intent intent = new Intent();
        intent.setAction(Constants.WAKE_UP);
        sendBroadcast(intent);
    }

    /**
     * 获得栈中最顶层的Activity
     *
     * @return
     */
    public String getTopActivity() {
        android.app.ActivityManager manager = (android.app.ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null) {
            return runningTaskInfos.get(0).topActivity.getShortClassName();
        } else
            return null;
    }

    private OnSNListener snListener = json -> {
        try {
            JSONObject object = new JSONObject(json);
            String sn = object.getString("sn").trim();
            if (TextUtils.isEmpty(sn) || TextUtils.equals(sn, "empty")) {//无效的sn

            } else {//保存sn
                Robot.setSN(sn);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CsjlogProxy.getInstance().info("HomeService: " + json);
    };

    private OnMapListener mMapListener = new OnMapListener() {//地图相关监听
        @Override
        public void saveMap(boolean state) {

        }

        @Override
        public void loadMap(boolean state) {
            CsjlogProxy.getInstance().info("是否加载地图成功" + state);
            isLoadMapSuccess = state;
            mHandler.sendEmptyMessage(state ? 1 : 2);
        }
    };

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://加载地图成功
                    CsjlogProxy.getInstance().info("加载地图成功");
                    Toast.makeText(HomeService.this, getString(R.string.restore_map_success), Toast.LENGTH_SHORT).show();
                    SharedPreUtil.putBoolean(SharedKey.ISLOADMAP, SharedKey.ISLOADMAP, true);
                    break;
                case 2://加载地图失败
                    CsjlogProxy.getInstance().info("地图恢复失败");
                    restoreMapException();
                    break;
                case 3://请检查底层连接状态
                    CsjlogProxy.getInstance().info("请检查底层连接状态");
                    break;
            }
        }
    };

    public void restoreMapException() {
        new Handler(getMainLooper()).post(() -> {
            LoadMapFileDialog dialog = new LoadMapFileDialog(HomeService.this);
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        });
    }
}
