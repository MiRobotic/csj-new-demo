package com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

import android.util.Log;

import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.BaseApplication;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.core.RobotManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnNaviSearchListener;
import com.csjbot.coshandler.listener.OnPositionListener;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 孙秀艳 on 2018/9/25.
 */

public abstract class RobotNaviStateBase {
    private static final String TAG = RobotNaviStateBase.class.getSimpleName();
    protected NaviTaskStatus naviTaskStatus;
    protected StateMachineManager stateMachineManager;
    public static final int NAVI_TIMEOUT = 10100;
    public static final int MSG_TIMEOUT = -10086;
    public static final int MSG_TIMEOUT_MSG_TIMEOUT = -10087;// 超时消息下发超时
    public static final int CANCEL_TASK = 11111111;

    public RobotNaviStateBase(StateMachineManager mgr) {
        stateMachineManager = mgr;
        setListener();
    }

    public void setContext(StateMachineManager stm) {
        this.stateMachineManager = stm;
    }

    abstract public boolean onAction(NaviAction action);

    public void onExit(NaviAction event) {
        stateMachineManager.onStateExited(naviTaskStatus);
    }

    public void onEnter(NaviTaskStatus oldState, NaviAction event) {
        stateMachineManager.onStateEntered(naviTaskStatus);
    }

    abstract public void setListener();//设置位置和搜索监听

    abstract public void moveToActionResult();//消息下发结果回调

    abstract public void moveActionResult(boolean isSuccess);//移动结果回调

    abstract public void moveResultTimeOut();//超时

    abstract public void cancelTaskResult(boolean isSuccess);//取消导航结果回调

    /**
     * 分发移动结果
     */
    OnPositionListener positionListener = new OnPositionListener() {
        @Override
        public void positionInfo(String json) {
        }

        @Override
        public void moveResult(String json) {//移动结果回调
            if (naviTaskStatus != stateMachineManager.getCurrentState()) {
                return;
            }
            handler.removeMessages(MSG_TIMEOUT);
            handler.removeMessages(MSG_TIMEOUT_MSG_TIMEOUT);
            CsjlogProxy.getInstance().info(TAG,"进入了移动结果回调，返回json：" + json+naviTaskStatus);
            boolean isSuccess = false;
            int error_code = 0;
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(json);
                error_code = jsonObj.optInt("error_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (error_code != 0) {
                CsjlogProxy.getInstance().info(TAG,"Navi:走点失败，状态码：" + error_code);
                if (error_code == 20007) {
                    if (NaviTaskUtils.isNaviSingle()) {
                        StateMachineManager.getInstance().oStateCircusee();
                    } else {
                        StateMachineManager.getInstance().onCircleStateCircusee();
                    }
                }
                isSuccess = false;
            } else {
                isSuccess = true;
            }
            handler.removeMessages(MSG_TIMEOUT);
            handler.removeMessages(MSG_TIMEOUT_MSG_TIMEOUT);
            if (isSuccess) {
                moveActionResult(isSuccess);
            } else {
                moveResultTimeOut();
            }
        }

        @Override
        public void moveToResult(String json) {//消息下发成功
            if (naviTaskStatus != stateMachineManager.getCurrentState()) {
                return;
            }
            CsjlogProxy.getInstance().info(TAG,"消息下发成功"+naviTaskStatus);
            handler.removeMessages(NAVI_TIMEOUT);//移除超时信息
            handler.sendEmptyMessageDelayed(MSG_TIMEOUT, 20000);//发送到点超时信息
            moveToActionResult();
        }

        @Override
        public void cancelResult(String json) {//取消任务成功
            if (naviTaskStatus != stateMachineManager.getCurrentState()) {
                return;
            }
            handler.removeMessages(CANCEL_TASK);
            handler.removeMessages(MSG_TIMEOUT);
            handler.removeMessages(MSG_TIMEOUT_MSG_TIMEOUT);
            boolean isSuccess = false;
            int error_code = 0;
            try {
                JSONObject jo = new JSONObject(json);
                error_code = jo.optInt("error_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (error_code == 0) {
                CsjlogProxy.getInstance().info(TAG,"取消任务成功"+naviTaskStatus);
                isSuccess = true;
            } else {
                isSuccess = false;
            }
            cancelTaskResult(isSuccess);
        }
    };

    public OnNaviSearchListener naviSearchListener = new OnNaviSearchListener() {
        @Override
        public void searchResult(String json) {
            CsjlogProxy.getInstance().info(TAG,"Navi：searchResult" + naviTaskStatus + stateMachineManager.getCurrentState());
//            if (amyState != stateMachineManager.getAmyRobotState()) {
//                return;
//            }
            handler.post(() -> {
                handler.removeMessages(MSG_TIMEOUT);
                handler.removeMessages(MSG_TIMEOUT_MSG_TIMEOUT);

                try {
                    JSONObject jo = new JSONObject(json);
                    int state = jo.optInt("state");
                    if (state == 0) {//空闲状态
                        moveActionResult(false);//继续送餐到达等后续流程
//                        if (stateMachineManager.getAmyRobotState() == AmyStatus.BACK) {
//                            stateMachineManager.tarnsState(AmyStatus.BACK, null);
//                        } else if (stateMachineManager.getAmyRobotState() == AmyStatus.SENDFOOD) {
//                            stateMachineManager.tarnsState(AmyStatus.SENDFOOD, null);
//                        }
                        CsjlogProxy.getInstance().info(TAG,"Navi：state==0，尝试重新走点");
                    } else if (state == 1) {
                        CsjlogProxy.getInstance().info(TAG,"Navi：state==1，尝试重新走点");
                        handler.sendEmptyMessageDelayed(MSG_TIMEOUT, 20000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    };

    /**
     * 异常处理
     */
    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CANCEL_TASK:
                    if (naviTaskStatus != stateMachineManager.getCurrentState()) {
                        return;
                    }
                    CsjlogProxy.getInstance().info(TAG,"取消任务失败"+naviTaskStatus);
                    StateMachineManager.getInstance().onStateError(BaseApplication.getAppContext().getString(R.string.cancel_failed));
                    StateMachineManager.getInstance().onCircleStateError("取消任务失败，请检查底层连接");
                    StateMachineManager.getInstance().setInitState(NaviTaskStatus.AWAIT);
                    break;
                case NAVI_TIMEOUT:
                    if (naviTaskStatus != stateMachineManager.getCurrentState()) {
                        return;
                    }
                    CsjlogProxy.getInstance().info(TAG,"请检查地盘"+naviTaskStatus);
//                    if (amyState == AmyStatus.CIRCLE) {
                    handler.sendEmptyMessageDelayed(MSG_TIMEOUT, 20000);
                    /*} else {
                        StateMachineManager.getInstance().onStateError(CsjBotApplication.application.getString(R.string.plz_check_slam));
                        StateMachineManager.getInstance().setInitState(AmyStatus.IDLE);
                    }*/
                    break;
                case MSG_TIMEOUT:
                    if (naviTaskStatus != stateMachineManager.getCurrentState()) {
                        return;
                    }
                    CsjlogProxy.getInstance().info(TAG,"超时，查询是否到点超时"+naviTaskStatus);
//                    StateMachineManager.getInstance().onStateError("超时，查询是否到点超时");
                    moveResultTimeOut();
                    break;
                case MSG_TIMEOUT_MSG_TIMEOUT:
                    if (naviTaskStatus != stateMachineManager.getCurrentState()) {
                        return;
                    }
                    CsjlogProxy.getInstance().info(TAG,"超时消息下发超时"+naviTaskStatus);
//                    StateMachineManager.getInstance().onStateError("超时消息下发超时");
                    moveResultTimeOut();
                    break;
                default:
                    break;
            }
        }
    };

    public void initPositionListener() {
        RobotManager.getInstance().addPositionListener(positionListener);
        Robot.getInstance().setOnNaviSearchListener(naviSearchListener);
//        Robot.getInstance().registerNaviSearchListener(naviSearchListener);
    }

    public void destroyListener() {
        RobotManager.getInstance().removePositionListener(positionListener);
//        Robot.getInstance().unregisterNaviSearchListener(naviSearchListener);
        Robot.getInstance().setOnNaviSearchListener(null);
        Robot.getInstance().setPositionListener(null);
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void finalize() throws Throwable {
        Log.e(TAG,"finalize"+naviTaskStatus);
        destroyListener();
        super.finalize();
    }
}

