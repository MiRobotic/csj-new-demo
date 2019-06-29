package com.csjbot.mobileshop.model.http.bean.req;

import java.util.List;

/**
 * Created by Administrator on 2018/12/17.
 */

public class VipInfoReq {


    /**
     * sn : 机器人SN
     * name : 美美
     * sex : 1
     * birthday : 2018-12-11
     * company : 宇宙集团
     * telephone : 17785669566
     * greetWords : 你好美美
     * audioUrl : www.baidu.com
     * audioName : 音频文件的名称
     * userStatus : 1
     * robotReguserFaces : [{"url":"wlwlwllw","name":"文件名"}]
     */

    private String sn;
    private String name;
    private int sex;
    private String birthday;
    private String company;
    private String telephone;
    private String greetWords;
    private String audioUrl;
    private String audioName;
    private String userStatus;
    private String faceId;

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    private List<RobotReguserFacesBean> robotReguserFaces;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGreetWords() {
        return greetWords;
    }

    public void setGreetWords(String greetWords) {
        this.greetWords = greetWords;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public List<RobotReguserFacesBean> getRobotReguserFaces() {
        return robotReguserFaces;
    }

    public void setRobotReguserFaces(List<RobotReguserFacesBean> robotReguserFaces) {
        this.robotReguserFaces = robotReguserFaces;
    }

    public static class RobotReguserFacesBean {
        /**
         * url : wlwlwllw
         * name : 文件名
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
