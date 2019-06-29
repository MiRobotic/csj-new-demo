package com.csjbot.mobileshop.model.http.bean.rsp;

/**
 * Created by 孙秀艳 on 2018/12/18.
 */

public class UpdateAPKBean {

    /**
     * id : 7
     * createById : 209
     * createByDate : 2018-12-28 14:35:44
     * modifyById : null
     * modifyByDate : 2018-12-28 14:35:54
     * deleted : 0
     * level : 1,209,
     * language : zh
     * fileUrl : http://csjbot-test.su.bcebos.com/3ctKfBidEF2jpW8MGYFhkEzcw34JdpxPJ8hmxGDK.apk
     * robotType : 35
     * channel : 1
     * versionCode : 6
     * versionName : TE_V1.0.0.0006[mobile_shop]
     * md5 : null
     * platform : 1
     * memo : null
     * fileAttribute : [{"url":"http://csjbot-test.su.bcebos.com/3ctKfBidEF2jpW8MGYFhkEzcw34JdpxPJ8hmxGDK.apk","name":"TE_V1.0.0.0006[mobile_shop].apk","type":"","uid":1545978940076,"status":"success"}]
     * robotTypeName : 售货车
     */

    private int id;
    private int createById;
    private String createByDate;
    private Object modifyById;
    private String modifyByDate;
    private int deleted;
    private String level;
    private String language;
    private String fileUrl;
    private int robotType;
    private int channel;
    private int versionCode;
    private String versionName;
    private Object md5;
    private int platform;
    private Object memo;
    private String fileAttribute;
    private String robotTypeName;

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

    public String getModifyByDate() {
        return modifyByDate;
    }

    public void setModifyByDate(String modifyByDate) {
        this.modifyByDate = modifyByDate;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getRobotType() {
        return robotType;
    }

    public void setRobotType(int robotType) {
        this.robotType = robotType;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Object getMd5() {
        return md5;
    }

    public void setMd5(Object md5) {
        this.md5 = md5;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public Object getMemo() {
        return memo;
    }

    public void setMemo(Object memo) {
        this.memo = memo;
    }

    public String getFileAttribute() {
        return fileAttribute;
    }

    public void setFileAttribute(String fileAttribute) {
        this.fileAttribute = fileAttribute;
    }

    public String getRobotTypeName() {
        return robotTypeName;
    }

    public void setRobotTypeName(String robotTypeName) {
        this.robotTypeName = robotTypeName;
    }

    @Override
    public String toString() {
        return "UpdateAPKBean{" +
                "id=" + id +
                ", createById=" + createById +
                ", createByDate='" + createByDate + '\'' +
                ", modifyById=" + modifyById +
                ", modifyByDate='" + modifyByDate + '\'' +
                ", deleted=" + deleted +
                ", level='" + level + '\'' +
                ", language='" + language + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", robotType=" + robotType +
                ", channel=" + channel +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", md5=" + md5 +
                ", platform=" + platform +
                ", memo=" + memo +
                ", fileAttribute='" + fileAttribute + '\'' +
                ", robotTypeName='" + robotTypeName + '\'' +
                '}';
    }
}
