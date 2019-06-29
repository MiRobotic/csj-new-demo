package com.csjbot.mobileshop.model.http.bean.rsp;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/12/21.
 */

public class ModuleBean implements Serializable {


    /**
     * id : null
     * robotInfoId : 2
     * moduleId : 1
     * moduleName : 展览讲解
     * replaceName : 展览讲解1
     * enable : 1
     */

    private Object id;
    private int robotInfoId;
    private int moduleId;
    private String moduleName;
    private String replaceName;
    private int enable;//0.不启用；1.启用

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public int getRobotInfoId() {
        return robotInfoId;
    }

    public void setRobotInfoId(int robotInfoId) {
        this.robotInfoId = robotInfoId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getReplaceName() {
        return replaceName;
    }

    public void setReplaceName(String replaceName) {
        this.replaceName = replaceName;
    }

    public boolean isEnable() {
        return enable == 1;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }
}
