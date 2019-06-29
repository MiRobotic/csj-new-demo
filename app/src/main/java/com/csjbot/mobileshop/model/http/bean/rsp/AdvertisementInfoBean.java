package com.csjbot.mobileshop.model.http.bean.rsp;

import java.util.List;

/**
 * @author ShenBen
 * @date 2018/12/19 16:07
 * @email 714081644@qq.com
 */

public class AdvertisementInfoBean {

    /**
     * standbyAdv : {"id":15,"createById":166,"createByDate":"2018-11-22 09:19:51","modifyById":null,"modifyByDate":"2018-11-22 09:19:51","deleted":0,"level":"166,","language":"zh","advName":"测试","advType":"1","remarks":"备注","account":null,"robotResourceEntities":[{"id":11,"resTypeName":"RobotAdvInfoEntity","resTypeId":30,"name":"测试的","fileUploadName":"111213221","fileType":"图片","url":"www.Alibaba.com"}]}
     * functionalAdv : {"id":30,"advName":"京东de广告","advType":"4","remarks":"","account":null,"robotResourceEntities":[{"id":11,"resTypeName":"RobotAdvInfoEntity","resTypeId":30,"name":"测试的","fileUploadName":"111213221","fileType":"图片","url":"www.Alibaba.com"}]}
     */

    private StandbyAdvBean standbyAdv;
    private FunctionalAdvBean functionalAdv;

    public StandbyAdvBean getStandbyAdv() {
        return standbyAdv;
    }

    public void setStandbyAdv(StandbyAdvBean standbyAdv) {
        this.standbyAdv = standbyAdv;
    }

    public FunctionalAdvBean getFunctionalAdv() {
        return functionalAdv;
    }

    public void setFunctionalAdv(FunctionalAdvBean functionalAdv) {
        this.functionalAdv = functionalAdv;
    }

    public static class StandbyAdvBean {
        /**
         * id : 15
         * createById : 166
         * createByDate : 2018-11-22 09:19:51
         * modifyById : null
         * modifyByDate : 2018-11-22 09:19:51
         * deleted : 0
         * level : 166,
         * language : zh
         * advName : 测试
         * advType : 1
         * remarks : 备注
         * account : null
         * robotResourceEntities : [{"id":11,"resTypeName":"RobotAdvInfoEntity","resTypeId":30,"name":"测试的","fileUploadName":"111213221","fileType":"图片","url":"www.Alibaba.com"}]
         */

        private int id;
        private int createById;
        private String createByDate;
        private Object modifyById;
        private String modifyByDate;
        private int deleted;
        private String level;
        private String language;
        private String advName;
        private String advType;
        private String remarks;
        private Object account;
        private List<RobotResourceEntitiesBean> robotResourceEntities;

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

        public String getAdvName() {
            return advName;
        }

        public void setAdvName(String advName) {
            this.advName = advName;
        }

        public String getAdvType() {
            return advType;
        }

        public void setAdvType(String advType) {
            this.advType = advType;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public Object getAccount() {
            return account;
        }

        public void setAccount(Object account) {
            this.account = account;
        }

        public List<RobotResourceEntitiesBean> getRobotResourceEntities() {
            return robotResourceEntities;
        }

        public void setRobotResourceEntities(List<RobotResourceEntitiesBean> robotResourceEntities) {
            this.robotResourceEntities = robotResourceEntities;
        }

        public static class RobotResourceEntitiesBean {
            /**
             * id : 11
             * resTypeName : RobotAdvInfoEntity
             * resTypeId : 30
             * name : 测试的
             * fileUploadName : 111213221
             * fileType : 图片
             * url : www.Alibaba.com
             */

            private int id;
            private String resTypeName;
            private int resTypeId;
            private String name;
            private String fileUploadName;
            private String fileType;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getResTypeName() {
                return resTypeName;
            }

            public void setResTypeName(String resTypeName) {
                this.resTypeName = resTypeName;
            }

            public int getResTypeId() {
                return resTypeId;
            }

            public void setResTypeId(int resTypeId) {
                this.resTypeId = resTypeId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFileUploadName() {
                return fileUploadName;
            }

            public void setFileUploadName(String fileUploadName) {
                this.fileUploadName = fileUploadName;
            }

            public String getFileType() {
                return fileType;
            }

            public void setFileType(String fileType) {
                this.fileType = fileType;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    public static class FunctionalAdvBean {
        /**
         * id : 30
         * advName : 京东de广告
         * advType : 4
         * remarks :
         * account : null
         * robotResourceEntities : [{"id":11,"resTypeName":"RobotAdvInfoEntity","resTypeId":30,"name":"测试的","fileUploadName":"111213221","fileType":"图片","url":"www.Alibaba.com"}]
         */

        private int id;
        private String advName;
        private String advType;
        private String remarks;
        private Object account;
        private List<RobotResourceEntitiesBeanX> robotResourceEntities;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAdvName() {
            return advName;
        }

        public void setAdvName(String advName) {
            this.advName = advName;
        }

        public String getAdvType() {
            return advType;
        }

        public void setAdvType(String advType) {
            this.advType = advType;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public Object getAccount() {
            return account;
        }

        public void setAccount(Object account) {
            this.account = account;
        }

        public List<RobotResourceEntitiesBeanX> getRobotResourceEntities() {
            return robotResourceEntities;
        }

        public void setRobotResourceEntities(List<RobotResourceEntitiesBeanX> robotResourceEntities) {
            this.robotResourceEntities = robotResourceEntities;
        }

        public static class RobotResourceEntitiesBeanX {
            /**
             * id : 11
             * resTypeName : RobotAdvInfoEntity
             * resTypeId : 30
             * name : 测试的
             * fileUploadName : 111213221
             * fileType : 图片
             * url : www.Alibaba.com
             */

            private int id;
            private String resTypeName;
            private int resTypeId;
            private String name;
            private String fileUploadName;
            private String fileType;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getResTypeName() {
                return resTypeName;
            }

            public void setResTypeName(String resTypeName) {
                this.resTypeName = resTypeName;
            }

            public int getResTypeId() {
                return resTypeId;
            }

            public void setResTypeId(int resTypeId) {
                this.resTypeId = resTypeId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFileUploadName() {
                return fileUploadName;
            }

            public void setFileUploadName(String fileUploadName) {
                this.fileUploadName = fileUploadName;
            }

            public String getFileType() {
                return fileType;
            }

            public void setFileType(String fileType) {
                this.fileType = fileType;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
