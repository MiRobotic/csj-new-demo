package com.csjbot.mobileshop.guide_patrol.patrol.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine.INaviCircleState;
import com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine.INaviState;
import com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine.NaviAction;
import com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine.StateMachineManager;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.model.tcp.tts.ISpeak;

/**
 * Created by 孙秀艳 on 2019/1/21.
 */

public class RobotProxy {
    private volatile static RobotProxy amyRobotProxy;
    public static StateMachineManager stateMachineManager;
    public static INaviCircleState iNaviState;
    private static volatile Runnable currentRunnable = null;
    public static final int ACTION_TOUCH = 10000;//摸手
    private static volatile boolean isFlag = false;//是否进行了继续送餐任务
    private static volatile boolean isBlock = false;//是否被阻挡
    private static volatile boolean isCancel = false;//送餐任务是否取消
    private static ISpeak mSpeak;

    public static RobotProxy getInstance() {
        if (amyRobotProxy == null) {
            synchronized (RobotProxy.class) {
                if (amyRobotProxy == null) {
                    amyRobotProxy = new RobotProxy();
                    setRobotStateListener();
                    mSpeak = ServerFactory.getSpeakInstance();
                }
            }
        }
        return amyRobotProxy;
    }

    public RobotProxy() {
        if (stateMachineManager == null) {
            stateMachineManager = StateMachineManager.getInstance();
        }
    }

    /**
     * 开始循环
     */
    public void startCircle() {
        stateMachineManager.setInitState(NaviTaskStatus.AWAIT);
        NaviAction robotAction = new NaviAction();
        robotAction.setNaviTaskStatus(NaviTaskStatus.CIRCLE);
        stateMachineManager.onAction(robotAction);
    }

    /**
     * 开始单次循环
     */
    public void startSingleCircle() {
        stateMachineManager.setInitState(NaviTaskStatus.AWAIT);
        NaviAction robotAction = new NaviAction();
        robotAction.setNaviTaskStatus(NaviTaskStatus.SINGLECIRCLE);
        stateMachineManager.onAction(robotAction);
    }

    /**
     * 返回等待点
     */
    public void backWelcomePos() {
        NaviAction amyAction = new NaviAction();
        amyAction.setNaviTaskStatus(NaviTaskStatus.GOWAIT);
        stateMachineManager.onAction(amyAction);
    }

    public void cancelTask() {
        NaviAction amyAction = new NaviAction();
        amyAction.setNaviTaskStatus(NaviTaskStatus.STOP);
        stateMachineManager.onAction(amyAction);
    }

    public void continueTask() {
        NaviAction robotAction = new NaviAction();
        robotAction.setNaviTaskStatus(NaviTaskStatus.CONTINUE);
        stateMachineManager.onAction(robotAction);
    }

    public void pauseTask() {
        NaviAction robotAction = new NaviAction();
        robotAction.setNaviTaskStatus(NaviTaskStatus.PAUSE);
        stateMachineManager.onAction(robotAction);
    }

    private static Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_TOUCH:
                    if (isFlag) {
                        isFlag = false;
                        if (currentRunnable != null) {
                            handler.removeCallbacks(currentRunnable);
                            currentRunnable = null;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 处理送餐过程中 每种可能状态的情况
     */
    public void setSendStateListener(INaviCircleState robotState) {
        iNaviState = robotState;
    }

    public static void setRobotStateListener() {
        stateMachineManager.setCircleStateListener(new INaviCircleState() {
            @Override
            public void robotStart(int curindex) {
                if (iNaviState != null) {
                    iNaviState.robotStart(curindex);
                }
            }

            @Override
            public void robotArrive(int curindex) {

            }

            @Override
            public void robotBack() {
                if (iNaviState != null) {
                    iNaviState.robotBack();
                }
            }

            @Override
            public void robotError(String msg) {

            }

            @Override
            public void robotPause() {

            }

            @Override
            public void robotContinue() {

            }

            @Override
            public void robotStop() {

            }

            @Override
            public void robotGoWaiting() {

            }

            @Override
            public void robotCircusee() {

            }
        });
    }

    /**
     * 设置摸手事件
     */
    public void setAmyTouchAction() {
        if (StateMachineManager.getInstance().getCurrentState() == NaviTaskStatus.ARRIVE) {
            handler.sendEmptyMessage(ACTION_TOUCH);
        }
    }
}


