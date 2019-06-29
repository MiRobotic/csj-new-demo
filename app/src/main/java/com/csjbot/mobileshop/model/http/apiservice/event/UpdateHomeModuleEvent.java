package com.csjbot.mobileshop.model.http.apiservice.event;

/**
 * @author ShenBen
 * @date 2019/1/3 11:50
 * @email 714081644@qq.com
 */

public class UpdateHomeModuleEvent {

    private boolean isNeedUpdate;

    public UpdateHomeModuleEvent(boolean isNeedUpdate) {
        this.isNeedUpdate = isNeedUpdate;
    }

    public boolean isNeedUpdate() {
        return isNeedUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        isNeedUpdate = needUpdate;
    }
}
