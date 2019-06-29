package com.csjbot.mobileshop.model.http.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/26.
 * @Package_name: BlackGaGa
 */
public class ContentTypeBean implements Serializable {

    /**
     * status : 200
     * message : ok
     * result : {"contentMessage":{"messageType":3,"parentId":"806fe7b9d0964acc9b3a7a8f87a2daec","messageList":[{"name":"三级1","speechScript":"三级1","order":1,"ccontentUrl":"","startDate":"","endDate":"","startTime":0,"endTime":0,"effective":true,"isDeleted":false,"icon":"","messages":[{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"05b7bf1b8ef04ae5b7f24996726dead1","icon":"","name":"三级1-1","speechScript":"三级1-1","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=0cc6e2f0720e4057bd36872a8a977164&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1},{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"05b7bf1b8ef04ae5b7f24996726dead1","icon":"","name":"三级1-2","speechScript":"三级1-2","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=edda4bcef93140ada5b0aa96788bd67b&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1}]},{"name":"三级3","speechScript":"三级3","order":1,"ccontentUrl":"","startDate":"","endDate":"","startTime":0,"endTime":0,"effective":true,"isDeleted":false,"icon":"","messages":[{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"215dd79f3a5a40998a48b19b82f72bc2","icon":"","name":"三级3-1","speechScript":"三级3-1","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=d7d4a372b0a54c0b8c70c34852cd1475&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1},{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"215dd79f3a5a40998a48b19b82f72bc2","icon":"","name":"三级3-2","speechScript":"三级3-2","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=e98a25ed43384c1ababf92a08670592d&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1}]},{"name":"三级4","speechScript":"三级4","order":1,"ccontentUrl":"","startDate":"","endDate":"","startTime":0,"endTime":0,"effective":true,"isDeleted":false,"icon":"","messages":[{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"b32372e0190940e9a87d4c2194f36936","icon":"","name":"三级4-2","speechScript":"三级4-2","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=72604228231e4066bd9266764dce912b&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1},{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"b32372e0190940e9a87d4c2194f36936","icon":"","name":"三级4-1","speechScript":"三级4","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=ab0966071b9244b8b2ed43222c0650cc&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1}]}]}}
     */

    private String status;
    private String message;
    private ResultBean result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * contentMessage : {"messageType":3,"parentId":"806fe7b9d0964acc9b3a7a8f87a2daec","messageList":[{"name":"三级1","speechScript":"三级1","order":1,"ccontentUrl":"","startDate":"","endDate":"","startTime":0,"endTime":0,"effective":true,"isDeleted":false,"icon":"","messages":[{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"05b7bf1b8ef04ae5b7f24996726dead1","icon":"","name":"三级1-1","speechScript":"三级1-1","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=0cc6e2f0720e4057bd36872a8a977164&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1},{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"05b7bf1b8ef04ae5b7f24996726dead1","icon":"","name":"三级1-2","speechScript":"三级1-2","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=edda4bcef93140ada5b0aa96788bd67b&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1}]},{"name":"三级3","speechScript":"三级3","order":1,"ccontentUrl":"","startDate":"","endDate":"","startTime":0,"endTime":0,"effective":true,"isDeleted":false,"icon":"","messages":[{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"215dd79f3a5a40998a48b19b82f72bc2","icon":"","name":"三级3-1","speechScript":"三级3-1","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=d7d4a372b0a54c0b8c70c34852cd1475&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1},{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"215dd79f3a5a40998a48b19b82f72bc2","icon":"","name":"三级3-2","speechScript":"三级3-2","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=e98a25ed43384c1ababf92a08670592d&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1}]},{"name":"三级4","speechScript":"三级4","order":1,"ccontentUrl":"","startDate":"","endDate":"","startTime":0,"endTime":0,"effective":true,"isDeleted":false,"icon":"","messages":[{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"b32372e0190940e9a87d4c2194f36936","icon":"","name":"三级4-2","speechScript":"三级4-2","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=72604228231e4066bd9266764dce912b&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1},{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"b32372e0190940e9a87d4c2194f36936","icon":"","name":"三级4-1","speechScript":"三级4","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=ab0966071b9244b8b2ed43222c0650cc&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1}]}]}
         */

        private ContentMessageBean contentMessage;

        public ContentMessageBean getContentMessage() {
            return contentMessage;
        }

        public void setContentMessage(ContentMessageBean contentMessage) {
            this.contentMessage = contentMessage;
        }

        public static class ContentMessageBean {
            /**
             * messageType : 3
             * parentId : 806fe7b9d0964acc9b3a7a8f87a2daec
             * messageList : [{"name":"三级1","speechScript":"三级1","order":1,"ccontentUrl":"","startDate":"","endDate":"","startTime":0,"endTime":0,"effective":true,"isDeleted":false,"icon":"","messages":[{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"05b7bf1b8ef04ae5b7f24996726dead1","icon":"","name":"三级1-1","speechScript":"三级1-1","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=0cc6e2f0720e4057bd36872a8a977164&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1},{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"05b7bf1b8ef04ae5b7f24996726dead1","icon":"","name":"三级1-2","speechScript":"三级1-2","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=edda4bcef93140ada5b0aa96788bd67b&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1}]},{"name":"三级3","speechScript":"三级3","order":1,"ccontentUrl":"","startDate":"","endDate":"","startTime":0,"endTime":0,"effective":true,"isDeleted":false,"icon":"","messages":[{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"215dd79f3a5a40998a48b19b82f72bc2","icon":"","name":"三级3-1","speechScript":"三级3-1","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=d7d4a372b0a54c0b8c70c34852cd1475&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1},{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"215dd79f3a5a40998a48b19b82f72bc2","icon":"","name":"三级3-2","speechScript":"三级3-2","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=e98a25ed43384c1ababf92a08670592d&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1}]},{"name":"三级4","speechScript":"三级4","order":1,"ccontentUrl":"","startDate":"","endDate":"","startTime":0,"endTime":0,"effective":true,"isDeleted":false,"icon":"","messages":[{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"b32372e0190940e9a87d4c2194f36936","icon":"","name":"三级4-2","speechScript":"三级4-2","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=72604228231e4066bd9266764dce912b&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1},{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"b32372e0190940e9a87d4c2194f36936","icon":"","name":"三级4-1","speechScript":"三级4","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=ab0966071b9244b8b2ed43222c0650cc&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1}]}]
             */

            private int messageType;
            private String parentId;
            private List<MessageListBean> messageList;

            public int getMessageType() {
                return messageType;
            }

            public void setMessageType(int messageType) {
                this.messageType = messageType;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public List<MessageListBean> getMessageList() {
                return messageList;
            }

            public void setMessageList(List<MessageListBean> messageList) {
                this.messageList = messageList;
            }

            public static class MessageListBean {
                /**
                 * name : 三级1
                 * speechScript : 三级1
                 * order : 1
                 * ccontentUrl :
                 * startDate :
                 * endDate :
                 * startTime : 0
                 * endTime : 0
                 * effective : true
                 * isDeleted : false
                 * icon :
                 * messages : [{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"05b7bf1b8ef04ae5b7f24996726dead1","icon":"","name":"三级1-1","speechScript":"三级1-1","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=0cc6e2f0720e4057bd36872a8a977164&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1},{"scene":"afd27c6d69ec11e893d100163e0f7607","parentId":"05b7bf1b8ef04ae5b7f24996726dead1","icon":"","name":"三级1-2","speechScript":"三级1-2","ccontentUrl":"http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=edda4bcef93140ada5b0aa96788bd67b&language=zh_CN","startDate":"","endDate":"","startTime":0,"endTime":0,"order":1}]
                 */

                private String name;
                private String speechScript;
                private int order;
                private String ccontentUrl;
                private String startDate;
                private String endDate;
                private int startTime;
                private int endTime;
                private boolean effective;
                private boolean isDeleted;
                private String icon;
                private List<MessagesBean> messages;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getSpeechScript() {
                    return speechScript;
                }

                public void setSpeechScript(String speechScript) {
                    this.speechScript = speechScript;
                }

                public int getOrder() {
                    return order;
                }

                public void setOrder(int order) {
                    this.order = order;
                }

                public String getCcontentUrl() {
                    return ccontentUrl;
                }

                public void setCcontentUrl(String ccontentUrl) {
                    this.ccontentUrl = ccontentUrl;
                }

                public String getStartDate() {
                    return startDate;
                }

                public void setStartDate(String startDate) {
                    this.startDate = startDate;
                }

                public String getEndDate() {
                    return endDate;
                }

                public void setEndDate(String endDate) {
                    this.endDate = endDate;
                }

                public int getStartTime() {
                    return startTime;
                }

                public void setStartTime(int startTime) {
                    this.startTime = startTime;
                }

                public int getEndTime() {
                    return endTime;
                }

                public void setEndTime(int endTime) {
                    this.endTime = endTime;
                }

                public boolean isEffective() {
                    return effective;
                }

                public void setEffective(boolean effective) {
                    this.effective = effective;
                }

                public boolean isIsDeleted() {
                    return isDeleted;
                }

                public void setIsDeleted(boolean isDeleted) {
                    this.isDeleted = isDeleted;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public List<MessagesBean> getMessages() {
                    return messages;
                }

                public void setMessages(List<MessagesBean> messages) {
                    this.messages = messages;
                }

                public static class MessagesBean {
                    /**
                     * scene : afd27c6d69ec11e893d100163e0f7607
                     * parentId : 05b7bf1b8ef04ae5b7f24996726dead1
                     * icon :
                     * name : 三级1-1
                     * speechScript : 三级1-1
                     * ccontentUrl : http://test.example.com:8080/csjbotservice/api/cms/content/detailed?id=0cc6e2f0720e4057bd36872a8a977164&language=zh_CN
                     * startDate :
                     * endDate :
                     * startTime : 0
                     * endTime : 0
                     * order : 1
                     */

                    private String scene;
                    private String parentId;
                    private String icon;
                    private String name;
                    private String speechScript;
                    private String ccontentUrl;
                    private String startDate;
                    private String endDate;
                    private int startTime;
                    private int endTime;
                    private int order;

                    public String getScene() {
                        return scene;
                    }

                    public void setScene(String scene) {
                        this.scene = scene;
                    }

                    public String getParentId() {
                        return parentId;
                    }

                    public void setParentId(String parentId) {
                        this.parentId = parentId;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public void setIcon(String icon) {
                        this.icon = icon;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getSpeechScript() {
                        return speechScript;
                    }

                    public void setSpeechScript(String speechScript) {
                        this.speechScript = speechScript;
                    }

                    public String getCcontentUrl() {
                        return ccontentUrl;
                    }

                    public void setCcontentUrl(String ccontentUrl) {
                        this.ccontentUrl = ccontentUrl;
                    }

                    public String getStartDate() {
                        return startDate;
                    }

                    public void setStartDate(String startDate) {
                        this.startDate = startDate;
                    }

                    public String getEndDate() {
                        return endDate;
                    }

                    public void setEndDate(String endDate) {
                        this.endDate = endDate;
                    }

                    public int getStartTime() {
                        return startTime;
                    }

                    public void setStartTime(int startTime) {
                        this.startTime = startTime;
                    }

                    public int getEndTime() {
                        return endTime;
                    }

                    public void setEndTime(int endTime) {
                        this.endTime = endTime;
                    }

                    public int getOrder() {
                        return order;
                    }

                    public void setOrder(int order) {
                        this.order = order;
                    }
                }
            }
        }
    }
}
