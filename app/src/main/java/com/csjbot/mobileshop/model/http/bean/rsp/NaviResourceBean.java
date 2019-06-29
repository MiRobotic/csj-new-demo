package com.csjbot.mobileshop.model.http.bean.rsp;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/12/21.
 */

public class NaviResourceBean implements Serializable {


    /**
     * id : 204
     * fileType : 2
     * fileName : 11
     * fileUrl : http://csjbot-test.su.bcebos.com/XGKjCix3Jhcais8rbBBGfHhzbeR7X6mN2PPEr5Kw.jpg
     */

    private int id;
    private String fileType;
    private String fileName;
    private String fileUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
