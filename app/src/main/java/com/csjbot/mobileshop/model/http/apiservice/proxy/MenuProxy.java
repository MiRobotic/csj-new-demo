package com.csjbot.mobileshop.model.http.apiservice.proxy;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.apiservice.event.UpdateContentTypeEvent;
import com.csjbot.mobileshop.model.http.apiservice.event.UpdateHomeModuleEvent;
import com.csjbot.mobileshop.model.http.bean.rsp.ContentInfoBean;
import com.csjbot.mobileshop.model.http.bean.rsp.GreetContentBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ModuleBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author ShenBen
 * @date 2019/1/3 11:42
 * @email 714081644@qq.com
 */

public class MenuProxy {

    private MenuProxy() {

    }

    public static MenuProxy newInstance() {
        return new MenuProxy();
    }

    public void getModule() {
        ServerFactory.createApi().getModule(Robot.SN, new Observer<ResponseBean<List<ModuleBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<List<ModuleBean>> listResponseBean) {
                if (listResponseBean != null && listResponseBean.getCode() == 200
                        && listResponseBean.getRows() != null && !listResponseBean.getRows().isEmpty()) {
                    //保存到本地
                    SharedPreUtil.putString(SharedKey.HOME_MODULE, SharedKey.HOME_MODULE, GsonUtils.objectToJson(listResponseBean));
                    EventBus.getDefault().post(new UpdateHomeModuleEvent(true));
                }
            }

            @Override
            public void onError(Throwable e) {
                CsjlogProxy.getInstance().error("主页模块 网络请求失败： " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getAllContentType(boolean isRefresh) {
        ServerFactory.createApi().getContentInfo(Robot.SN, new Observer<ResponseBean<List<ContentInfoBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<List<ContentInfoBean>> listResponseBean) {
                if (listResponseBean != null && listResponseBean.getCode() == 200) {
                    Constants.sContentInfos.clear();
                    if (listResponseBean.getRows() != null && !listResponseBean.getRows().isEmpty()) {
                        Collections.sort(listResponseBean.getRows());
                        Constants.sContentInfos.addAll(listResponseBean.getRows());
                        //保存数据
                        SharedPreUtil.putString(SharedKey.CONTENT_NAME, SharedKey.CONTENT_NAME, GsonUtils.objectToJson(listResponseBean));
                    } else {
                        SharedPreUtil.removeString(SharedKey.CONTENT_NAME, SharedKey.CONTENT_NAME);
                    }
                    if(isRefresh){
                        EventBus.getDefault().post(new UpdateContentTypeEvent(true));
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                CsjlogProxy.getInstance().error("内容管理 网络请求失败： " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getGreetContent() {
        ServerFactory.createApi().getGreetContent(Robot.SN, new Observer<ResponseBean<GreetContentBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<GreetContentBean> greetContentBeanResponseBean) {
                CsjlogProxy.getInstance().info("getGreetContent: Jpush :" + new Gson().toJson(greetContentBeanResponseBean));
                if (greetContentBeanResponseBean != null && greetContentBeanResponseBean.getCode() == 200) {
                    //保存数据
                    Constants.greetContentBean = greetContentBeanResponseBean.getRows();
                    SharedPreUtil.putString(SharedKey.ROBOT_DEFAULT_GREET_CONTENT, SharedKey.ROBOT_DEFAULT_GREET_CONTENT, GsonUtils.objectToJson(greetContentBeanResponseBean.getRows()));
                }
            }

            @Override
            public void onError(Throwable e) {
                CsjlogProxy.getInstance().error("getGreetContent:推送更新 onError:e:" + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
