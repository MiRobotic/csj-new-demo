package com.csjbot.mobileshop.feature.content.bean;

import android.support.annotation.NonNull;

/**
 * @author ShenBen
 * @date 2018/10/14 16:07
 * @email 714081644@qq.com
 */
public class ChildContentLevelTwoBean implements Comparable<ChildContentLevelTwoBean> {
    private String title;
    private String contentUrl;
    private String speakWords;
    private int order;//调用顺序
    private boolean isChecked;

    public ChildContentLevelTwoBean(String title, String contentUrl, String speakWords, int order) {
        this.title = title;
        this.contentUrl = contentUrl;
        this.speakWords = speakWords;
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getSpeakWords() {
        return speakWords;
    }

    public void setSpeakWords(String speakWords) {
        this.speakWords = speakWords;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int compareTo(@NonNull ChildContentLevelTwoBean o) {
        return getOrder() - o.getOrder();
    }
}
