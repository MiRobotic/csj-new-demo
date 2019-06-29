package com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine;

import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class NaviAction {
    private NaviTaskStatus naviTaskStatus;

    public NaviTaskStatus getNaviTaskStatus() {
        return naviTaskStatus;
    }

    public void setNaviTaskStatus(NaviTaskStatus naviTaskStatus) {
        this.naviTaskStatus = naviTaskStatus;
    }
}