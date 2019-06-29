package com.csjbot.mobileshop.customer_service;

/**
 * @author ShenBen
 * @date 2019/4/4 10:36
 * @email 714081644@qq.com
 */
public class RepeatHeatEvent {

    private boolean onStopHeat;

    public RepeatHeatEvent(boolean StopHeat) {
        this.onStopHeat = StopHeat;
    }

    public boolean onStopHeat() {
        return onStopHeat;
    }

}
