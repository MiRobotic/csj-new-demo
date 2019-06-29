package com.csjbot.mobileshop.feature.media.bean;

/**
 * @author ShenBen
 * @date 2019/1/21 16:40
 * @email 714081644@qq.com
 */
public class MediaPublicizeBean {
    private String fileName;
    private String filePath;

    public MediaPublicizeBean() {
    }

    public MediaPublicizeBean(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "MediaPublicizeBean{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
