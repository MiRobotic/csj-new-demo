package com.csjbot.mobileshop.guide_patrol.patrol.event;

/**
 *
 * @author Ben
 * @date 2018/3/23
 */

public class PatrolEvent {

    public static final int START = 1;//开始巡航
    public static final int PLAYING = 2;//媒体全屏播放
    public static final int PLAYEND = 3;//媒体全屏播放完成
    private int action;

    public PatrolEvent() {
        this.action = START;
    }

    public PatrolEvent(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
