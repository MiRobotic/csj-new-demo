package com.csjbot.mobileshop.widget.chat_view.bean;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShenBen
 * @date 2019/2/25 9:20
 * @email 714081644@qq.com
 */
public class ChatBean {
    /**
     * 显示的文字(显示的文字是必备的)
     */
    private CharSequence message;
    /**
     * 显示的图片
     */
    private List<PictureBean> pictures = new ArrayList<>();

    public ChatBean() {
    }

    public ChatBean(@NonNull CharSequence message) {
        this.message = message;
    }

    public ChatBean(@NonNull CharSequence message, @NonNull List<PictureBean> pictures) {
        this.message = message;
        this.pictures.clear();
        this.pictures.addAll(pictures);
    }

    public CharSequence getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PictureBean> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureBean> pictures) {
        this.pictures = pictures;
    }

    public static class PictureBean {
        private String pictureName = "";
        private String pictureUrl = "";
        private String picturePath = "";

        public String getPictureName() {
            return pictureName;
        }

        public void setPictureName(String pictureName) {
            this.pictureName = pictureName;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public String getPicturePath() {
            return picturePath;
        }

        public void setPicturePath(String picturePath) {
            this.picturePath = picturePath;
        }
    }
}
