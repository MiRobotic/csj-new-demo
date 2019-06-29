package com.csjbot.mobileshop.model.http.apiservice.proxy;


import android.graphics.Bitmap;
import android.text.TextUtils;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * Copyright (c) 2018, SuZhou CsjBot. All Rights Reserved.
 * www.example.com
 * <p>
 * Created by 浦耀宗 at 2018/04/01 0028-12:43.
 * Email: puyz@example.com
 */

public class OSSAwsProxy {

    public static OSSAwsProxy newInstance() {
        return new OSSAwsProxy();
    }

    private OSSAwsProxy() {

    }

    public void uploadFile(Bitmap bitmap, OSSListener listener) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
        }
        ServerFactory.createApi().getAwsOSSSignature(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String bodyJson = "";
                try {
                    bodyJson = responseBody.string();
                } catch (IOException e) {
                }

                CsjlogProxy.getInstance().info("ossSignatureBean bodyJson: " + bodyJson);
                if (!TextUtils.isEmpty(bodyJson)) {
                    try {
                        JSONObject object = new JSONObject(bodyJson);
                        //                    AwsOSSSignatureBean ossSignatureBean =
//                            new GsonBuilder().disableHtmlEscaping().create().fromJson(bodyJson, AwsOSSSignatureBean.class);
                        JSONObject rowsObj = object.getJSONObject("rows");

                        String key = System.currentTimeMillis() + "_" + Robot.SN + ".jpg";
                        Map<String, RequestBody> params = new HashMap<>();
                        params.put("key", convertToRequestBody(key));
                        params.put("X-Amz-Credential", convertToRequestBody(rowsObj.getString("X-Amz-Credential")));
                        params.put("X-Amz-Algorithm", convertToRequestBody(rowsObj.getString("X-Amz-Algorithm")));
                        params.put("X-Amz-Date", convertToRequestBody(rowsObj.getString("X-Amz-Date")));
                        params.put("policy", convertToRequestBody(rowsObj.getString("policy")));
                        params.put("X-Amz-Signature", convertToRequestBody(rowsObj.getString("X-Amz-Signature")));
                        params.put("Content-Type", convertToRequestBody("image/jpeg"));
                        params.put("acl", convertToRequestBody(rowsObj.getString("acl")));

                        String url = rowsObj.getString("url");

                        CsjlogProxy.getInstance().error(" ossSignatureBean params " + params.toString());

                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), stream.toByteArray());
                        MultipartBody.Part part = MultipartBody.Part.createFormData("file", key, requestBody);

                        CsjlogProxy.getInstance().info("ossSignatureBean.getUrl() " + url + "/" + key);
                        ServerFactory.createApi().uploadAwsOSS(
                                url, params, part, new Observer<Response<Void>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Response responseBody) {
                                        CsjlogProxy.getInstance().info("ossSignatureBean:onNext:");
                                        if (listener != null) {
                                            listener.onSuccess(url + "/" + key, key);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        CsjlogProxy.getInstance().info("ossSignatureBean:onError:" + e.getMessage());
                                        if (listener != null) {
                                            listener.onError();
                                        }
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
