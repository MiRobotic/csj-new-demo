package com.csjbot.mobileshop.feature.customer;

/**
 * @author ShenBen
 * @date 2018/12/19 8:32
 * @email 714081644@qq.com
 */

public class FloatWindowCustomerEvent {

    private boolean isShow = false;

    public FloatWindowCustomerEvent(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

}
