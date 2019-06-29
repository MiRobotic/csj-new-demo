package com.csjbot.mobileshop.model.http.bean.rsp;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/12/18.
 */

public class LogoBean implements Serializable {

    /**
     * id : 2
     * sn : 0D0018390000
     * robotName : 小清
     * logoUrl : www.baidu.com
     * logoName : logo名称
     */

    private int id;
    private String sn;
    private String robotName;
    private String logoUrl;
    private String logoName;

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

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoName() {
        return logoName;
    }

    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }
}
