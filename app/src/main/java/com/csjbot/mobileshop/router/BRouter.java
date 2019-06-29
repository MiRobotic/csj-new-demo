package com.csjbot.mobileshop.router;

import android.app.Application;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.csjbot.mobileshop.feature.content.ContentActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.bean.rsp.ContentInfoBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import skin.support.SkinCompatManager;

/**
 * Created by xiasuhuei321 on 2017/11/28.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class BRouter {
    /**
     * 获取到现在的主页是哪个
     */
    public static String CURRENTCLASSNAME = "";

    /**
     * 初始化 路由
     */
    public static void init(Application app, boolean isDebug) {
        if (isDebug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(app);
    }

    private static class BRouterHolder {
        private static final BRouter INSTANCE = new BRouter();
    }

    public static BRouter getInstance() {
        return BRouterHolder.INSTANCE;
    }

    // 应用内跳转
    public BRouterDataCarry build(String url) {
        return new BRouterDataCarry(url);
    }

    /**
     * 根据传入的path进行activity的跳转
     *
     * @param path activity 路径，可见 BRouterPath
     */
    public static void jumpActivity(@Nullable String path) {
        BRouter.getInstance()
                .build(path)
                .navigation();
    }

    /**
     * 根据传入的path进行activity的跳转
     * 带参数跳转
     *
     * @param path activity 路径，可见 BRouterPath
     */
    public static void jumpActivity(@Nullable String path, @Nullable String key, @Nullable Serializable bundle) {
        BRouter.getInstance()
                .build(path)
                .withSerializable(key, bundle)
                .navigation();
    }

    /**
     * 根据传入的path进行activity的跳转
     * 带参数跳转
     *
     * @param path activity 路径，可见 BRouterPath
     */
    public static void jumpActivity(@Nullable String path, @Nullable String key, @Nullable String bundle) {
        BRouter.getInstance()
                .build(path)
                .withString(key, bundle)
                .navigation();
    }

    /**
     * 根据传入的path进行activity的跳转
     * 带参数跳转
     *
     * @param path activity 路径，可见 BRouterPath
     */
    public static void jumpActivity(@Nullable String path, @Nullable String key, @Nullable Parcelable bundle) {
        BRouter.getInstance()
                .build(path)
                .withParcelable(key, bundle)
                .navigation();
    }

    /**
     * 根据传入的path进行activity的跳转
     * 带参数跳转
     */
    public static void jumpActivityByContent(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        CsjlogProxy.getInstance().info("跳转内容管理, name: " + name);
        List<ContentInfoBean> list = new ArrayList<>(Constants.sContentInfos);
        if (list.isEmpty()) {
            String json = SharedPreUtil.getString(SharedKey.CONTENT_NAME, SharedKey.CONTENT_NAME);
            if (TextUtils.isEmpty(json)) {
                return;
            }
            ResponseBean<List<ContentInfoBean>> listResponseBean = GsonUtils.jsonToObject(json,
                    new TypeToken<ResponseBean<List<ContentInfoBean>>>() {
                    }.getType());
            if (listResponseBean != null) {
                list = new ArrayList<>(listResponseBean.getRows());
            }
        }
        int level;//几级菜单
        String data;
        String words = "";
        for (ContentInfoBean bean : list) {
            if (bean.isHomeShow() && TextUtils.equals(bean.getName(), name)) {
                if (bean.getChildren() == null || bean.getChildren().isEmpty()) {//1级菜单
                    level = 1;
                    data = bean.getDetails();
                    words = bean.getWords();
                } else {//其余情况为2级
                    level = 2;
                    data = GsonUtils.objectToJson(bean.getChildren());
                    for (ContentInfoBean.ChildrenBeanX beanX : bean.getChildren()) {
                        if (beanX.getChildren() != null && !beanX.getChildren().isEmpty()) {//二级里Children不为空，则为三级
                            level = 3;
                            break;
                        }
                    }
                }
                CsjlogProxy.getInstance().info("跳转内容管理, 菜单等级: " + level + ",话术: " + words);
                ARouter.getInstance()
                        .build(BRouterPath.CONTENT_ACTIVITY)
                        .withInt(ContentActivity.CONTENT_LEVEL, level)
                        .withString(ContentActivity.CONTENT_DATA, data)
                        .withString(ContentActivity.CONTENT_WORD, words)
                        .navigation();
                break;
            }
        }
    }


    /**
     * 实时刷新
     *
     * @param name
     */
    public static void sync(String name) {

    }

    /**
     * 跳转到主界面
     */
    public static void toHome() {
        toAllSceneHome();
    }

    /**
     * 程序进入首次使用，跳转到首页并指定场景
     */
    public static void toAllSceneHome() {
        String industry = SharedPreUtil.getString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, "CSJ_BOT");//默认场景
        Constants.initSceneColor(industry);
        Constants.Scene.CurrentScene = industry;
        CsjlogProxy.getInstance().info("跳转的场景: " + industry);
        ARouter.getInstance().build(BRouterPath.ALL_SCENE_HOME_PATH)
                .withString(BRouterKey.SCENE_NAME, industry)
                .navigation();
    }

    /**
     * 跳转到测试主界面
     */
    public static void toTestHome(String industry) {
        Constants.initSceneColor(industry);
        SkinCompatManager.getInstance().loadSkin(industry + ".skins", new SkinCompatManager.SkinLoaderListener() {

            @Override
            public void onStart() {
                CsjlogProxy.getInstance().warn("开始换肤");
            }

            @Override
            public void onSuccess() {
                CsjlogProxy.getInstance().warn("换肤成功");
            }

            @Override
            public void onFailed(String s) {
                CsjlogProxy.getInstance().warn("换肤失败  " + s);

            }
        }, SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
        ARouter.getInstance().build(BRouterPath.ALL_SCENE_HOME_PATH)
                .withString(BRouterKey.SCENE_NAME, industry)
                .navigation();
    }

    public static void loadLocalSkin(String skinName) {
        SkinCompatManager.getInstance().loadSkin(skinName + ".skins", new SkinCompatManager.SkinLoaderListener() {

            @Override
            public void onStart() {
                CsjlogProxy.getInstance().warn("开始换肤");
            }

            @Override
            public void onSuccess() {
                CsjlogProxy.getInstance().warn("换肤成功");
            }

            @Override
            public void onFailed(String s) {
                CsjlogProxy.getInstance().warn("换肤失败  " + s);

            }
        }, SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
    }
}
