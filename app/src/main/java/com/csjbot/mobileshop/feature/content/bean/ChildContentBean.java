package com.csjbot.mobileshop.feature.content.bean;

import java.util.List;

/**
 * @author ShenBen
 * @date 2018/10/19 16:48
 * @email 714081644@qq.com
 */
public class ChildContentBean {


    /**
     * result : {"data":[{"modifyById":null,"resUrl":"http://localhost:1234567890/csjbot-service/api/contentInfo/detail?id=117&language=23&sn=34235","scenesId":28,"isLast":"false","level":null,"words":"1","parentId":116,"sequence":1,"router":null,"deleted":0,"treeLevel":"0,116,","children":[{"modifyById":null,"resUrl":"http://localhost:1234567890/csjbot-service/api/contentInfo/detail?id=119&language=23&sn=34235","scenesId":28,"isLast":"true","level":null,"words":"1","parentId":117,"sequence":1,"router":null,"deleted":0,"treeLevel":"0,116,117,","children":null,"createByDate":1539934178000,"name":"三级模板","scenesName":null,"createById":1,"isHomeShow":1,"isHomeShowb":true,"details":"1","id":119,"modifyByDate":null}],"createByDate":1539931528000,"name":"二级模板1","scenesName":null,"createById":1,"isHomeShow":1,"isHomeShowb":true,"details":"1","id":117,"modifyByDate":null},{"modifyById":null,"resUrl":"http://localhost:1234567890/csjbot-service/api/contentInfo/detail?id=118&language=23&sn=34235","scenesId":28,"isLast":"true","level":null,"words":"2","parentId":116,"sequence":2,"router":null,"deleted":0,"treeLevel":"0,116,","children":null,"createByDate":1539934084000,"name":"二级目录2","scenesName":null,"createById":1,"isHomeShow":1,"isHomeShowb":true,"details":"2","id":118,"modifyByDate":null}],"modelType":3}
     * message : 查询成功
     * status : 200
     */

    private ResultBean result;
    private String message;
    private String status;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ResultBean {
        /**
         * data : [{"modifyById":null,"resUrl":"http://localhost:1234567890/csjbot-service/api/contentInfo/detail?id=117&language=23&sn=34235","scenesId":28,"isLast":"false","level":null,"words":"1","parentId":116,"sequence":1,"router":null,"deleted":0,"treeLevel":"0,116,","children":[{"modifyById":null,"resUrl":"http://localhost:1234567890/csjbot-service/api/contentInfo/detail?id=119&language=23&sn=34235","scenesId":28,"isLast":"true","level":null,"words":"1","parentId":117,"sequence":1,"router":null,"deleted":0,"treeLevel":"0,116,117,","children":null,"createByDate":1539934178000,"name":"三级模板","scenesName":null,"createById":1,"isHomeShow":1,"isHomeShowb":true,"details":"1","id":119,"modifyByDate":null}],"createByDate":1539931528000,"name":"二级模板1","scenesName":null,"createById":1,"isHomeShow":1,"isHomeShowb":true,"details":"1","id":117,"modifyByDate":null},{"modifyById":null,"resUrl":"http://localhost:1234567890/csjbot-service/api/contentInfo/detail?id=118&language=23&sn=34235","scenesId":28,"isLast":"true","level":null,"words":"2","parentId":116,"sequence":2,"router":null,"deleted":0,"treeLevel":"0,116,","children":null,"createByDate":1539934084000,"name":"二级目录2","scenesName":null,"createById":1,"isHomeShow":1,"isHomeShowb":true,"details":"2","id":118,"modifyByDate":null}]
         * modelType : 3
         */

        private int modelType;
        private List<DataBean> data;

        public int getModelType() {
            return modelType;
        }

        public void setModelType(int modelType) {
            this.modelType = modelType;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * modifyById : null
             * resUrl : http://localhost:1234567890/csjbot-service/api/contentInfo/detail?id=117&language=23&sn=34235
             * scenesId : 28
             * isLast : false
             * level : null
             * words : 1
             * parentId : 116
             * sequence : 1
             * router : null
             * deleted : 0
             * treeLevel : 0,116,
             * children : [{"modifyById":null,"resUrl":"http://localhost:1234567890/csjbot-service/api/contentInfo/detail?id=119&language=23&sn=34235","scenesId":28,"isLast":"true","level":null,"words":"1","parentId":117,"sequence":1,"router":null,"deleted":0,"treeLevel":"0,116,117,","children":null,"createByDate":1539934178000,"name":"三级模板","scenesName":null,"createById":1,"isHomeShow":1,"isHomeShowb":true,"details":"1","id":119,"modifyByDate":null}]
             * createByDate : 1539931528000
             * name : 二级模板1
             * scenesName : null
             * createById : 1
             * isHomeShow : 1
             * isHomeShowb : true
             * details : 1
             * id : 117
             * modifyByDate : null
             */

            private Object modifyById;
            private String resUrl;
            private int scenesId;
            private String isLast;
            private Object level;
            private String words;
            private int parentId;
            private int sequence;
            private Object router;
            private int deleted;
            private String treeLevel;
            private long createByDate;
            private String name;
            private Object scenesName;
            private int createById;
            private int isHomeShow;
            private boolean isHomeShowb;
            private String details;
            private int id;
            private Object modifyByDate;
            private List<ChildrenBean> children;

            public Object getModifyById() {
                return modifyById;
            }

            public void setModifyById(Object modifyById) {
                this.modifyById = modifyById;
            }

            public String getResUrl() {
                return resUrl;
            }

            public void setResUrl(String resUrl) {
                this.resUrl = resUrl;
            }

            public int getScenesId() {
                return scenesId;
            }

            public void setScenesId(int scenesId) {
                this.scenesId = scenesId;
            }

            public String getIsLast() {
                return isLast;
            }

            public void setIsLast(String isLast) {
                this.isLast = isLast;
            }

            public Object getLevel() {
                return level;
            }

            public void setLevel(Object level) {
                this.level = level;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getSequence() {
                return sequence;
            }

            public void setSequence(int sequence) {
                this.sequence = sequence;
            }

            public Object getRouter() {
                return router;
            }

            public void setRouter(Object router) {
                this.router = router;
            }

            public int getDeleted() {
                return deleted;
            }

            public void setDeleted(int deleted) {
                this.deleted = deleted;
            }

            public String getTreeLevel() {
                return treeLevel;
            }

            public void setTreeLevel(String treeLevel) {
                this.treeLevel = treeLevel;
            }

            public long getCreateByDate() {
                return createByDate;
            }

            public void setCreateByDate(long createByDate) {
                this.createByDate = createByDate;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getScenesName() {
                return scenesName;
            }

            public void setScenesName(Object scenesName) {
                this.scenesName = scenesName;
            }

            public int getCreateById() {
                return createById;
            }

            public void setCreateById(int createById) {
                this.createById = createById;
            }

            public int getIsHomeShow() {
                return isHomeShow;
            }

            public void setIsHomeShow(int isHomeShow) {
                this.isHomeShow = isHomeShow;
            }

            public boolean isIsHomeShowb() {
                return isHomeShowb;
            }

            public void setIsHomeShowb(boolean isHomeShowb) {
                this.isHomeShowb = isHomeShowb;
            }

            public String getDetails() {
                return details;
            }

            public void setDetails(String details) {
                this.details = details;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getModifyByDate() {
                return modifyByDate;
            }

            public void setModifyByDate(Object modifyByDate) {
                this.modifyByDate = modifyByDate;
            }

            public List<ChildrenBean> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBean> children) {
                this.children = children;
            }

            public static class ChildrenBean {
                /**
                 * modifyById : null
                 * resUrl : http://localhost:1234567890/csjbot-service/api/contentInfo/detail?id=119&language=23&sn=34235
                 * scenesId : 28
                 * isLast : true
                 * level : null
                 * words : 1
                 * parentId : 117
                 * sequence : 1
                 * router : null
                 * deleted : 0
                 * treeLevel : 0,116,117,
                 * children : null
                 * createByDate : 1539934178000
                 * name : 三级模板
                 * scenesName : null
                 * createById : 1
                 * isHomeShow : 1
                 * isHomeShowb : true
                 * details : 1
                 * id : 119
                 * modifyByDate : null
                 */

                private Object modifyById;
                private String resUrl;
                private int scenesId;
                private String isLast;
                private Object level;
                private String words;
                private int parentId;
                private int sequence;
                private Object router;
                private int deleted;
                private String treeLevel;
                private Object children;
                private long createByDate;
                private String name;
                private Object scenesName;
                private int createById;
                private int isHomeShow;
                private boolean isHomeShowb;
                private String details;
                private int id;
                private Object modifyByDate;

                public Object getModifyById() {
                    return modifyById;
                }

                public void setModifyById(Object modifyById) {
                    this.modifyById = modifyById;
                }

                public String getResUrl() {
                    return resUrl;
                }

                public void setResUrl(String resUrl) {
                    this.resUrl = resUrl;
                }

                public int getScenesId() {
                    return scenesId;
                }

                public void setScenesId(int scenesId) {
                    this.scenesId = scenesId;
                }

                public String getIsLast() {
                    return isLast;
                }

                public void setIsLast(String isLast) {
                    this.isLast = isLast;
                }

                public Object getLevel() {
                    return level;
                }

                public void setLevel(Object level) {
                    this.level = level;
                }

                public String getWords() {
                    return words;
                }

                public void setWords(String words) {
                    this.words = words;
                }

                public int getParentId() {
                    return parentId;
                }

                public void setParentId(int parentId) {
                    this.parentId = parentId;
                }

                public int getSequence() {
                    return sequence;
                }

                public void setSequence(int sequence) {
                    this.sequence = sequence;
                }

                public Object getRouter() {
                    return router;
                }

                public void setRouter(Object router) {
                    this.router = router;
                }

                public int getDeleted() {
                    return deleted;
                }

                public void setDeleted(int deleted) {
                    this.deleted = deleted;
                }

                public String getTreeLevel() {
                    return treeLevel;
                }

                public void setTreeLevel(String treeLevel) {
                    this.treeLevel = treeLevel;
                }

                public Object getChildren() {
                    return children;
                }

                public void setChildren(Object children) {
                    this.children = children;
                }

                public long getCreateByDate() {
                    return createByDate;
                }

                public void setCreateByDate(long createByDate) {
                    this.createByDate = createByDate;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Object getScenesName() {
                    return scenesName;
                }

                public void setScenesName(Object scenesName) {
                    this.scenesName = scenesName;
                }

                public int getCreateById() {
                    return createById;
                }

                public void setCreateById(int createById) {
                    this.createById = createById;
                }

                public int getIsHomeShow() {
                    return isHomeShow;
                }

                public void setIsHomeShow(int isHomeShow) {
                    this.isHomeShow = isHomeShow;
                }

                public boolean isIsHomeShowb() {
                    return isHomeShowb;
                }

                public void setIsHomeShowb(boolean isHomeShowb) {
                    this.isHomeShowb = isHomeShowb;
                }

                public String getDetails() {
                    return details;
                }

                public void setDetails(String details) {
                    this.details = details;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public Object getModifyByDate() {
                    return modifyByDate;
                }

                public void setModifyByDate(Object modifyByDate) {
                    this.modifyByDate = modifyByDate;
                }
            }
        }
    }
}
