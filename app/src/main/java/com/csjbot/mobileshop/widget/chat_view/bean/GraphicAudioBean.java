package com.csjbot.mobileshop.widget.chat_view.bean;

import java.util.List;

/**
 * Copyright (c) 2019, SuZhou CsjBot. All Rights Reserved.
 * www.example.com
 * <p>
 * Created by 浦耀宗 at 2019/04/09 0009-21:01.
 * Email: puyz@example.com
 */

public class GraphicAudioBean {

    /**
     * type : 3
     * answer : 当然好吃拉
     * audioFile : [{"url":"http://csjbot-test.su.bcebos.com/kX6fwEDNa4ZDr6QJWdc7ws6DnmXPNyHQJ8nx8KWF.MP3","name":"尚法昆山.MP3"}]
     */

    private String type;
    private String answer;
    private List<AudioFileBean> audioFile;

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

    public List<AudioFileBean> getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(List<AudioFileBean> audioFile) {
        this.audioFile = audioFile;
    }

    public static class AudioFileBean {
        /**
         * url : http://csjbot-test.su.bcebos.com/kX6fwEDNa4ZDr6QJWdc7ws6DnmXPNyHQJ8nx8KWF.MP3
         * name : 尚法昆山.MP3
         */

        private String url;
        private String name;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
