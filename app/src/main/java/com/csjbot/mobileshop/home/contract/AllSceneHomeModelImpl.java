package com.csjbot.mobileshop.home.contract;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.apiservice.proxy.OSSAwsProxy;
import com.csjbot.mobileshop.model.http.apiservice.proxy.OSSProxy;
import com.csjbot.mobileshop.model.http.bean.req.VisitorBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.bean.rsp.VipGreetBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.google.gson.Gson;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author ShenBen
 * @date 2019/1/18 11:43
 * @email 714081644@qq.com
 */

public class AllSceneHomeModelImpl implements AllSceneHomeModel {
    @Override
    public void uploadBitmap(Bitmap bitmap, String faceId, String userName) {
        if (Constants.isI18N()) {
            OSSAwsProxy.newInstance().uploadFile(bitmap, new OSSAwsProxy.OSSListener() {
                @Override
                public void onSuccess(String url, String name) {
                    CsjlogProxy.getInstance().info("aws 访客图片上传: url: " + url);
                    uploadVisitor(url, faceId, userName);
                }

                @Override
                public void onError() {
                    uploadVisitor(null, faceId, userName);
                }
            });
        } else {
            OSSProxy.newInstance().uploadFile(bitmap, new OSSProxy.OSSListener() {
                @Override
                public void onSuccess(String url, String name) {
                    CsjlogProxy.getInstance().info("访客图片上传: url: " + url);
                    uploadVisitor(url, faceId, userName);
                }

                @Override
                public void onError() {
                    uploadVisitor(null, faceId, userName);
                }
            });
        }
    }

    @Override
    public void uploadVisitor(String imageUrl, String faceId, String userName) {
        VisitorBean visitorBean = new VisitorBean();
        visitorBean.setSn(Robot.SN);
        visitorBean.setImageUrl(imageUrl);
        visitorBean.setSessionId(Robot.sessionId);
        visitorBean.setIsMember(TextUtils.isEmpty(faceId) ? "0" : "1");
        visitorBean.setUserName(userName);
        String body = new Gson().toJson(visitorBean);
        CsjlogProxy.getInstance().info("上传访客信息： " + body);
        ServerFactory.createApi().uploadVisitor(body, new Observer<ResponseBean<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<String> stringResponseBean) {
                if (stringResponseBean != null) {
                    CsjlogProxy.getInstance().info("上传访客信息:onNext:json" + new Gson().toJson(stringResponseBean));
                }
            }

            @Override
            public void onError(Throwable e) {
                CsjlogProxy.getInstance().error("上传访客信息:onError:e:" + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getVipGreet(String faceId, Consumer<VipGreetBean> onNext, Consumer<Throwable> onError) {
        ServerFactory.createApi().getVipGreet(Robot.SN, faceId, new Observer<ResponseBean<VipGreetBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ResponseBean<VipGreetBean> vipGreetBeanResponseBean) {
                if (vipGreetBeanResponseBean != null
                        && vipGreetBeanResponseBean.getCode() == 200
                        && vipGreetBeanResponseBean.getRows() != null) {
                    if (onNext != null) {
                        try {
                            onNext.accept(vipGreetBeanResponseBean.getRows());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (onNext != null) {
                        try {
                            onNext.accept(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                CsjlogProxy.getInstance().error("获取会员信息:onError:e:" + e.toString());
                if (onError != null) {
                    try {
                        onError.accept(e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
