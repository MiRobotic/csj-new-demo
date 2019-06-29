package com.csjbot.mobileshop.model.http.bean.rsp;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/12/18.
 */

public class SceneBean implements Serializable {


    /**
     * id : 64
     * createById : 1
     * createByDate : 2019-01-10 15:30:59
     * modifyById : null
     * modifyByDate : 2019-01-10 15:30:59
     * deleted : 0
     * level : 1,
     * language : zh
     * name : 默认场景
     * sceneCode : null
     * typeName : 10
     * typeValue : null
     * seqencing : 1
     * icon : http://csjbot-test.su.bcebos.com/hGbbm3yJrHbfDysDz6Tx7AmZdKQ73EiMwfAXZeht.png
     * skinResource : http://csjbot-test.su.bcebos.com/Pjy8ypXAESHhnXWjBd5cWFweiXHJcHsQFz6wrWCe.skins
     * previewImage : http://csjbot-test.su.bcebos.com/EAH82NwMhTJ6RXFwrTbyYYefNN2s7cRBi7XWG8Ar.jpg
     * remark : 默认场景
     * baseId : null
     * skinName : null
     * sceneByModuleEntities : null
     * boundRobots : null
     */

    private int id;
    private int createById;
    private String createByDate;
    private Object modifyById;
    private String modifyByDate;
    private int deleted;
    private String level;
    private String language;
    private String name;
    private String sceneCode;
    private String typeName;
    private Object typeValue;
    private int seqencing;
    private String icon;
    private String skinResource;
    private String previewImage;
    private String remark;
    private Object baseId;
    private Object skinName;
    private Object sceneByModuleEntities;
    private Object boundRobots;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSceneCode() {
        return sceneCode;
    }

    public void setSceneCode(String sceneCode) {
        this.sceneCode = sceneCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Object getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(Object typeValue) {
        this.typeValue = typeValue;
    }

    public int getSeqencing() {
        return seqencing;
    }

    public void setSeqencing(int seqencing) {
        this.seqencing = seqencing;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSkinResource() {
        return skinResource;
    }

    public void setSkinResource(String skinResource) {
        this.skinResource = skinResource;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Object getBaseId() {
        return baseId;
    }

    public void setBaseId(Object baseId) {
        this.baseId = baseId;
    }

    public Object getSkinName() {
        return skinName;
    }

    public void setSkinName(Object skinName) {
        this.skinName = skinName;
    }

    public Object getSceneByModuleEntities() {
        return sceneByModuleEntities;
    }

    public void setSceneByModuleEntities(Object sceneByModuleEntities) {
        this.sceneByModuleEntities = sceneByModuleEntities;
    }

    public Object getBoundRobots() {
        return boundRobots;
    }

    public void setBoundRobots(Object boundRobots) {
        this.boundRobots = boundRobots;
    }
}
