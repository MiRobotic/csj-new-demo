package com.csjbot.mobileshop.model.http.bean.rsp;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/12/18.
 */

public class AwsOSSSignatureBean implements Serializable {

    /**
     * code : 200
     * rows : {"bucket":"csjbot-usa-oss","X-Amz-Date":"20190401T203934Z","X-Amz-Signature":"be9409e494e8b60a400a79e5b2bd8eede284e02a2289bbe410b2571db11a4501","X-Amz-Algorithm":"AWS4-HMAC-SHA256","X-Amz-Credential":"AKIAIGHEOWWVDZK4TPRA/20190401/us-east-1/s3/aws4_request","acl":"public-read","url":"http://csjbot-usa-oss.s3.amazonaws.com","policy":"eyJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJjc2pib3QtdXNhLW9zcyJ9LFsic3RhcnRzLXdpdGgiLCIka2V5IiwiIl0seyJYLUFtei1EYXRlIjoiMjAxOTA0MDFUMjAzOTM0WiJ9LHsiWC1BbXotQWxnb3JpdGhtIjoiQVdTNC1ITUFDLVNIQTI1NiJ9LHsiYWNsIjoicHVibGljLXJlYWQifSx7IlgtQW16LUNyZWRlbnRpYWwiOiJBS0lBSUdIRU9XV1ZEWks0VFBSQS8yMDE5MDQwMS91cy1lYXN0LTEvczMvYXdzNF9yZXF1ZXN0In1dLCJleHBpcmF0aW9uIjoiMjAxOS0wNC0wMVQyMDozOTozNC41NDRaIn0="}
     */

    private int code;
    private RowsBean rows;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public RowsBean getRows() {
        return rows;
    }

    public void setRows(RowsBean rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * bucket : csjbot-usa-oss
         * X-Amz-Date : 20190401T203934Z
         * X-Amz-Signature : be9409e494e8b60a400a79e5b2bd8eede284e02a2289bbe410b2571db11a4501
         * X-Amz-Algorithm : AWS4-HMAC-SHA256
         * X-Amz-Credential : AKIAIGHEOWWVDZK4TPRA/20190401/us-east-1/s3/aws4_request
         * acl : public-read
         * url : http://csjbot-usa-oss.s3.amazonaws.com
         * policy : eyJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJjc2pib3QtdXNhLW9zcyJ9LFsic3RhcnRzLXdpdGgiLCIka2V5IiwiIl0seyJYLUFtei1EYXRlIjoiMjAxOTA0MDFUMjAzOTM0WiJ9LHsiWC1BbXotQWxnb3JpdGhtIjoiQVdTNC1ITUFDLVNIQTI1NiJ9LHsiYWNsIjoicHVibGljLXJlYWQifSx7IlgtQW16LUNyZWRlbnRpYWwiOiJBS0lBSUdIRU9XV1ZEWks0VFBSQS8yMDE5MDQwMS91cy1lYXN0LTEvczMvYXdzNF9yZXF1ZXN0In1dLCJleHBpcmF0aW9uIjoiMjAxOS0wNC0wMVQyMDozOTozNC41NDRaIn0=
         */

        private String bucket;
        @JSONField(name = "X-Amz-Date")
        private String XAmzDate;
        @JSONField(name = "X-Amz-Signature")
        private String XAmzSignature;
        @JSONField(name = "X-Amz-Algorithm")
        private String XAmzAlgorithm;
        @JSONField(name = "X-Amz-Credential")
        private String XAmzCredential;
        private String acl;
        private String url;
        private String policy;

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getXAmzDate() {
            return XAmzDate;
        }

        public void setXAmzDate(String XAmzDate) {
            this.XAmzDate = XAmzDate;
        }

        public String getXAmzSignature() {
            return XAmzSignature;
        }

        public void setXAmzSignature(String XAmzSignature) {
            this.XAmzSignature = XAmzSignature;
        }

        public String getXAmzAlgorithm() {
            return XAmzAlgorithm;
        }

        public void setXAmzAlgorithm(String XAmzAlgorithm) {
            this.XAmzAlgorithm = XAmzAlgorithm;
        }

        public String getXAmzCredential() {
            return XAmzCredential;
        }

        public void setXAmzCredential(String XAmzCredential) {
            this.XAmzCredential = XAmzCredential;
        }

        public String getAcl() {
            return acl;
        }

        public void setAcl(String acl) {
            this.acl = acl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPolicy() {
            return policy;
        }

        public void setPolicy(String policy) {
            this.policy = policy;
        }
    }
}
