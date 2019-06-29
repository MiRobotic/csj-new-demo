package com.csjbot.mobileshop.model.http.bean.rsp;

/**
 * @author ShenBen
 * @date 2019/2/19 18:19
 * @email 714081644@qq.com
 */
public class UnreachablePointBean {

    /**
     * id : 1
     * createById : 154
     * createByDate : 2018-10-19 16:05:13
     * modifyById : null
     * modifyByDate : null
     * deleted : 0
     * fileUrl : www.baidu.com
     * fileName : 你好.jpg
     * location : 大礼堂
     * speech : 不欢迎来到大礼堂
     */

    private int id;
    private int createById;
    private String createByDate;
    private Object modifyById;
    private Object modifyByDate;
    private int deleted;
    private String fileUrl;
    private String fileName;
    private String location;
    private String speech;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreateById() {
        return createById;
    }

    public void setCreateById(int createById) {
        this.createById = createById;
    }

    public String getCreateByDate() {
        return createByDate;
    }

    public void setCreateByDate(String createByDate) {
        this.createByDate = createByDate;
    }

    public Object getModifyById() {
        return modifyById;
    }

    public void setModifyById(Object modifyById) {
        this.modifyById = modifyById;
    }

    public Object getModifyByDate() {
        return modifyByDate;
    }

    public void setModifyByDate(Object modifyByDate) {
        this.modifyByDate = modifyByDate;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }
}
