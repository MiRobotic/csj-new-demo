package com.csjbot.mobileshop.model.http.bean.rsp;

import java.util.List;

/**
 * Created by Administrator on 2018/12/20.
 */

public class VipGreetBean {

    /**
     * id : 13
     * createById : 2
     * createByDate : 2019-01-22 18:43:55
     * modifyById : null
     * modifyByDate : 2019-01-22 18:43:55
     * deleted : 0
     * level : null
     * language : zh
     * name : 暂住证
     * sex : 0
     * userStatus : 1
     * birthday : null
     * company : null
     * telephone :
     * greetWords : null
     * audioUrl : null
     * audioName : null
     * userId : null
     * robotReguserFaces : [{"id":24,"createById":2,"createByDate":"2019-01-22 18:43:55","modifyById":null,"modifyByDate":"2019-01-22 18:43:55","deleted":0,"level":"1,2,","language":"zh","reguserId":13,"faceId":null,"url":"http://csjbot-test.su.bcebos.com/1548153836464_080019040003.png","name":"1548153836464_080019040003.png"}]
     * sn : 080019040003
     * faceId : null
     * ids : null
     * robotReguserState : false
     * robotReguserStateEntity : null
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
    private int sex;
    private String userStatus;
    private String birthday;
    private String company;
    private String telephone;
    private String greetWords;
    private String audioUrl;
    private String audioName;
    private int userId;
    private String sn;
    private String faceId;
    private Object ids;
    private boolean robotReguserState;
    private Object robotReguserStateEntity;
    private List<RobotReguserFacesBean> robotReguserFaces;

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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGreetWords() {
        return greetWords;
    }

    public void setGreetWords(String greetWords) {
        this.greetWords = greetWords;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public Object getIds() {
        return ids;
    }

    public void setIds(Object ids) {
        this.ids = ids;
    }

    public boolean isRobotReguserState() {
        return robotReguserState;
    }

    public void setRobotReguserState(boolean robotReguserState) {
        this.robotReguserState = robotReguserState;
    }

    public Object getRobotReguserStateEntity() {
        return robotReguserStateEntity;
    }

    public void setRobotReguserStateEntity(Object robotReguserStateEntity) {
        this.robotReguserStateEntity = robotReguserStateEntity;
    }

    public List<RobotReguserFacesBean> getRobotReguserFaces() {
        return robotReguserFaces;
    }

    public void setRobotReguserFaces(List<RobotReguserFacesBean> robotReguserFaces) {
        this.robotReguserFaces = robotReguserFaces;
    }

    public static class RobotReguserFacesBean {
        /**
         * id : 24
         * createById : 2
         * createByDate : 2019-01-22 18:43:55
         * modifyById : null
         * modifyByDate : 2019-01-22 18:43:55
         * deleted : 0
         * level : 1,2,
         * language : zh
         * reguserId : 13
         * faceId : null
         * url : http://csjbot-test.su.bcebos.com/1548153836464_080019040003.png
         * name : 1548153836464_080019040003.png
         */

        private int id;
        private int createById;
        private String createByDate;
        private Object modifyById;
        private String modifyByDate;
        private int deleted;
        private String level;
        private String language;
        private int reguserId;
        private String faceId;
        private String url;
        private String name;

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

        public int getReguserId() {
            return reguserId;
        }

        public void setReguserId(int reguserId) {
            this.reguserId = reguserId;
        }

        public String getFaceId() {
            return faceId;
        }

        public void setFaceId(String faceId) {
            this.faceId = faceId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
