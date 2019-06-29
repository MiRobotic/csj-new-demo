package com.csjbot.mobileshop.feature.Learning.event;

/**
 * @author ShenBen
 * @date 2018/12/19 8:32
 * @email 714081644@qq.com
 */

public class FloatWindowBackEvent {
    private boolean isShow;

    public FloatWindowBackEvent(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
