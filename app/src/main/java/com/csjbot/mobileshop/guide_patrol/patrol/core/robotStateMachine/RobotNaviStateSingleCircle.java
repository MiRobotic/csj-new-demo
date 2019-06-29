package com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine;

import android.util.Log;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.guide_patrol.patrol.bean.PatrolBean;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskUtils;
import com.csjbot.mobileshop.model.tcp.bean.Position;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class RobotNaviStateSingleCircle extends RobotNaviStateBase{

    private static final String TAG = RobotNaviStateSingleCircle.class.getSimpleName();
    private static volatile int curIndex = 0;
    private static List<Position> workList = new ArrayList<>();//一键导航点信息

    public RobotNaviStateSingleCircle(StateMachineManager mgr) {
        super(mgr);
        naviTaskStatus = NaviTaskStatus.SINGLECIRCLE;
    }

    @Override
    public boolean onAction(NaviAction action) {
        destroyListener();
        switch (action.getNaviTaskStatus()) {
            case GOWAIT:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.GOWAIT, null);
                break;
            case ARRIVE:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.ARRIVE, null);
                break;
            case CIRCLE:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.CIRCLE, null);
                break;
            case STOP:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.STOP, null);
                break;
            case PAUSE:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.PAUSE, null);
                break;
            case BACK:
                StateMachineManager.getInstance().tarnsState(NaviTaskStatus.BACK, null);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onEnter(NaviTaskStatus oldState, NaviAction event) {
        super.onEnter(oldState, event);
        initPositionListener();
        switch (oldState) {
            case AWAIT:
                cacelTask();
                curIndex = 0;
                NaviTaskUtils.setCircleStatus(-1);
                NaviTaskUtils.setCurCircleIndex(curIndex);
                break;
            case ARRIVE:
            case CONTINUE:
                curIndex = NaviTaskUtils.getCurCircleIndex();
                nextGuide();
                break;
            default:
                break;
        }
    }

    /**
     * 取消当前送餐任务
     */
    private void cacelTask() {
        handler.sendEmptyMessageDelayed(CANCEL_TASK, 50000);
        RobotManager.getInstance().robot.reqProxy.cancelNavi();
        handler.removeMessages(MSG_TIMEOUT);
        handler.removeMessages(MSG_TIMEOUT_MSG_TIMEOUT);
        handler.removeMessages(NAVI_TIMEOUT);
    }

    /** 开始一键导航*/
    private void startGuide() {
        // 获取一键导览数据
        workList = getCirclePos();
        // 没有数据，跳转到设置界面
        if (workList == null || workList.size() == 0) {
            stateMachineManager.onCircleStateError("您还没有设置一键导览数据！");
        } else if (workList != null && workList.size() <= curIndex) {//按理说不应该出现，不知道什么时候会出现
            stateMachineManager.onCircleStateError("导览出错！");
        } else {
            nextGuide();
        }
    }

    /** 继续下一个导航*/
    private void nextGuide() {
        Log.e("sunxy","ccc继续导航出错"+curIndex+workList.size());
        if (curIndex == workList.size()) {
            back();
        } else if (workList != null && curIndex < workList.size()) {
            Position curPosition = workList.get(curIndex);
            //发送走点信息
            handler.sendEmptyMessageDelayed(NAVI_TIMEOUT, 5000);
            RobotManager.getInstance().robot.reqProxy.navi(curPosition.toJson());
        } else {
            stateMachineManager.onCircleStateError("继续导航出错");
        }
    }

    private void back() {
        NaviAction naviAction = new NaviAction();
        naviAction.setNaviTaskStatus(NaviTaskStatus.BACK);
        StateMachineManager.getInstance().onAction(naviAction);
    }

    private List<Position> getCirclePos() {
        String json = SharedPreUtil.getString(SharedKey.PATROL_LINE_NAME, SharedKey.PATROL_LINE_KEY);
        List<PatrolBean> naviBeanList = GsonUtils.jsonToObject(json, new TypeToken<List<PatrolBean>>() {
        }.getType());
        List<Position> positions = new ArrayList<>();
        if (naviBeanList != null && naviBeanList.size() > 0) {
            for (int i=0; i<naviBeanList.size(); i++) {
                positions.add(naviBeanList.get(i).getPos());
            }
        }
        return positions;
    }

    @Override
    public void setListener() {
        initPositionListener();
    }

    @Override
    public void moveToActionResult() {
        NaviTaskUtils.setCircleStatus(0);
        NaviTaskUtils.setCurCircleIndex(curIndex);
        stateMachineManager.onCircleStateStart(curIndex);
    }

    @Override
    public void moveActionResult(boolean isSuccess) {
        if (!isSuccess) {//失败的话，再次发点
            startGuide();
            return;
        } else {
            curIndex++;
            NaviTaskUtils.setCurCircleIndex(curIndex);
            NaviTaskUtils.setCircleStatus(1);
            NaviAction naviAction = new NaviAction();
            naviAction.setNaviTaskStatus(NaviTaskStatus.ARRIVE);
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
        Log.e(TAG,"cancelTaskResult"+StateMachineManager.getInstance().getCurrentState()+isSuccess);
        if (!isSuccess) {
            handler.postDelayed(()->{
                cacelTask();
            }, 100);
            return;
        }
        handler.postDelayed(() -> {
            startGuide();
        }, 1000);
    }
}

