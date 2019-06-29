package com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine;

import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskUtils;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class RobotNaviStateArrive extends RobotNaviStateBase{

    private static final String TAG = RobotNaviStateArrive.class.getSimpleName();

    public RobotNaviStateArrive(StateMachineManager mgr) {
        super(mgr);
        naviTaskStatus = NaviTaskStatus.ARRIVE;
    }

    @Override
    public boolean onAction(NaviAction action) {
        switch (action.getNaviTaskStatus()) {//导航到点后返回迎宾点
            case CIRCLE:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.CIRCLE, null);
                break;
            case SINGLECIRCLE:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.SINGLECIRCLE, null);
                break;
            case BACK:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.BACK, null);
                break;
            case PAUSE:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.PAUSE, null);
                break;
            case STOP://###
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
            case CONTINUE:
                if (NaviTaskUtils.isNaviSingle()) {
                    if (NaviTaskUtils.getCircleStatus() == 2) {
                        back();
                    } else {
                        arriveSingleCircle();
                    }
                } else {//一键导览 待修改
                    arriveCircle();
                }
                break;
            case CIRCLE:
                arriveCircle();
                break;
            case SINGLECIRCLE:
                arriveSingleCircle();
                break;
            case START:
                arrive();
                break;
            default:
                break;
        }
    }

    private void arriveSingleCircle() {
        NaviTaskUtils.setCircleStatus(1);
        StateMachineManager.getInstance().onCircleStateArrive(NaviTaskUtils.getCurCircleIndex());
        NaviAction naviAction = new NaviAction();
        naviAction.setNaviTaskStatus(NaviTaskStatus.SINGLECIRCLE);
        StateMachineManager.getInstance().onAction(naviAction);
    }

    /** 到达导航点，通知上层*/
    private void arriveCircle() {
        NaviTaskUtils.setCircleStatus(1);
        StateMachineManager.getInstance().onCircleStateArrive(NaviTaskUtils.getCurCircleIndex());
        NaviAction naviAction = new NaviAction();
        naviAction.setNaviTaskStatus(NaviTaskStatus.CIRCLE);
        StateMachineManager.getInstance().onAction(naviAction);
    }

    /** 返回迎宾点，通知上层*/
    private void back() {
        NaviTaskUtils.setCircleStatus(-1);
        StateMachineManager.getInstance().onCircleStateBack();
    }

    /** 到达导航点，通知上层*/
    private void arrive() {
        NaviTaskUtils.setCircleStatus(1);
        StateMachineManager.getInstance().onStateArrive();
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

