package com.csjbot.mobileshop.model.http.bean.rsp;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/12/18.
 */

public class OSSSignatureBean implements Serializable {


    private static final long serialVersionUID = -141878085061969562L;
    /**
     * bucket : csjbot-test
     * signature : 89d5e401304ccf0c8ea69e8e5fb6eb2f48c54b8211f2a9b6ce9042fe3326d4bf
     * accessKey : 49bd7e4024ba4474bab00cb16b061eb3
     * url : http://csjbot-test.su.bcebos.com
     * policy : eyJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJjc2pib3QtdGVzdCJ9LHsia2V5IjoiKiJ9XSwiZXhwaXJhdGlvbiI6IjIwMTgtMTItMThUMDk6MzM6NDFaIn0=
     */

    private String bucket;
    private String signature;
    private String accessKey;
    private String url;
    private String policy;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
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
