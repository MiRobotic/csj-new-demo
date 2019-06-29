package com.csjbot.mobileshop.model.http.bean.rsp;

/**
 * @author ShenBen
 * @date 2019/1/29 13:26
 * @email 714081644@qq.com
 */
public class P2PBean {

    /**
     * isenable : 1
     * password : 123456
     * code : 200
     * serverAddr : p2p.example.com:7781
     * captureid : csj450
     * sn : 080019020001
     */

    private int isenable;
    private String password;
    private int code;
    private String serverAddr;
    private String captureid;
    private String sn;

    public int getIsenable() {
        return isenable;
    }

    public void setIsenable(int isenable) {
        this.isenable = isenable;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getCaptureid() {
        return captureid;
    }

    public void setCaptureid(String captureid) {
        this.captureid = captureid;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
