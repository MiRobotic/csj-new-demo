package com.csjbot.mobileshop.model.http.bean.req;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/12/20.
 */

public class VisitorBean implements Serializable {

    /**
     * sn : 0D0018390000
     * sessionId : 12345
     * imageUrl : www.baidu.com
     * userName : 王五
     * isMember : 0
     */

    private String sn;
    private String sessionId;
    private String imageUrl;
    private String userName;
    private String isMember;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsMember() {
        return isMember;
    }

    public void setIsMember(String isMember) {
        this.isMember = isMember;
    }
}
