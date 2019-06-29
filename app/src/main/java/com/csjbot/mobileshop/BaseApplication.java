package com.csjbot.mobileshop;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mobstat.StatService;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.coshandler.log.Csjlogger;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.jpush.manager.CsjJPushManager;
import com.csjbot.mobileshop.model.http.factory.RetrofitFactory;
import com.csjbot.mobileshop.router.BRouter;
import com.csjbot.mobileshop.util.CustomSDCardLoader;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.mobileshop.util.ShellUtils;
import com.iflytek.cloud.SpeechUtility;

import java.util.Locale;

import skin.support.SkinCompatManager;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Created by xiasuhuei321 on 2017/8/14.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 * <p>
 * desc:初始化的各种操作
 */

public class BaseApplication extends Application {

    static Application app;
    public static boolean isFirstComing = true;

    public volatile static boolean isToAppGoHome = false;

    public volatile static boolean isPushSkinEnd = false;


    //================== add ==============================

    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();


    public WindowManager.LayoutParams getMywmParams() {
        return wmParams;
    }

    //================== add ==============================

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        /*换肤初始化*/
        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .addStrategy(new CustomSDCardLoader())                  // 自定义加载策略，指定SDCard路径
                .loadSkin();
        /* 初始化retrofit */
        RetrofitFactory.initClient();
        /* 科大讯飞appid */
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        BRouter.init(this, BuildConfig.DEBUG);

        setUncaughtExceptionHandler();
        initBaiduMap();

//        if (!Constants.isI18N()) {
        CsjJPushManager.getInstance().init(getApplicationContext(), BuildConfig.DEBUG);
//        }


        // 百度统计

        // 打开调试开关，可以查看logcat日志。版本发布前，为避免影响性能，移除此代码
        // 查看方法：adb logcat -s sdkstat

//        if (BuildConfig.BUILD_TYPE.equals("debug") || BuildConfig.BUILD_TYPE.equals("for_test")) {
////        StatService.setDebugOn(true);
//        }

        // 开启自动埋点统计，为保证所有页面都能准确统计，建议在Application中调用。
        // 第三个参数：autoTrackWebview：
        // 如果设置为true，则自动track所有webview；如果设置为false，则不自动track webview，
        // 如需对webview进行统计，需要对特定webview调用trackWebView() 即可。
        // 重要：如果有对webview设置过webchromeclient，则需要调用trackWebView() 接口将WebChromeClient对象传入，
        // 否则开发者自定义的回调无法收到。
        StatService.autoTrace(this, true, true);

        // 根据需求使用
        // StatService.autoTrace(this, true, false);

        SharedPreUtil.putInt(SharedKey.STARTMODE, SharedKey.STARTMODE, 0);//冷启动


        if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_BAIDU)) {
        }

        if (!Constants.isI18N()) {
//            if (CsjLanguage.CURRENT_LANGUAGE != CsjLanguage.CHINESE) {
//                CsjLanguage.CURRENT_LANGUAGE = CsjLanguage.CHINESE;
//
//                SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME,
//                        SharedKey.LANGUAGEMODE_KEY, CsjLanguage.CHINESE);
//            }
            CsjLanguage.CURRENT_LANGUAGE = SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME,
                    SharedKey.LANGUAGEMODE_KEY, CsjLanguage.CHINESE);
        } else {
            CsjLanguage.CURRENT_LANGUAGE = SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME,
                    SharedKey.LANGUAGEMODE_KEY, CsjLanguage.ENGELISH_US);

            SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME,
                    SharedKey.LANGUAGEMODE_KEY, CsjLanguage.CURRENT_LANGUAGE);

        }
        CsjlogProxy.getInstance().error("language  =" + CsjLanguage.CURRENT_LANGUAGE);

        // 直接粗暴的设置 density 为 160
        ShellUtils.execCommand("wm density 160", true);
    }

    private void setUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            // 输出异常！
            throwable.printStackTrace();
            CsjlogProxy.getInstance().error(throwable.toString());
            if (!BuildConfig.DEBUG) {
                Csjlogger.debug("-异常重启-");
                new Thread(() -> {
                    restartApp();
                }).start();
            }
        });

    }

    public static Application getAppContext() {
        return app;
    }

    /**
     * 初始化百度地图和定位
     */
    private void initBaiduMap() {
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
//        LocationUtil.init(this);
//        LocationUtil.start();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void restartApp() {
        // 防止无限重启，这里需要做个保障，10次没有进入 BaseModuleActivity 就不重启了。
        int not_enter_main = SharedPreUtil.getInt(SharedKey.REBOOTTIME,
                SharedKey.REBOOTTIME_KEY, 0);
        if (not_enter_main < 10) {
            not_enter_main++;
            SharedPreUtil.putInt(SharedKey.REBOOTTIME, SharedKey.REBOOTTIME_KEY, not_enter_main);

            // restart
            String appName = BuildConfig.APPLICATION_ID;
            String cmd = "am force-stop %s\nam start -n %s/%s.SplashActivity ";
            cmd = String.format(Locale.getDefault(), cmd, appName, appName, appName);
            ShellUtils.execCommand(cmd, true, false);
        }
    }
}
