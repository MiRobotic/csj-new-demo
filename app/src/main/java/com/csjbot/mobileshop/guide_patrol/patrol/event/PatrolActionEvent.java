package com.csjbot.mobileshop.guide_patrol.patrol.event;

/**
 *
 * @author Ben
 * @date 2018/3/23
 */

public class PatrolActionEvent {

    private String action;//隐藏显示待机页

    public PatrolActionEvent() {

    }

    public PatrolActionEvent(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
