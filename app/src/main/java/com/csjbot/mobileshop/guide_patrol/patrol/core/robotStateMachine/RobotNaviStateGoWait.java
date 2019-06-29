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

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class RobotNaviStateGoWait extends RobotNaviStateBase{

    private static final String TAG = RobotNaviStateGoWait.class.getSimpleName();

    public RobotNaviStateGoWait(StateMachineManager mgr) {
        super(mgr);
        naviTaskStatus = NaviTaskStatus.GOWAIT;
    }

    @Override
    public boolean onAction(NaviAction action) {
        destroyListener();
        switch (action.getNaviTaskStatus()) {
            case AWAIT:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.AWAIT, null);
                break;
            case PAUSE:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.PAUSE, null);
                break;
            case STOP:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.STOP, null);
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
            case AWAIT:
            case STOP://###
            case ARRIVE:
            case PAUSE:
            case CONTINUE:
            case CIRCLE:
            case SINGLECIRCLE:
                back();
                break;
            default:
                break;
        }
    }

    /** 返回迎宾点*/
    private void back() {
        Log.e(TAG,"准备返回等待点");
        if (NaviTaskUtils.isNaviSingle()) {
            List<PatrolBean> workList = NaviTaskUtils.getNaviList();
            if (workList != null) {
                NaviTaskUtils.setCurCircleIndex(workList.size());
            }
        }
        //获取等待点位置
        Position position = NaviTaskUtils.getWelcomePos();
        NaviTaskUtils.setCircleStatus(2);
        if (position == null) {
            return;
        }
        //发送走点信息
        handler.sendEmptyMessageDelayed(NAVI_TIMEOUT, 5000);
        RobotManager.getInstance().robot.reqProxy.navi(position.toJson());
    }

    @Override
    public void setListener() {
        initPositionListener();
    }

    @Override
    public void moveToActionResult() {
        NaviTaskUtils.setCircleStatus(2);
        stateMachineManager.onCircleStateGoWaiting();
    }

    @Override
    public void moveActionResult(boolean isSuccess) {
        Log.e(TAG,"我回来了");
        //收到到点信息
        if (!isSuccess) {//导览失败，再次发点
            back();
            return;
        } else {
            NaviAction amyAction = new NaviAction();
            amyAction.setNaviTaskStatus(NaviTaskStatus.AWAIT);
            StateMachineManager.getInstance().onAction(amyAction);
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

