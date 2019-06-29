package com.csjbot.mobileshop.advertisement.event;

/**
 * @author ShenBen
 * @date 2018/12/4 17:01
 * @email 714081644@qq.com
 */

public class AdvertisementEvent {
    /**
     * 显示、隐藏事件
     */
    public static final int SHOW_HIDE_EVENT = 1;
    /**
     * 询问事件
     */
    public static final int ASK_EVENT = 2;
    private int actionType;
    private boolean isHide;
    private String askMsg;

    public AdvertisementEvent(int actionType, boolean isHide) {
        this.actionType = actionType;
        this.isHide = isHide;
    }

    public AdvertisementEvent(int actionType, String askMsg) {
        this.actionType = actionType;
        this.askMsg = askMsg;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }

    public String getAskMsg() {
        return askMsg;
    }

    public void setAskMsg(String askMsg) {
        this.askMsg = askMsg;
    }
}
