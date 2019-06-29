package com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine;

import android.util.Log;

import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskUtils;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class RobotNaviStatePause extends RobotNaviStateBase{

    private static final String TAG = RobotNaviStatePause.class.getSimpleName();

    public RobotNaviStatePause(StateMachineManager mgr) {
        super(mgr);
        naviTaskStatus = NaviTaskStatus.PAUSE;
    }

    @Override
    public boolean onAction(NaviAction action) {
        switch (action.getNaviTaskStatus()) {
            case BACK:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.BACK, null);
                break;
            case CONTINUE:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.CONTINUE, null);
                break;
            case STOP:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.STOP, null);
                break;
            case GOWAIT:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.GOWAIT, null);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onEnter(NaviTaskStatus oldState, NaviAction event) {
        super.onEnter(oldState, event);
        switch (oldState) {
            case START:
            case ARRIVE:
            case BACK:
            case GOWAIT:
            case CONTINUE:
            case CIRCLE:
            case SINGLECIRCLE:
                cacelTask();
                break;
            default:
                break;
        }
    }

    /**
     * 暂停导航任务  这里的暂停就是取消导航任务
     */
    private void cacelTask() {
        handler.sendEmptyMessageDelayed(CANCEL_TASK, 5000);
        RobotManager.getInstance().robot.reqProxy.cancelNavi();
        handler.removeMessages(MSG_TIMEOUT);
        handler.removeMessages(MSG_TIMEOUT_MSG_TIMEOUT);
        handler.removeMessages(NAVI_TIMEOUT);
    }

    @Override
    public void setListener() {
        initPositionListener();
    }

    @Override
    public void moveToActionResult() {

    }

    @Override
    public void moveActionResult(boolean isSuccess) {

    }

    @Override
    public void moveResultTimeOut() {

    }

    @Override
    public void cancelTaskResult(boolean isSuccess) {
        Log.e(TAG,"cancelTaskResult"+StateMachineManager.getInstance().getCurrentState()+isSuccess);
        if (!isSuccess) {
            handler.postDelayed(()->{
                cacelTask();
            }, 100);
            return;
        }
        handler.postDelayed(() -> {
            stateMachineManager.onCircleStatePause();
        }, 1000);
    }
}

