package com.csjbot.mobileshop.model.http.apiservice.proxy;


import android.content.Intent;
import android.text.TextUtils;

import com.csjbot.mobileshop.BaseApplication;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.bean.rsp.SceneBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.util.CustomSDCardLoader;
import com.csjbot.mobileshop.util.FileUtil;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import skin.support.SkinCompatManager;

/**
 * Created by jingwc on 2018/12/18.
 */

public class SceneProxy {

    public static SceneProxy newInstance() {
        return new SceneProxy();
    }

    private SceneProxy() {

    }

    public void getScene(String sn, SceneListener listener) {
        ServerFactory.createApi().getScene(sn, new Observer<ResponseBean<SceneBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<SceneBean> sceneBeanResponseBean) {
                if (sceneBeanResponseBean != null) {
                    CsjlogProxy.getInstance().info("getScene onNext:json:" + new Gson().toJson(sceneBeanResponseBean));
                }
                if (sceneBeanResponseBean != null && sceneBeanResponseBean.getCode() == 200 && sceneBeanResponseBean.getRows() != null) {
                    //进入下载
                    saveScene(listener, sceneBeanResponseBean.getRows());
                } else {
                    if (listener != null) {
                        listener.onError();
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onError();
                }
                CsjlogProxy.getInstance().info("getScene:onError:e:" + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getScene(SceneListener listener) {
        String sn = Robot.SN;
        ServerFactory.createApi().getScene(sn, new Observer<ResponseBean<SceneBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<SceneBean> sceneBeanResponseBean) {
                if (sceneBeanResponseBean != null && sceneBeanResponseBean.getCode() == 200 && sceneBeanResponseBean.getRows() != null) {
                    CsjlogProxy.getInstance().info("getScene onNext:json:" + new Gson().toJson(sceneBeanResponseBean));
                    //进入下载
                    saveScene(listener, sceneBeanResponseBean.getRows());
                } else {
                    if (listener != null) {
                        listener.onError();
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onError();
                }
                CsjlogProxy.getInstance().info("getScene:onError:e:" + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void saveScene(SceneListener listener, SceneBean sceneBean) {
        String tempURL = sceneBean.getSceneCode();
        SharedPreUtil.putString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, tempURL);
        downLoadScene(sceneBean, listener);
    }

    private synchronized void downLoadScene(SceneBean bean, SceneListener listener) {
        String url = bean.getSkinResource();
        String[] field = url.split("/");
        String skinName = field[field.length - 1];
        ServerFactory.createApi().downloadSceneResource(url, new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                BaseApplication.isPushSkinEnd = true;
                if (responseBody == null) {
                    CsjlogProxy.getInstance().info("skin资源下载失败！");
                    if (listener != null) {
                        listener.onError();
                    }
                } else {
                    if (FileUtil.writeResponseBodyToDisk(responseBody, skinName)) {
                        if (listener != null) {
                            listener.onSuccess();
                        }
                        SkinCompatManager.getInstance().loadSkin(skinName, new SkinCompatManager.SkinLoaderListener() {
                            @Override
                            public void onStart() {
                                CsjlogProxy.getInstance().info("换肤开始");
                            }

                            @Override
                            public void onSuccess() {
                                BaseApplication.getAppContext().sendBroadcast(new Intent(Constants.UPDATE_LOGO));
                                Constants.HomePage.isHomePageLoadSuccess = true;
                            }

                            @Override
                            public void onFailed(String s) {
                                CsjlogProxy.getInstance().info("换肤失败" + s);
                                Constants.HomePage.isHomePageLoadSuccess = true;
                            }
                        }, CustomSDCardLoader.SKIN_LOADER_STRATEGY_SDCARD);
                    } else {
                        if (listener != null) {
                            listener.onError();
                        }
                        CsjlogProxy.getInstance().info("skin资源保存失败！");
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onError();
                }
                CsjlogProxy.getInstance().info("downloadSceneResource:onError:e:" + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public interface SceneListener {
        void onSuccess();

        void onError();
    }
}
