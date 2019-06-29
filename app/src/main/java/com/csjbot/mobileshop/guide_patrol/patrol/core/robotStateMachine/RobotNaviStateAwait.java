package com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine;

import android.util.Log;

import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskUtils;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class RobotNaviStateAwait extends RobotNaviStateBase{

    private static final String TAG = RobotNaviStateAwait.class.getSimpleName();

    public RobotNaviStateAwait(StateMachineManager mgr) {
        super(mgr);
        naviTaskStatus = NaviTaskStatus.AWAIT;
    }

    @Override
    public boolean onAction(NaviAction action) {
        switch (action.getNaviTaskStatus()) {
            case START://开始单点导览
                Log.e(TAG,"开始带我去功能");
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.START, null);
                break;
            case CIRCLE:
                Log.e(TAG,"一键导航");
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.CIRCLE, null);
                break;
            case SINGLECIRCLE:
                Log.e(TAG,"单次导航");
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.SINGLECIRCLE, null);
                break;
            case BACK:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.BACK, null);
                break;
            case GOWAIT://返回迎宾点
                Log.e(TAG,"返回迎宾点");
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.GOWAIT, null);
                break;
            case STOP:
                Log.e(TAG,"停止");
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.STOP, null);
            default:
                break;

        }
        return true;
    }

    @Override
    public void onEnter(NaviTaskStatus oldState, NaviAction event) {
        super.onEnter(oldState, event);
        switch (oldState) {
            case GOWAIT:
            case BACK:
                NaviTaskUtils.setCircleStatus(-1);
                StateMachineManager.getInstance().onCircleStateBack();
                break;
            default:
                break;
        }
    }

    @Override
    public void setListener() {

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

    }
}

