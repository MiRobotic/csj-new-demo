package com.csjbot.mobileshop.model.http.apiservice.event;

/**
 * @author ShenBen
 * @date 2019/1/3 16:58
 * @email 714081644@qq.com
 */

public class UpdateContentTypeEvent {
    private boolean isNeedUpdate;

    public UpdateContentTypeEvent(boolean isNeedUpdate) {
        this.isNeedUpdate = isNeedUpdate;
    }

    public boolean isNeedUpdate() {
        return isNeedUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        isNeedUpdate = needUpdate;
    }
}
