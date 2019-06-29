package com.csjbot.mobileshop.widget.chat_view;

/**
 * Copyright (c) 2019, SuZhou CsjBot. All Rights Reserved.
 * www.example.com
 * <p>
 * Created by 浦耀宗 at 2019/04/11 0011-11:25.
 * Email: puyz@example.com
 */

public class ChatVideoEvent {
    private int eventType;
    private Object obj;

    public ChatVideoEvent(int eventType, Object obj) {
        this.obj = obj;
        this.eventType = eventType;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
