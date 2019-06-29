package com.csjbot.mobileshop.feature.content.bean;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author ShenBen
 * @date 2018/10/19 17:08
 * @email 714081644@qq.com
 */
public class ChildContentLevelThreeBean implements Comparable<ChildContentLevelThreeBean> {

    private List<ChildrenBean> mList;
    private boolean isChecked = false;//是否选中
    private boolean isCanDrop = false;//是否可以下拉
    private boolean isHadChild = false;//是否有子集
    private boolean isDropping = false;//是否处于下拉状态
    private int order;//排序

    public ChildContentLevelThreeBean(List<ChildrenBean> mList, boolean isCanDrop) {
        this.mList = mList;
        this.isCanDrop = isCanDrop;
    }

    public ChildContentLevelThreeBean(List<ChildrenBean> mList, boolean isCanDrop, boolean isHadChild) {
        this.mList = mList;
        this.isCanDrop = isCanDrop;
        this.isHadChild = isHadChild;
    }

    public List<ChildrenBean> getList() {
        return mList;
    }

    public void setList(List<ChildrenBean> mList) {
        this.mList = mList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isCanDrop() {
        return isCanDrop;
    }

    public void setCanDrop(boolean canDrop) {
        isCanDrop = canDrop;
    }

    public boolean isDropping() {
        return isDropping;
    }

    public void setDropping(boolean dropping) {
        isDropping = dropping;
    }

    public boolean isHadChild() {
        return isHadChild;
    }

    public void setHadChild(boolean hadChild) {
        isHadChild = hadChild;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compareTo(@NonNull ChildContentLevelThreeBean o) {
        return getOrder() - o.getOrder();
    }


    public static class ChildrenBean implements Comparable<ChildrenBean> {

        private String title;
        private String contentUrl;
        private String speakWords;
        private boolean isChecked;
        private int order;

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

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        @Override
        public String toString() {
            return "ChildrenBean{" +
                    "title='" + title + '\'' +
                    ", contentUrl='" + contentUrl + '\'' +
                    ", speakWords='" + speakWords + '\'' +
                    ", isChecked=" + isChecked +
                    '}';
        }

        @Override
        public int compareTo(@NonNull ChildrenBean o) {
            return getOrder() - o.getOrder();
        }

    }

    @Override
    public String toString() {
        return "ChildContentLevelThreeBean{" +
                "mList=" + mList +
                ", isChecked=" + isChecked +
                ", isCanDrop=" + isCanDrop +
                ", isHadChild=" + isHadChild +
                ", isDropping=" + isDropping +
                '}';
    }
}
