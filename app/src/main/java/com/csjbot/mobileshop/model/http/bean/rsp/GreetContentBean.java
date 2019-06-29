package com.csjbot.mobileshop.model.http.bean.rsp;

/**
 * Created by Administrator on 2019/1/15.
 */

public class GreetContentBean {

    /**
     * greetName : 欢迎语.mp3
     * greetContent : 齐鲁文化圣地
     * robotName : Alice大屏
     * id : 2
     * sn : 080019020001
     * greetType : 0
     * timeEnable : 0
     * startTime : 01:30
     * endTime : 02:00
     * words : 领导好
     */

    private String greetName;
    private String greetContent;
    private String robotName;
    private int id;
    private String sn;
    private String greetType;
    private String timeEnable;
    private String startTime;
    private String endTime;
    private String words;
    private String timePeriodInfo;

    public String getGreetName() {
        return greetName;
    }

    public void setGreetName(String greetName) {
        this.greetName = greetName;
    }

    public String getGreetContent() {
        return greetContent;
    }

    public void setGreetContent(String greetContent) {
        this.greetContent = greetContent;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getGreetType() {
        return greetType;
    }

    public void setGreetType(String greetType) {
        this.greetType = greetType;
    }

    public String getTimeEnable() {
        return timeEnable;
    }

    public void setTimeEnable(String timeEnable) {
        this.timeEnable = timeEnable;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getTimePeriodInfo() {
        return timePeriodInfo;
    }

    public void setTimePeriodInfo(String timePeriodInfo) {
        this.timePeriodInfo = timePeriodInfo;
    }

    public static class TimePeriodInfo {

        /**
         * startTime : 01:30
         * endTime : 02:00
         * words : 22
         */

        private String startTime;
        private String endTime;
        private String words;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }
}
