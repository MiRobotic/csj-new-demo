package com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine;

import android.util.Log;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.guide_patrol.patrol.bean.PatrolBean;
import com.csjbot.mobileshop.localbean.NaviBean;
import com.csjbot.mobileshop.model.tcp.bean.Position;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskUtils;

import java.util.List;
import java.util.logging.Handler;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class RobotNaviStateContinue extends RobotNaviStateBase{

    private static final String TAG = RobotNaviStateContinue.class.getSimpleName();

    public RobotNaviStateContinue(StateMachineManager mgr) {
        super(mgr);
        naviTaskStatus = NaviTaskStatus.CONTINUE;
    }

    @Override
    public boolean onAction(NaviAction action) {
        destroyListener();
        switch (action.getNaviTaskStatus()) {
            case ARRIVE:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.ARRIVE, null);
                break;
            case PAUSE:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.PAUSE, null);
                break;
            case BACK:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.BACK, null);
                break;
            case STOP:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.STOP, null);
                break;
            case GOWAIT:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.GOWAIT, null);
                break;
            case CIRCLE:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.CIRCLE, null);
                break;
            case SINGLECIRCLE:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.SINGLECIRCLE, null);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onEnter(NaviTaskStatus oldState, NaviAction event) {
        initPositionListener();
        super.onEnter(oldState, event);
        switch (oldState) {
            case PAUSE://单点导航还是一键导览
                continueGuide();
                break;
            default:
                break;
        }
    }

    /** 继续导航*/
    private void continueGuide() {
        if (NaviTaskUtils.isNaviSingle()) {
            continueSingleGuide();
        } else {
            continueCircleGuide();
        }
    }

    /** 继续单次导览*/
    private void continueSingleGuide() {
        List<PatrolBean> workList = NaviTaskUtils.getNaviList();
        Position position = null;
        Log.e("sunxy", "continueSingleGuide继续下一个点"+NaviTaskUtils.getCurCircleIndex()+NaviTaskUtils.getCircleStatus());
        if (NaviTaskUtils.getCurCircleIndex() == workList.size()) {
            NaviTaskUtils.setCircleStatus(2);
            position = NaviTaskUtils.getWelcomePos();
        } else {
            position = workList.get(NaviTaskUtils.getCurCircleIndex()).getPos();
        }
        if (position == null) {
            StateMachineManager.getInstance().onCircleStateError("循环导航信息有误");
            return;
        }
        //发送单点导航信息
        handler.sendEmptyMessageDelayed(NAVI_TIMEOUT, 5000);
        RobotManager.getInstance().robot.reqProxy.navi(position.toJson());
    }

    /** 继续循环导览*/
    private void continueCircleGuide() {
        List<PatrolBean> workList = NaviTaskUtils.getNaviList();
        Position position = null;
        if (NaviTaskUtils.getCircleStatus() == 2) {
            Log.e("sunxy", "继续回迎宾点");
            position = NaviTaskUtils.getWelcomePos();
        } else {
            Log.e("sunxy", "continueCircleGuide继续下一个点"+NaviTaskUtils.getCurCircleIndex()+NaviTaskUtils.getCircleStatus());
            position = workList.get(NaviTaskUtils.getCurCircleIndex()).getPos();
        }
        if (position == null) {
            StateMachineManager.getInstance().onCircleStateError("循环导航信息有误");
            return;
        }
        //发送单点导航信息
        handler.sendEmptyMessageDelayed(NAVI_TIMEOUT, 5000);
        RobotManager.getInstance().robot.reqProxy.navi(position.toJson());
    }

    @Override
    public void setListener() {
        initPositionListener();
    }

    @Override
    public void moveToActionResult() {
        if (NaviTaskUtils.isNaviSingle()) {
            StateMachineManager.getInstance().onStateContinue();
        } else {
            StateMachineManager.getInstance().onCircleStateContinue();
        }
    }

    /** 导航移动回调结果*/
    @Override
    public void moveActionResult(boolean isSuccess) {
        if (!isSuccess) {//失败的话，再次发点
            continueGuide();
            return;
        } else {
            NaviAction naviAction = new NaviAction();
            Log.e("sunxy", "continue arrive" + NaviTaskUtils.getCircleStatus());
            if (NaviTaskUtils.getCircleStatus() == 2) {//到达目标点
                naviAction.setNaviTaskStatus(NaviTaskStatus.BACK);
            } else {
                NaviTaskUtils.setCurCircleIndex(NaviTaskUtils.getCurCircleIndex()+1);
                naviAction.setNaviTaskStatus(NaviTaskStatus.ARRIVE);
            }
            StateMachineManager.getInstance().onAction(naviAction);
        }
    }

    @Override
    public void moveResultTimeOut() {
        Robot.getInstance().reqProxy.search();
        handler.sendEmptyMessageDelayed(MSG_TIMEOUT_MSG_TIMEOUT, 5000);
    }

    @Override
    public void cancelTaskResult(boolean isSuccess) {

    }
}

