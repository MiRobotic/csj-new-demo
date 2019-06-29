package com.csjbot.mobileshop.model.http.bean.rsp;

import java.util.List;

/**
 * 悬浮和购物指南二维码
 *
 * @author ShenBen
 * @date 2019/1/21 9:47
 * @email 714081644@qq.com
 */

public class QrCodeBean {

    /**
     * id : 24
     * createById : 2
     * createByDate : 2019-01-22 14:49:11
     * modifyById : null
     * modifyByDate : 2019-01-22 14:49:11
     * deleted : 0
     * level : 1,2,
     * language : zh
     * qrName : 支付宝
     * qrCodeUsage : 2
     * qrCodeKey : null
     * qrStoreFile : [{"status":"success","name":"支付宝.png","size":1038,"percentage":100,"uid":1548139742484,"raw":{"uid":1548139742484,"OSSDataKey":"7SeSwJByGHn6sYQiYkrsaxkJ8X2MxAGWScjWWFF6.png"},"url":"http://csjbot-test.su.bcebos.com/7SeSwJByGHn6sYQiYkrsaxkJ8X2MxAGWScjWWFF6.png","response":"","type":"2"}]
     * qrWords : 支付宝缴费话术
     * qrStoreIcon : [{"status":"success","name":"zfb.png","size":14271,"percentage":100,"uid":1548139738924,"raw":{"uid":1548139738924,"OSSDataKey":"GJHJYQSNReefbA7fid7MCe8sWtGxwEKpNRmBCRsp.png"},"url":"http://csjbot-test.su.bcebos.com/GJHJYQSNReefbA7fid7MCe8sWtGxwEKpNRmBCRsp.png","response":"","type":"2"}]
     * robotInfoEntityList : null
     * qrFile : [{"status":"success","name":"支付宝.png","size":1038,"percentage":100,"uid":1548139742484,"raw":{"uid":1548139742484,"OSSDataKey":"7SeSwJByGHn6sYQiYkrsaxkJ8X2MxAGWScjWWFF6.png"},"url":"http://csjbot-test.su.bcebos.com/7SeSwJByGHn6sYQiYkrsaxkJ8X2MxAGWScjWWFF6.png","response":"","type":"2"}]
     * icon : [{"status":"success","name":"zfb.png","size":14271,"percentage":100,"uid":1548139738924,"raw":{"uid":1548139738924,"OSSDataKey":"GJHJYQSNReefbA7fid7MCe8sWtGxwEKpNRmBCRsp.png"},"url":"http://csjbot-test.su.bcebos.com/GJHJYQSNReefbA7fid7MCe8sWtGxwEKpNRmBCRsp.png","response":"","type":"2"}]
     */

    private int id;
    private int createById;
    private String createByDate;
    private Object modifyById;
    private String modifyByDate;
    private int deleted;
    private String level;
    private String language;
    private String qrName;
    private String qrCodeUsage;
    private String qrCodeKey;
    private String qrStoreFile;
    private String qrWords;
    private String qrStoreIcon;
    private Object robotInfoEntityList;
    private List<QrFileBean> qrFile;
    private List<IconBean> icon;

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

    public String getQrName() {
        return qrName;
    }

    public void setQrName(String qrName) {
        this.qrName = qrName;
    }

    public String getQrCodeUsage() {
        return qrCodeUsage;
    }

    public void setQrCodeUsage(String qrCodeUsage) {
        this.qrCodeUsage = qrCodeUsage;
    }

    public String getQrCodeKey() {
        return qrCodeKey;
    }

    public void setQrCodeKey(String qrCodeKey) {
        this.qrCodeKey = qrCodeKey;
    }

    public String getQrStoreFile() {
        return qrStoreFile;
    }

    public void setQrStoreFile(String qrStoreFile) {
        this.qrStoreFile = qrStoreFile;
    }

    public String getQrWords() {
        return qrWords;
    }

    public void setQrWords(String qrWords) {
        this.qrWords = qrWords;
    }

    public String getQrStoreIcon() {
        return qrStoreIcon;
    }

    public void setQrStoreIcon(String qrStoreIcon) {
        this.qrStoreIcon = qrStoreIcon;
    }

    public Object getRobotInfoEntityList() {
        return robotInfoEntityList;
    }

    public void setRobotInfoEntityList(Object robotInfoEntityList) {
        this.robotInfoEntityList = robotInfoEntityList;
    }

    public List<QrFileBean> getQrFile() {
        return qrFile;
    }

    public void setQrFile(List<QrFileBean> qrFile) {
        this.qrFile = qrFile;
    }

    public List<IconBean> getIcon() {
        return icon;
    }

    public void setIcon(List<IconBean> icon) {
        this.icon = icon;
    }

    public static class QrFileBean {
        /**
         * status : success
         * name : 支付宝.png
         * size : 1038
         * percentage : 100
         * uid : 1548139742484
         * raw : {"uid":1548139742484,"OSSDataKey":"7SeSwJByGHn6sYQiYkrsaxkJ8X2MxAGWScjWWFF6.png"}
         * url : http://csjbot-test.su.bcebos.com/7SeSwJByGHn6sYQiYkrsaxkJ8X2MxAGWScjWWFF6.png
         * response :
         * type : 2
         */

        private String status;
        private String name;
        private int size;
        private int percentage;
        private long uid;
        private RawBean raw;
        private String url;
        private String response;
        private String type;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public RawBean getRaw() {
            return raw;
        }

        public void setRaw(RawBean raw) {
            this.raw = raw;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class RawBean {
            /**
             * uid : 1548139742484
             * OSSDataKey : 7SeSwJByGHn6sYQiYkrsaxkJ8X2MxAGWScjWWFF6.png
             */

            private long uid;
            private String OSSDataKey;

            public long getUid() {
                return uid;
            }

            public void setUid(long uid) {
                this.uid = uid;
            }

            public String getOSSDataKey() {
                return OSSDataKey;
            }

            public void setOSSDataKey(String OSSDataKey) {
                this.OSSDataKey = OSSDataKey;
            }
        }
    }

    public static class IconBean {
        /**
         * status : success
         * name : zfb.png
         * size : 14271
         * percentage : 100
         * uid : 1548139738924
         * raw : {"uid":1548139738924,"OSSDataKey":"GJHJYQSNReefbA7fid7MCe8sWtGxwEKpNRmBCRsp.png"}
         * url : http://csjbot-test.su.bcebos.com/GJHJYQSNReefbA7fid7MCe8sWtGxwEKpNRmBCRsp.png
         * response :
         * type : 2
         */

        private String status;
        private String name;
        private int size;
        private int percentage;
        private long uid;
        private RawBeanX raw;
        private String url;
        private String response;
        private String type;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public RawBeanX getRaw() {
            return raw;
        }

        public void setRaw(RawBeanX raw) {
            this.raw = raw;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class RawBeanX {
            /**
             * uid : 1548139738924
             * OSSDataKey : GJHJYQSNReefbA7fid7MCe8sWtGxwEKpNRmBCRsp.png
             */

            private long uid;
            private String OSSDataKey;

            public long getUid() {
                return uid;
            }

            public void setUid(long uid) {
                this.uid = uid;
            }

            public String getOSSDataKey() {
                return OSSDataKey;
            }

            public void setOSSDataKey(String OSSDataKey) {
                this.OSSDataKey = OSSDataKey;
            }
        }
    }
}
