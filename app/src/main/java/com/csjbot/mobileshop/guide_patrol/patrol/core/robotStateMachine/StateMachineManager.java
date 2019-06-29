package com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine;

import android.util.Log;

import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 孙秀艳 on 2019/1/18.
 * 机器人状态管理器
 */

public class StateMachineManager {
    private static final String TAG = StateMachineManager.class.getSimpleName();

    private Map<NaviTaskStatus, RobotNaviStateBase> all_states = new HashMap<>();
    private volatile static NaviTaskStatus oldState = NaviTaskStatus.AWAIT;
    private volatile static NaviTaskStatus currentState = NaviTaskStatus.AWAIT;
    private INaviState onStateListener;
    private INaviCircleState onCircleStateListener;
    private volatile static StateMachineManager stateMachineManager;

    public static StateMachineManager getInstance() {
        if (stateMachineManager == null) {
            synchronized (StateMachineManager.class) {
                if (stateMachineManager == null) {
                    stateMachineManager = new StateMachineManager();
                }
            }
        }
        return stateMachineManager;
    }

    /**
     * 获取当前的导航状态
     */
    public NaviTaskStatus getCurrentState() {
        return currentState;
    }

    /**
     * 获取前一个导航状态
     */
    public NaviTaskStatus getOldState() {
        return oldState;
    }

    /**
     * 机器人当前状态是否是空闲
     */
    public boolean isRobotIdle() {
        return currentState == NaviTaskStatus.AWAIT;
    }

    /**
     * 机器人当前状态是不是到达状态
     */
    public boolean isRobotArrive() {
        return currentState == NaviTaskStatus.ARRIVE;
    }

    public StateMachineManager() {
        this.register(NaviTaskStatus.AWAIT, new RobotNaviStateAwait(this));
        this.register(NaviTaskStatus.BACK, new RobotNaviStateBack(this));
        this.register(NaviTaskStatus.ARRIVE, new RobotNaviStateArrive(this));
        this.register(NaviTaskStatus.GOWAIT, new RobotNaviStateGoWait(this));
        this.register(NaviTaskStatus.CONTINUE, new RobotNaviStateContinue(this));
        this.register(NaviTaskStatus.CIRCLE, new RobotNaviStateCircle(this));
        this.register(NaviTaskStatus.PAUSE, new RobotNaviStatePause(this));
        this.register(NaviTaskStatus.STOP, new RobotNaviStateStop(this));
        this.register(NaviTaskStatus.SINGLECIRCLE, new RobotNaviStateSingleCircle(this));
    }

    /**
     * 核心方法，所有的动作都是由事件开始，在状态中做事件的处理，并且由 this 做状态转换
     */
    public void onAction(NaviAction action) {
        all_states.get(currentState).onAction(action);
    }

    /**
     * 注册状态
     * @param id    状态id
     * @param state 状态的实体
     */
    private void register(NaviTaskStatus id, RobotNaviStateBase state) {
        all_states.put(id, state);
    }

    /**
     * 转换状态
     * @param newState 新的状态
     * @return 暂时返回true
     */
    public boolean tarnsState(NaviTaskStatus newState, NaviAction event) {
        oldState = currentState;
        currentState = newState;
        all_states.get(oldState).onExit(event);
        all_states.get(newState).onEnter(oldState, event);
        return true;
    }

    /**
     * 设置初始化状态
     * @param state 初始化状态
     */
    public void setInitState(NaviTaskStatus state) {
        currentState = state;
    }

    /**
     * 状态进入通知，用于接口回调到应用层，例如 mListener.onStateEntered(state)
     * @param state 状态
     */
    public void onStateEntered(NaviTaskStatus state) {
        Log.e(TAG, "onStateEntered " + state);
    }

    /**
     * 状态退出通知，用于接口回调到应用层，例如 mListener.onStateEntered(state)
     * @param state 状态
     */
    public void onStateExited(NaviTaskStatus state) {
        Log.e(TAG, "onStateExited " + state);
    }

    public void setStateListener(INaviState listener) {
        onStateListener = listener;
    }

    public void setCircleStateListener(INaviCircleState listener) {
        onCircleStateListener = listener;
    }

    /**
     * 用于接口通知UI层，机器人到达送餐位置
     */
    public void onStateArrive() {
        if (onStateListener != null) {
            onStateListener.robotArrive();
        }
    }

    /**
     * 用于接口通知UI层，机器人返回到等待点
     */
    public void onStateBack() {
        if (onStateListener != null) {
            onStateListener.robotBack();
        }
    }

    /**
     * 用于接口通知UI层，机器人可以正常送餐
     */
    public void onStateStart() {
        if (onStateListener != null) {
            onStateListener.robotStart();
        }
    }

    /**
     * 用于接口通知UI层，机器人暂停任务
     */
    public void onStatePause() {
        if (onStateListener != null) {
            onStateListener.robotPause();
        }
    }

    /**
     * 用于接口通知UI层，机器人继续任务
     */
    public void onStateContinue() {
        if (onStateListener != null) {
            onStateListener.robotContinue();
        }
    }

    /**
     * 用于接口通知UI层，机器人停止任务
     */
    public void onStateStop() {
        if (onStateListener != null) {
            onStateListener.robotStop();
        }
    }

    /**
     * 用于接口通知UI层，机器人送餐过程中的错误信息
     * @param errorMsg 错误提示信息
     */
    public void onStateError(String errorMsg) {
        if (onStateListener != null) {
            onStateListener.robotError(errorMsg);
        }
    }

    /**
     * 用于接口通知UI层，机器人返回等待点途中
     */
    public void onStateGoWaiting() {
        if (onStateListener != null) {
            onStateListener.robotGoWaiting();
        }
    }

    /**
     * 用于接口通知UI层，机器人被围观
     */
    public void oStateCircusee() {
        if (onStateListener != null) {
            onStateListener.robotCircusee();
        }
    }

    /**
     * 用于接口通知UI层，机器人到达送餐位置
     */
    public void onCircleStateArrive(int index) {
        if (onCircleStateListener != null) {
            onCircleStateListener.robotArrive(index);
        }
    }

    /**
     * 用于接口通知UI层，机器人返回到等待点
     */
    public void onCircleStateBack() {
        if (onCircleStateListener != null) {
            onCircleStateListener.robotBack();
        }
    }

    /**
     * 用于接口通知UI层，机器人可以正常送餐
     */
    public void onCircleStateStart(int index) {
        if (onCircleStateListener != null) {
            onCircleStateListener.robotStart(index);
        }
    }

    /**
     * 用于接口通知UI层，机器人暂停任务
     */
    public void onCircleStatePause() {
        if (onCircleStateListener != null) {
            onCircleStateListener.robotPause();
        }
    }

    /**
     * 用于接口通知UI层，机器人继续任务
     */
    public void onCircleStateContinue() {
        if (onCircleStateListener != null) {
            onCircleStateListener.robotContinue();
        }
    }

    /**
     * 用于接口通知UI层，机器人停止任务
     */
    public void onCircleStateStop() {
        if (onCircleStateListener != null) {
            onCircleStateListener.robotStop();
        }
    }

    /**
     * 用于接口通知UI层，机器人送餐过程中的错误信息
     * @param errorMsg 错误提示信息
     */
    public void onCircleStateError(String errorMsg) {
        if (onCircleStateListener != null) {
            onCircleStateListener.robotError(errorMsg);
        }
    }

    /**
     * 用于接口通知UI层，机器人返回等待点途中
     */
    public void onCircleStateGoWaiting() {
        if (onCircleStateListener != null) {
            onCircleStateListener.robotGoWaiting();
        }
    }

    /**
     * 用于接口通知UI层，机器人被围观
     */
    public void onCircleStateCircusee() {
        if (onCircleStateListener != null) {
            onCircleStateListener.robotCircusee();
        }
    }
}

