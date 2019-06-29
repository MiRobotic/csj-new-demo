package com.csjbot.mobileshop.model.http.bean.rsp;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/12/21.
 */

public class ContentInfoBean implements Serializable, Comparable<ContentInfoBean> {
    private static final long serialVersionUID = -3010733335442844463L;
    /**
     * id : 356
     * createById : 199
     * createByDate : 2018-12-28 15:03:58
     * modifyById : null
     * modifyByDate : 2018-12-29 13:47:21
     * deleted : 0
     * level : 1,199,
     * language : zh
     * name : 556s56s4a4s
     * parentId : 0
     * isHomeShow : 0
     * details : 5666
     * words : 33
     * sequence : 2
     * treeLevel : 0,
     * contentIcon : http://csjbot-test.su.bcebos.com/mx4PaerksnAZJfXBsEQEyHsExx66TH7sSEtQNB8F.jpg
     * children : [{"id":357,"createById":1,"createByDate":"2018-12-29 15:28:46","modifyById":null,"modifyByDate":"2018-12-29 16:43:11","deleted":0,"level":"1,","language":"zh","name":"1213","parentId":356,"isHomeShow":0,"details":"121212","words":"1212","sequence":1,"treeLevel":"0,356,","contentIcon":null,"children":[{"id":358,"createById":1,"createByDate":"2018-12-29 16:42:56","modifyById":null,"modifyByDate":"2018-12-29 16:43:11","deleted":0,"level":"1,","language":"zh","name":"1212","parentId":357,"isHomeShow":0,"details":"1212","words":"1212","sequence":1,"treeLevel":"0,356,357,","contentIcon":null,"children":[],"robotInfos":[],"childCount":0},{"id":359,"createById":1,"createByDate":"2019-01-02 13:24:22","modifyById":null,"modifyByDate":"2019-01-02 13:24:22","deleted":0,"level":"1,","language":"zh","name":"122222222222222222222222222222222222222222222222222222222255555555555555","parentId":357,"isHomeShow":1,"details":"","words":"","sequence":1,"treeLevel":"0,356,357,","contentIcon":null,"children":[],"robotInfos":[],"childCount":0}],"robotInfos":[],"childCount":2}]
     * robotInfos : [{"id":135,"createById":1,"createByDate":"2018-12-20 17:07:20","modifyById":1,"modifyByDate":"2018-12-28 11:47:12","deleted":0,"level":"1,","language":"zh","sn":"010018510001","enterOrderCode":null,"outOrderCode":"5556dd44d54d54d","checkState":1,"robotTypeId":22,"robotName":"售货机器人","state":"1","sceneId":null,"knowledgeBaseId":null,"isBound":1,"boundUserId":199,"greetType":null,"greetContent":null,"greetName":null,"logoUrl":null,"logoName":null,"standbyAdvId":null,"functionalAdvId":null,"expressionId":null,"standbyAdv":null,"functionalAdv":null,"robotEnterOrderEntity":null,"robotKnowledgeBase":null,"robotTypeEntity":null,"createUser":null,"robotSceneInfo":null,"robotByModules":null,"robotExpression":null,"mobile":null,"moduleNames":null,"greetContentModel":null,"robotState":null}]
     * childCount : 3
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
    private int parentId;
    private int isHomeShow;
    private String details;
    private String words;
    private int sequence;
    private String treeLevel;
    private String contentIcon;
    private int childCount;
    private List<ChildrenBeanX> children;
    private List<RobotInfosBean> robotInfos;

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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isHomeShow() {
        return isHomeShow == 1;
    }

    public void setIsHomeShow(int isHomeShow) {
        this.isHomeShow = isHomeShow;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(String treeLevel) {
        this.treeLevel = treeLevel;
    }

    public String getContentIcon() {
        return contentIcon;
    }

    public void setContentIcon(String contentIcon) {
        this.contentIcon = contentIcon;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public List<ChildrenBeanX> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBeanX> children) {
        this.children = children;
    }

    public List<RobotInfosBean> getRobotInfos() {
        return robotInfos;
    }

    public void setRobotInfos(List<RobotInfosBean> robotInfos) {
        this.robotInfos = robotInfos;
    }

    @Override
    public int compareTo(@NonNull ContentInfoBean o) {
        return getSequence() - o.getSequence();
    }

    public static class ChildrenBeanX implements Serializable, Comparable<ChildrenBeanX> {
        private static final long serialVersionUID = -99169350735935942L;
        /**
         * id : 357
         * createById : 1
         * createByDate : 2018-12-29 15:28:46
         * modifyById : null
         * modifyByDate : 2018-12-29 16:43:11
         * deleted : 0
         * level : 1,
         * language : zh
         * name : 1213
         * parentId : 356
         * isHomeShow : 0
         * details : 121212
         * words : 1212
         * sequence : 1
         * treeLevel : 0,356,
         * contentIcon : null
         * children : [{"id":358,"createById":1,"createByDate":"2018-12-29 16:42:56","modifyById":null,"modifyByDate":"2018-12-29 16:43:11","deleted":0,"level":"1,","language":"zh","name":"1212","parentId":357,"isHomeShow":0,"details":"1212","words":"1212","sequence":1,"treeLevel":"0,356,357,","contentIcon":null,"children":[],"robotInfos":[],"childCount":0},{"id":359,"createById":1,"createByDate":"2019-01-02 13:24:22","modifyById":null,"modifyByDate":"2019-01-02 13:24:22","deleted":0,"level":"1,","language":"zh","name":"122222222222222222222222222222222222222222222222222222222255555555555555","parentId":357,"isHomeShow":1,"details":"","words":"","sequence":1,"treeLevel":"0,356,357,","contentIcon":null,"children":[],"robotInfos":[],"childCount":0}]
         * robotInfos : []
         * childCount : 2
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
        private int parentId;
        private int isHomeShow;
        private String details;
        private String words;
        private int sequence;
        private String treeLevel;
        private Object contentIcon;
        private int childCount;
        private List<ChildrenBean> children;
        private List<?> robotInfos;

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

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public boolean isHomeShow() {
            return isHomeShow == 1;
        }

        public void setIsHomeShow(int isHomeShow) {
            this.isHomeShow = isHomeShow;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public String getTreeLevel() {
            return treeLevel;
        }

        public void setTreeLevel(String treeLevel) {
            this.treeLevel = treeLevel;
        }

        public Object getContentIcon() {
            return contentIcon;
        }

        public void setContentIcon(Object contentIcon) {
            this.contentIcon = contentIcon;
        }

        public int getChildCount() {
            return childCount;
        }

        public void setChildCount(int childCount) {
            this.childCount = childCount;
        }

        public List<ChildrenBean> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBean> children) {
            this.children = children;
        }

        public List<?> getRobotInfos() {
            return robotInfos;
        }

        public void setRobotInfos(List<?> robotInfos) {
            this.robotInfos = robotInfos;
        }

        @Override
        public int compareTo(@NonNull ChildrenBeanX o) {
            return getSequence() - o.getSequence();
        }

        public static class ChildrenBean implements Serializable, Comparable<ChildrenBean> {
            private static final long serialVersionUID = 5040761296040619250L;
            /**
             * id : 358
             * createById : 1
             * createByDate : 2018-12-29 16:42:56
             * modifyById : null
             * modifyByDate : 2018-12-29 16:43:11
             * deleted : 0
             * level : 1,
             * language : zh
             * name : 1212
             * parentId : 357
             * isHomeShow : 0
             * details : 1212
             * words : 1212
             * sequence : 1
             * treeLevel : 0,356,357,
             * contentIcon : null
             * children : []
             * robotInfos : []
             * childCount : 0
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
            private int parentId;
            private int isHomeShow;
            private String details;
            private String words;
            private int sequence;
            private String treeLevel;
            private Object contentIcon;
            private int childCount;
            private List<?> children;
            private List<?> robotInfos;

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

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public boolean isHomeShow() {
                return isHomeShow == 1;
            }

            public void setIsHomeShow(int isHomeShow) {
                this.isHomeShow = isHomeShow;
            }

            public String getDetails() {
                return details;
            }

            public void setDetails(String details) {
                this.details = details;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public int getSequence() {
                return sequence;
            }

            public void setSequence(int sequence) {
                this.sequence = sequence;
            }

            public String getTreeLevel() {
                return treeLevel;
            }

            public void setTreeLevel(String treeLevel) {
                this.treeLevel = treeLevel;
            }

            public Object getContentIcon() {
                return contentIcon;
            }

            public void setContentIcon(Object contentIcon) {
                this.contentIcon = contentIcon;
            }

            public int getChildCount() {
                return childCount;
            }

            public void setChildCount(int childCount) {
                this.childCount = childCount;
            }

            public List<?> getChildren() {
                return children;
            }

            public void setChildren(List<?> children) {
                this.children = children;
            }

            public List<?> getRobotInfos() {
                return robotInfos;
            }

            public void setRobotInfos(List<?> robotInfos) {
                this.robotInfos = robotInfos;
            }

            @Override
            public int compareTo(@NonNull ChildrenBean o) {
                return getSequence() - o.getSequence();
            }
        }
    }

    public static class RobotInfosBean implements Serializable{
        private static final long serialVersionUID = 354021314684392891L;
        /**
         * id : 135
         * createById : 1
         * createByDate : 2018-12-20 17:07:20
         * modifyById : 1
         * modifyByDate : 2018-12-28 11:47:12
         * deleted : 0
         * level : 1,
         * language : zh
         * sn : 010018510001
         * enterOrderCode : null
         * outOrderCode : 5556dd44d54d54d
         * checkState : 1
         * robotTypeId : 22
         * robotName : 售货机器人
         * state : 1
         * sceneId : null
         * knowledgeBaseId : null
         * isBound : 1
         * boundUserId : 199
         * greetType : null
         * greetContent : null
         * greetName : null
         * logoUrl : null
         * logoName : null
         * standbyAdvId : null
         * functionalAdvId : null
         * expressionId : null
         * standbyAdv : null
         * functionalAdv : null
         * robotEnterOrderEntity : null
         * robotKnowledgeBase : null
         * robotTypeEntity : null
         * createUser : null
         * robotSceneInfo : null
         * robotByModules : null
         * robotExpression : null
         * mobile : null
         * moduleNames : null
         * greetContentModel : null
         * robotState : null
         */

        private int id;
        private int createById;
        private String createByDate;
        private int modifyById;
        private String modifyByDate;
        private int deleted;
        private String level;
        private String language;
        private String sn;
        private Object enterOrderCode;
        private String outOrderCode;
        private int checkState;
        private int robotTypeId;
        private String robotName;
        private String state;
        private Object sceneId;
        private Object knowledgeBaseId;
        private int isBound;
        private int boundUserId;
        private Object greetType;
        private Object greetContent;
        private Object greetName;
        private Object logoUrl;
        private Object logoName;
        private Object standbyAdvId;
        private Object functionalAdvId;
        private Object expressionId;
        private Object standbyAdv;
        private Object functionalAdv;
        private Object robotEnterOrderEntity;
        private Object robotKnowledgeBase;
        private Object robotTypeEntity;
        private Object createUser;
        private Object robotSceneInfo;
        private Object robotByModules;
        private Object robotExpression;
        private Object mobile;
        private Object moduleNames;
        private Object greetContentModel;
        private Object robotState;

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

        public int getModifyById() {
            return modifyById;
        }

        public void setModifyById(int modifyById) {
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

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public Object getEnterOrderCode() {
            return enterOrderCode;
        }

        public void setEnterOrderCode(Object enterOrderCode) {
            this.enterOrderCode = enterOrderCode;
        }

        public String getOutOrderCode() {
            return outOrderCode;
        }

        public void setOutOrderCode(String outOrderCode) {
            this.outOrderCode = outOrderCode;
        }

        public int getCheckState() {
            return checkState;
        }

        public void setCheckState(int checkState) {
            this.checkState = checkState;
        }

        public int getRobotTypeId() {
            return robotTypeId;
        }

        public void setRobotTypeId(int robotTypeId) {
            this.robotTypeId = robotTypeId;
        }

        public String getRobotName() {
            return robotName;
        }

        public void setRobotName(String robotName) {
            this.robotName = robotName;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Object getSceneId() {
            return sceneId;
        }

        public void setSceneId(Object sceneId) {
            this.sceneId = sceneId;
        }

        public Object getKnowledgeBaseId() {
            return knowledgeBaseId;
        }

        public void setKnowledgeBaseId(Object knowledgeBaseId) {
            this.knowledgeBaseId = knowledgeBaseId;
        }

        public int getIsBound() {
            return isBound;
        }

        public void setIsBound(int isBound) {
            this.isBound = isBound;
        }

        public int getBoundUserId() {
            return boundUserId;
        }

        public void setBoundUserId(int boundUserId) {
            this.boundUserId = boundUserId;
        }

        public Object getGreetType() {
            return greetType;
        }

        public void setGreetType(Object greetType) {
            this.greetType = greetType;
        }

        public Object getGreetContent() {
            return greetContent;
        }

        public void setGreetContent(Object greetContent) {
            this.greetContent = greetContent;
        }

        public Object getGreetName() {
            return greetName;
        }

        public void setGreetName(Object greetName) {
            this.greetName = greetName;
        }

        public Object getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(Object logoUrl) {
            this.logoUrl = logoUrl;
        }

        public Object getLogoName() {
            return logoName;
        }

        public void setLogoName(Object logoName) {
            this.logoName = logoName;
        }

        public Object getStandbyAdvId() {
            return standbyAdvId;
        }

        public void setStandbyAdvId(Object standbyAdvId) {
            this.standbyAdvId = standbyAdvId;
        }

        public Object getFunctionalAdvId() {
            return functionalAdvId;
        }

        public void setFunctionalAdvId(Object functionalAdvId) {
            this.functionalAdvId = functionalAdvId;
        }

        public Object getExpressionId() {
            return expressionId;
        }

        public void setExpressionId(Object expressionId) {
            this.expressionId = expressionId;
        }

        public Object getStandbyAdv() {
            return standbyAdv;
        }

        public void setStandbyAdv(Object standbyAdv) {
            this.standbyAdv = standbyAdv;
        }

        public Object getFunctionalAdv() {
            return functionalAdv;
        }

        public void setFunctionalAdv(Object functionalAdv) {
            this.functionalAdv = functionalAdv;
        }

        public Object getRobotEnterOrderEntity() {
            return robotEnterOrderEntity;
        }

        public void setRobotEnterOrderEntity(Object robotEnterOrderEntity) {
            this.robotEnterOrderEntity = robotEnterOrderEntity;
        }

        public Object getRobotKnowledgeBase() {
            return robotKnowledgeBase;
        }

        public void setRobotKnowledgeBase(Object robotKnowledgeBase) {
            this.robotKnowledgeBase = robotKnowledgeBase;
        }

        public Object getRobotTypeEntity() {
            return robotTypeEntity;
        }

        public void setRobotTypeEntity(Object robotTypeEntity) {
            this.robotTypeEntity = robotTypeEntity;
        }

        public Object getCreateUser() {
            return createUser;
        }

        public void setCreateUser(Object createUser) {
            this.createUser = createUser;
        }

        public Object getRobotSceneInfo() {
            return robotSceneInfo;
        }

        public void setRobotSceneInfo(Object robotSceneInfo) {
            this.robotSceneInfo = robotSceneInfo;
        }

        public Object getRobotByModules() {
            return robotByModules;
        }

        public void setRobotByModules(Object robotByModules) {
            this.robotByModules = robotByModules;
        }

        public Object getRobotExpression() {
            return robotExpression;
        }

        public void setRobotExpression(Object robotExpression) {
            this.robotExpression = robotExpression;
        }

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
        }

        public Object getModuleNames() {
            return moduleNames;
        }

        public void setModuleNames(Object moduleNames) {
            this.moduleNames = moduleNames;
        }

        public Object getGreetContentModel() {
            return greetContentModel;
        }

        public void setGreetContentModel(Object greetContentModel) {
            this.greetContentModel = greetContentModel;
        }

        public Object getRobotState() {
            return robotState;
        }

        public void setRobotState(Object robotState) {
            this.robotState = robotState;
        }
    }
}
