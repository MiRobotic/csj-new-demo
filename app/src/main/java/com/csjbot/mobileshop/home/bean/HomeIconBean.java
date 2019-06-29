package com.csjbot.mobileshop.home.bean;

import android.support.annotation.NonNull;

/**
 * @author ShenBen
 * @date 2018/11/12 16:41
 * @email 714081644@qq.com
 */

public class HomeIconBean implements Comparable<HomeIconBean> {
    /**
     * 排序
     */
    private int sort;
    /**
     * 原始Title
     */
    private String title;
    /**
     * 替换的title
     */
    private String replaceTitle;

    private int moduleId;
    /**
     * 本地图片
     */
    private int icon;

    private String imageUrl;

    public HomeIconBean() {
    }

    public HomeIconBean(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public HomeIconBean(String title, int icon, int moduleId) {
        this.title = title;
        this.icon = icon;
        this.moduleId = moduleId;
    }

    public HomeIconBean(String title, int moduleId, int icon, int sort) {
        this.title = title;
        this.moduleId = moduleId;
        this.icon = icon;
        this.sort = sort;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReplaceTitle() {
        return replaceTitle;
    }

    public void setReplaceTitle(String replaceTitle) {
        this.replaceTitle = replaceTitle;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "HomeIconBean{" +
                "title='" + title + '\'' +
                ", replaceTitle='" + replaceTitle + '\'' +
                ", moduleId='" + moduleId + '\'' +
                ", icon=" + icon +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull HomeIconBean o) {
        return getSort() - o.getSort();
    }
}
