package com.csjbot.mobileshop.widget.chat_view.bean;

import java.util.List;

/**
 * Copyright (c) 2019, SuZhou CsjBot. All Rights Reserved.
 * www.example.com
 * <p>
 * Created by 浦耀宗 at 2019/04/09 0009-21:01.
 * Email: puyz@example.com
 */

public class GraphicVideoBean {

    /**
     * type : 2
     * answer : 我想吃红烧肉
     * videoFile : [{"url":"http://csjbot-test.su.bcebos.com/mkYycGynG8KsP5SwBn7EKwHwfy7YRxdHmfXaTKmM.mp4"},{"url":"http://csjbot-test.su.bcebos.com/MFxpxFMmS6Q4FEsEPymmxkNJHd5RyCWZPmrbYJB3.jpg"}]
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

    private List<VideoFileBean> videoFile;


    public List<VideoFileBean> getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(List<VideoFileBean> videoFile) {
        this.videoFile = videoFile;
    }

    public static class VideoFileBean {
        /**
         * url : http://csjbot-test.su.bcebos.com/mkYycGynG8KsP5SwBn7EKwHwfy7YRxdHmfXaTKmM.mp4
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
