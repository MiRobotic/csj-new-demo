package com.csjbot.mobileshop.advertisement.event;

/**
 * @author ShenBen
 * @date 2019/1/22 16:56
 * @email 714081644@qq.com
 */
public class FloatQrCodeEvent {
    /**
     * 更新二维码
     */
    public static final int UPDATE_QR_CODE = 1;
    /**
     * 显示隐藏二维码
     */
    public static final int SHOW_HIDE_QR_CODE = 2;
    /**
     * 弹出二维码
     */
    public static final int POP_QR_CODE = 3;
    private int eventType;//行为类型
    private boolean isHide;//是否隐藏
    private String qrName;//要弹出二维码的名字

    public FloatQrCodeEvent(int eventType) {
        this.eventType = eventType;
    }

    public FloatQrCodeEvent(int eventType, boolean isHide) {
        this.eventType = eventType;
        this.isHide = isHide;
    }

    public FloatQrCodeEvent(int eventType, String qrName) {
        this.eventType = eventType;
        this.qrName = qrName;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }

    public String getQrName() {
        return qrName;
    }

    public void setQrName(String qrName) {
        this.qrName = qrName;
    }
}
