package com.csjbot.mobileshop.widget.chat_view.bean;

/**
 * Copyright (c) 2019, SuZhou CsjBot. All Rights Reserved.
 * www.example.com
 * <p>
 * Created by 浦耀宗 at 2019/04/10 0010-11:38.
 * Email: puyz@example.com
 */

public class GraphicBean {
    public static final String GRAPHIC_TYPE_TEXT = "1";
    public static final String GRAPHIC_TYPE_PICTURE = "2";
    public static final String GRAPHIC_TYPE_AUDIO = "3";
    public static final String GRAPHIC_TYPE_VIDEO = "4";
    public static final String GRAPHIC_TYPE_HYPERLINK= "10";

    /**
     * type : 4
     * answer : 当然帅拉
     * videoFile : [{"url":"http://csjbot-test.su.bcebos.com/W2ZGh2RiC2z774DWfFEEWmnKJsXDeWZpxPsyARzz.mp4","name":"alice_coffee.mp4"}]
     */

    private String type;
    private String answer;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
