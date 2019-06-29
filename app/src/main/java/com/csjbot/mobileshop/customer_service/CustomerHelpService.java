package com.csjbot.mobileshop.customer_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.TextUtils;

import com.csjbot.cameraclient.utils.CameraLogger;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.feature.customer.FloatWindowCustomerEvent;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.service.BaseService;
import com.csjbot.mobileshop.util.VolumeUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.NTFConstants;
import com.csjbot.coshandler.listener.OnCustomServiceMsgListener;
import com.csjbot.coshandler.log.CsjlogProxy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CustomerHelpService extends BaseService implements OnCustomServiceMsgListener {
    private IComplexActionWorker worker;
    private Map<String, Runnable> actionFromCMD = new HashMap<>();

    private Timer heatTimer;

    @Override
    public void onCreate() {
        super.onCreate();
        RobotManager.getInstance().addListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        worker = new ComplexActionWorker2();

        initMap();

        registerReceiver(tempReceiver, new IntentFilter("CustomerHelpService"));
    }

    private void initMap() {
        actionFromCMD.put("ROBOT_ACTION_NOD", ROBOT_ACTION_NOD);
        actionFromCMD.put("ROBOT_ACTION_SHAKE_HEAD", ROBOT_ACTION_SHAKE_HEAD);
        actionFromCMD.put("ROBOT_ACTION_WELCOME", ROBOT_ACTION_WELCOME);
        actionFromCMD.put("ROBOT_ACTION_UP_DOWN_LEFT_ARM", ROBOT_ACTION_UP_DOWN_LEFT_ARM);
        actionFromCMD.put("ROBOT_ACTION_UP_DOWN_RIGHT_ARM", ROBOT_ACTION_UP_DOWN_RIGHT_ARM);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    BroadcastReceiver tempReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String json = intent.getStringExtra("json");

            onMsg(json);
        }
    };

    @Override
    public void onMsg(String json) {
        JSONObject jsonObject = null;
        String msgId;
        try {
            jsonObject = new JSONObject(json);
            msgId = jsonObject.getString("msg_id");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        if (TextUtils.isEmpty(msgId)) {
            return;
        }

        switch (msgId) {
            case NTFConstants.ROBOT_COMPLEX_ACTION_NTF:
                parseComplexAction(jsonObject);
                break;
            case NTFConstants.ROBOT_SET_VOLUME_NTF:
                try {
                    int volume = jsonObject.getInt("volume");
                    setVolume(volume);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

        CsjlogProxy.getInstance().debug("RbOther default " + json);
    }

    private void parseComplexAction(JSONObject jsonObject) {
        String action;
        try {
            action = jsonObject.getString("action");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Runnable job = actionFromCMD.get(action);
        if (job != null) {
            pushJob(job);
        } else {
            CsjlogProxy.getInstance().warn("未知的动作 {}", action);
        }
    }

    private synchronized void pushJob(Runnable job) {
        if (!worker.pushJob(job)) {
            CsjlogProxy.getInstance().warn("worker is busy !");
        }
    }

    private Runnable ROBOT_ACTION_WELCOME = () -> {
        //机器人点头示意，伸右手，语音播报“欢迎光临
        Robot.getInstance().startSpeaking(getResources().getString(R.string.hello_welcome), null);
        if (BuildConfig.robotType.contains("alice")) {
            Robot.getInstance().AliceHeadDown();
            Robot.getInstance().AliceRightArmUp();

            CsjlogProxy.getInstance().debug("低头、抬右手、说您好");


            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Robot.getInstance().AliceHeadUp();
            Robot.getInstance().AliceRightArmDown();
            CsjlogProxy.getInstance().debug("抬头、放右手");
        }else {
            Robot.getInstance().snowDoubleArm();
        }
    };

    /**
     * 抬放左手一次
     */
    private Runnable ROBOT_ACTION_UP_DOWN_LEFT_ARM = () -> {
        Robot.getInstance().AliceLeftArmUp();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Robot.getInstance().AliceLeftArmDown();
    };

    /**
     * 抬放右手一次
     */
    private Runnable ROBOT_ACTION_UP_DOWN_RIGHT_ARM = () -> {
        Robot.getInstance().AliceRightArmUp();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Robot.getInstance().AliceRightArmDown();
    };

    /**
     * 摇头
     */
    private Runnable ROBOT_ACTION_SHAKE_HEAD = () -> {
        Robot.getInstance().AliceHeadLeft();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Robot.getInstance().AliceHeadRight();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Robot.getInstance().AliceHeadHReset();
    };


    /**
     * 点头
     */
    private Runnable ROBOT_ACTION_NOD = () -> {
        Robot.getInstance().AliceHeadDown();
        CsjlogProxy.getInstance().debug("低头");


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Robot.getInstance().AliceHeadUp();
        CsjlogProxy.getInstance().debug("抬头");
    };


    private void setVolume(int volume) {
        VolumeUtil.setMediaVolume(this, volume);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (heatTimer != null) {
            heatTimer.cancel();
            heatTimer.purge();
            heatTimer = null;
        }
    }

    // =============== ADD ===============
    //页面发送心跳指令
    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 100)
    public void CustomerServerDisconnect(Page2customerServerEvent event) {
        if (event != null) {
            Robot.getInstance().sendHumanServiceHeartBeat(Robot.SN);
            startTimeOut(20);
        }
    }


    //接收到心跳，等30s再发  / 停止心跳
    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 100)
    public void ResendHeatBeat(RepeatHeatEvent event) {
        if (event != null) {
            if (event.onStopHeat()) {
                CloseHeatBeat();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CloseHeatBeat();
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            CameraLogger.error(e.toString());
                        }
                        Robot.getInstance().sendHumanServiceHeartBeat(Robot.SN);
                        startTimeOut(20);
                    }
                }).start();
            }
        }
    }

    private void CloseHeatBeat () {
        if (heatTimer != null) {
            heatTimer.cancel();
            heatTimer.purge();
            heatTimer = null;
        }
    }


    //心跳定时器
    public void startTimeOut(int sec){
        if (heatTimer != null) {
            heatTimer.cancel();
            heatTimer.purge();
            heatTimer = null;
        }
        heatTimer = new Timer();
        heatTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //heatBeat lost
                Constants.sIsCustomerService = false;
                EventBus.getDefault().post(new FloatWindowCustomerEvent(false));  //关闭浮窗
                EventBus.getDefault().post(new CustomerServer2pageEvent()); //通知UI，跳出diglog提示断开
            }
        }, sec * 1000L);
    }


}
