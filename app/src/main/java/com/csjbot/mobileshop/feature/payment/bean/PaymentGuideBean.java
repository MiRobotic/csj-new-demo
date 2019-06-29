package com.csjbot.mobileshop.feature.payment.bean;

/**
 * @author ShenBen
 * @date 2019/1/22 11:13
 * @email 714081644@qq.com
 */
public class PaymentGuideBean {
    private String name;
    private String icon;
    private String qrCode;
    private String words;
    private String key;
    private boolean canOverTurn = true;//默认可以翻转
    private boolean isOverTurn;

    public PaymentGuideBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isCanOverTurn() {
        return canOverTurn;
    }

    public void setCanOverTurn(boolean canOverTurn) {
        this.canOverTurn = canOverTurn;
    }

    public boolean isOverTurn() {
        return isOverTurn;
    }

    public void setOverTurn(boolean overTurn) {
        isOverTurn = overTurn;
    }

    @Override
    public String toString() {
        return "PaymentGuideBean{" +
                "name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", words='" + words + '\'' +
                ", key='" + key + '\'' +
                ", canOverTurn=" + canOverTurn +
                ", isOverTurn=" + isOverTurn +
                '}';
    }
}
