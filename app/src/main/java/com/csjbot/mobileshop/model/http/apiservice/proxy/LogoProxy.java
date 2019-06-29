package com.csjbot.mobileshop.model.http.apiservice.proxy;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.csjbot.mobileshop.BaseApplication;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.bean.rsp.LogoBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.util.FileUtil;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jingwc on 2018/12/18.
 */

public class LogoProxy{

    public static LogoProxy newInstance(){
        return new LogoProxy();
    }

    private LogoProxy(){

    }

    public void getLogo(String sn) {
        if (TextUtils.isEmpty(sn)) {
            SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGONAME, "");
            String path = Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME;
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            return;
        }
        CsjlogProxy.getInstance().info("getLogo sn:" + sn);
        ServerFactory.createApi().getLogo(sn, new Observer<ResponseBean<LogoBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<LogoBean> logoBeanResponseBean) {
                CsjlogProxy.getInstance().info("getLogo onNext:");
                if(logoBeanResponseBean != null){
                    CsjlogProxy.getInstance().info("getLogo onNext:json:" + new Gson().toJson(logoBeanResponseBean));
                }
                if (logoBeanResponseBean != null && logoBeanResponseBean.getRows() != null) {
                    LogoBean logoBean = logoBeanResponseBean.getRows();
                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGONAME, logoBean.getLogoName());
                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.ROBOTNAME, logoBean.getRobotName());
                    Glide.with(BaseApplication.getAppContext())
                            .asFile()
                            .load(Uri.decode(logoBean.getLogoUrl()))
                            .listener(new RequestListener<File>() {

                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                                    CsjlogProxy.getInstance().info("后台获取logo Failed");
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                                    CsjlogProxy.getInstance().info("后台获取logo ResourceReady");
                                    return false;
                                }
                            })
                            .into(new SimpleTarget<File>() {
                                @Override
                                public void onResourceReady(File resource, Transition<? super File> transition) {
                                    File pathFile = new File(Constants.Path.LOGO_PATH);
                                    if (!pathFile.exists()) {
                                        pathFile.mkdirs();
                                    }
                                    Observable.just(FileUtil.copy(resource, new File(Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME)))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(b -> {
                                                if (b) {
                                                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGOURL, Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME);
                                                    CsjlogProxy.getInstance().info("后台获取logo 下载成功");
                                                } else {
                                                    CsjlogProxy.getInstance().info("后台获取logo 下载失败");
                                                }
                                            });
                                }
                            });
                } else {
                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGONAME, "");
                    String path = Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME;
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                CsjlogProxy.getInstance().info("getLogo onError");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getLogo() {
        String sn = Robot.SN;
        if (TextUtils.isEmpty(sn)) {
            SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGONAME, "");
            String path = Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME;
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            return;
        }
        CsjlogProxy.getInstance().info("getLogo sn:" + sn);
        ServerFactory.createApi().getLogo(sn, new Observer<ResponseBean<LogoBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<LogoBean> logoBeanResponseBean) {
                CsjlogProxy.getInstance().info("getLogo onNext:");
                if(logoBeanResponseBean != null){
                    CsjlogProxy.getInstance().info("getLogo onNext:json:" + new Gson().toJson(logoBeanResponseBean));
                }
                if (logoBeanResponseBean != null && logoBeanResponseBean.getRows() != null) {
                    LogoBean logoBean = logoBeanResponseBean.getRows();
                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGONAME, logoBean.getLogoName());
                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.ROBOTNAME, logoBean.getRobotName());
                    Glide.with(BaseApplication.getAppContext())
                            .asFile()
                            .load(Uri.decode(logoBean.getLogoUrl()))
                            .listener(new RequestListener<File>() {

                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                                    CsjlogProxy.getInstance().info("后台获取logo Failed");
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                                    CsjlogProxy.getInstance().info("后台获取logo ResourceReady");
                                    return false;
                                }
                            })
                            .into(new SimpleTarget<File>() {
                                @Override
                                public void onResourceReady(File resource, Transition<? super File> transition) {
                                    File pathFile = new File(Constants.Path.LOGO_PATH);
                                    if (!pathFile.exists()) {
                                        pathFile.mkdirs();
                                    }
                                    Observable.just(FileUtil.copy(resource, new File(Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME)))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(b -> {
                                                if (b) {
                                                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGOURL, Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME);
                                                    CsjlogProxy.getInstance().info("后台获取logo 下载成功");
                                                } else {
                                                    CsjlogProxy.getInstance().info("后台获取logo 下载失败");
                                                }
                                            });
                                }
                            });
                } else {
                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGONAME, "");
                    String path = Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME;
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                CsjlogProxy.getInstance().info("getLogo onError");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public String getLogoName() {
        return SharedPreUtil.getString(SharedKey.LOGO, SharedKey.LOGONAME);
    }

    public String getLogoUrl() {
        return SharedPreUtil.getString(SharedKey.LOGO, SharedKey.LOGOURL);
    }

    public String getRobotName() {
        return SharedPreUtil.getString(SharedKey.LOGO, SharedKey.ROBOTNAME);
    }


}
