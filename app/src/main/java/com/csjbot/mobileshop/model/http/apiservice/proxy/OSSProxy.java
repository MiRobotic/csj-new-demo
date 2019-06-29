package com.csjbot.mobileshop.model.http.apiservice.proxy;


import android.graphics.Bitmap;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.model.http.bean.rsp.OSSSignatureBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.util.GsonUtils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by jingwc on 2018/12/20.
 */

public class OSSProxy {

    public static OSSProxy newInstance() {
        return new OSSProxy();
    }

    private OSSProxy() {

    }

    public void uploadFile(Bitmap bitmap, OSSListener listener) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        ServerFactory.createApi().getOSSSignature(new Observer<ResponseBean<OSSSignatureBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<OSSSignatureBean> responseBean) {
                CsjlogProxy.getInstance().info("getOSSSignature:onNext:json: " + GsonUtils.objectToJson(responseBean));
                if (responseBean != null && responseBean.getCode() == 200 && responseBean.getRows() != null) {
                    OSSSignatureBean ossSignatureBean = responseBean.getRows();
                    String key = System.currentTimeMillis() + "_" + Robot.SN + ".jpeg";
                    Map<String, RequestBody> params = new HashMap<>();
                    params.put("key", convertToRequestBody(key));
                    params.put("bucket", convertToRequestBody(ossSignatureBean.getBucket()));
                    params.put("signature", convertToRequestBody(ossSignatureBean.getSignature()));
                    params.put("accessKey", convertToRequestBody(ossSignatureBean.getAccessKey()));
                    params.put("url", convertToRequestBody(ossSignatureBean.getUrl()));
                    params.put("policy", convertToRequestBody(ossSignatureBean.getPolicy()));


                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), stream.toByteArray());
                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", key, requestBody);

                    com.csjbot.mobileshop.model.http.factory.ServerFactory.createApi().uploadOSS(
                            ossSignatureBean.getUrl(), params, part, new Observer<ResponseBody>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(ResponseBody responseBody) {
                                    CsjlogProxy.getInstance().info("uploadOSS:onNext:");
                                    if (listener != null) {
                                        listener.onSuccess(ossSignatureBean.getUrl() + "/" + key, key);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (listener != null) {
                                        listener.onError();
                                    }
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
            }

            @Override
            public void onError(Throwable e) {
                CsjlogProxy.getInstance().error("getOSSSignature:onError: " + e.getMessage());
                if (listener != null) {
                    listener.onError();
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private RequestBody convertToRequestBody(String param) {
        if (param == null) {
            param = "";
        }

        return RequestBody.create(MediaType.parse("text/plain"), param);
    }

    public interface OSSListener {
        void onSuccess(String url, String name);

        void onError();
    }
}
