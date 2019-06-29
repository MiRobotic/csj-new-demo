package com.csjbot.mobileshop;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.advertisement.service.SplashService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.customer_service.CustomerHelpService;
import com.csjbot.mobileshop.feature.navigation.NaviAction;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.jpush.manager.CsjJPushManager;
import com.csjbot.mobileshop.model.http.apiservice.proxy.LogoProxy;
import com.csjbot.mobileshop.model.http.apiservice.proxy.MenuProxy;
import com.csjbot.mobileshop.model.http.apiservice.proxy.SceneProxy;
import com.csjbot.mobileshop.model.http.apiservice.proxy.UnreachablePointProxy;
import com.csjbot.mobileshop.model.http.bean.rsp.GreetContentBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ModuleBean;
import com.csjbot.mobileshop.model.http.bean.rsp.NaviResourceBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.bean.rsp.SensitiveWordsBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.network.CheckEthernetService;
import com.csjbot.mobileshop.network.NetworkListenerService;
import com.csjbot.mobileshop.router.BRouter;
import com.csjbot.mobileshop.service.HomeService;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by jingwc on 2018/4/4.
 */

public class SplashActivity extends BaseModuleActivity {

    public static String START = "START_MODE";

    // 冷启动
    public static final int COLD_START = 0;

    // 热启动
    public static final int HEAT_START = 1;

    // 启动模式
    private int mStartMode = COLD_START;

    // 点击次数
    private int mClickCount;

    // 主页是否获取完毕
    private boolean isHomePageSuccess, isLoading;


    @BindView(R.id.bt_default_home)
    Button bt_default_home;

    @BindView(R.id.splash_image)
    ImageView splash_image;

    @BindView(R.id.showInfoTextView)
    TextView showInfoTextView;

    private Handler mHandler = new Handler();

    private Runnable mShowRb = () -> {
        bt_default_home.setVisibility(View.VISIBLE);
    };


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        super.init();
        Robot.initSN();
        // 人工客服的服务
        startService(new Intent(this, CustomerHelpService.class));
            /* 启动控制底层连接的HomeService */
        startService(new Intent(this, HomeService.class));
        if (isPlus()) {
            startService(new Intent(this, AdvertisementService.class));

            Glide.with(this).load(R.drawable.splash_plus).into(splash_image);
        } else {
            Glide.with(this).load(R.drawable.splash).into(splash_image);
        }

        // 检查网络Service
        startService(new Intent(this, CheckEthernetService.class));

        CsjJPushManager.getInstance().setAliasSN();
        //检查App更新Service
//        startService(new Intent(this, UpdateAppService.class));
        //广告Service
        startService(new Intent(this, SplashService.class));

        initInternational();

        if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_BAIDU)) {
        }

        getModule();
        getSensitiveWords();
        getContentInfo();
        getNaviResource();
        getGreetContent();
        UnreachablePointProxy.newInstance().getUnreachableProxy(Robot.SN);
    }

    private void getGreetContent() {
        String json = SharedPreUtil.getString(SharedKey.ROBOT_DEFAULT_GREET_CONTENT, SharedKey.ROBOT_DEFAULT_GREET_CONTENT);
        if (!TextUtils.isEmpty(json)) {
            Constants.greetContentBean = new Gson().fromJson(json, GreetContentBean.class);
//            if (TextUtils.isEmpty(Constants.greetContentBean.getWords())) {
//                Constants.greetContentBean.setWords(getString(R.string.hello_welcome));
//            }
//
//            if (TextUtils.isEmpty(Constants.greetContentBean.getGreetContent())) {
//                Constants.greetContentBean.setGreetContent(getString(R.string.hello_welcome));
//            }
//            CsjlogProxy.getInstance().debug("getGreetContent ： 获取本地缓存的 GreetContent : " + Constants.greetContentBean.getWords());
        }
        ServerFactory.createApi().getGreetContent(Robot.SN, new Observer<ResponseBean<GreetContentBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<GreetContentBean> greetContentBeanResponseBean) {
                getLog().info("getGreetContent:onNext:" + new Gson().toJson(greetContentBeanResponseBean));
                if (greetContentBeanResponseBean != null && greetContentBeanResponseBean.getCode() == 200) {
                    //保存数据
                    Constants.greetContentBean = greetContentBeanResponseBean.getRows();
//                    if (TextUtils.isEmpty(Constants.greetContentBean.getGreetContent())) {
//                        Constants.greetContentBean.setGreetContent(getString(R.string.hello_welcome));
//                    }
                    SharedPreUtil.putString(SharedKey.ROBOT_DEFAULT_GREET_CONTENT, SharedKey.ROBOT_DEFAULT_GREET_CONTENT, GsonUtils.objectToJson(greetContentBeanResponseBean.getRows()));
                }
            }

            @Override
            public void onError(Throwable e) {
                getLog().error("getGreetContent:onError:e:" + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getNaviResource() {

        ServerFactory.createApi().getNaviResourceList(Robot.SN, new Observer<ResponseBean<List<NaviResourceBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<List<NaviResourceBean>> listResponseBean) {
                if (listResponseBean != null) {
                    getLog().info("getNaviResourceList:onNext:json:" + new Gson().toJson(listResponseBean));
                }
            }

            @Override
            public void onError(Throwable e) {
                getLog().error("getNaviResourceList:onError:e:" + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getContentInfo() {
        MenuProxy.newInstance().getAllContentType(false);
    }

    private void getSensitiveWords() {
        String json = SharedPreUtil.getString(SharedKey.SENSITIVE_WORDS, SharedKey.SENSITIVE_WORDS);
        if (!TextUtils.isEmpty(json)) {
            Constants.sensitiveWordsBeans = new Gson().fromJson(json, new TypeToken<List<SensitiveWordsBean>>() {
            }.getType());
        }
        ServerFactory.createApi().getSensitiveWords(Robot.SN, new Observer<ResponseBean<List<SensitiveWordsBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<List<SensitiveWordsBean>> listResponseBean) {
                getLog().info("getSensitiveWords:onNext:" + GsonUtils.objectToJson(listResponseBean));
                if (listResponseBean != null && listResponseBean.getCode() == 200
                        && listResponseBean.getRows() != null && !listResponseBean.getRows().isEmpty()) {
                    //保存到本地
                    SharedPreUtil.putString(SharedKey.SENSITIVE_WORDS, SharedKey.SENSITIVE_WORDS, new Gson().toJson(listResponseBean.getRows()));
                    Constants.sensitiveWordsBeans = listResponseBean.getRows();
                }
            }

            @Override
            public void onError(Throwable e) {
                getLog().error("getSensitiveWords:onError:e:" + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getModule() {
        ServerFactory.createApi().getModule(Robot.SN, new Observer<ResponseBean<List<ModuleBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<List<ModuleBean>> listResponseBean) {
                getLog().info("getModule:onNext:json:" + new Gson().toJson(listResponseBean));
                if (listResponseBean != null && listResponseBean.getCode() == 200
                        && listResponseBean.getRows() != null && !listResponseBean.getRows().isEmpty()) {
                    //保存到本地
                    SharedPreUtil.putString(SharedKey.HOME_MODULE, SharedKey.HOME_MODULE, GsonUtils.objectToJson(listResponseBean));
                }
            }

            @Override
            public void onError(Throwable e) {
                getLog().error("getModule:onError:e:" + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int languageMode = SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, CsjLanguage.CHINESE);
        refreshLanguage(languageMode);
        mStartMode = SharedPreUtil.getInt(SharedKey.STARTMODE, SharedKey.STARTMODE, 0);
        if (mStartMode == COLD_START) {// 冷启动
            // 初始化配置
            initConfig();
            // 超时任务(显示进入默认主页的textview)
            mHandler.postDelayed(mShowRb, 1000 * 60 * 2);
        } else if (mStartMode == HEAT_START) {// 热启动
            // 由主页切换语言功能跳转而来,直接跳转主页即可
//            new LogoServiceImpl().getLogo(Robot.SN);
            MenuProxy.newInstance().getAllContentType(true);
            LogoProxy.newInstance().getLogo();
            jumpHomePage();
        }

    }

    private void initConfig() {

        // 音量恢复
        setVoice();

        // 导航数据初始化
        NaviAction.getInstance().initData();

        // 电量信息初始化
        Constants.Charging.initCharging();

        // 腾讯bugly
        CrashReport.initCrashReport(getApplicationContext(), "99de59fd2e", false);
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("for_test")) {
            CrashReport.setUserId(getApplicationContext(), Robot.SN + " te");
        } else {
            CrashReport.setUserId(getApplicationContext(), Robot.SN);
        }

        // 检查wifi
        checkWifi();

        // 网络检查服务
        startService(new Intent(this, NetworkListenerService.class));

        // 数据加载
        isLoading = true;
        loadProductData();

        Constants.UnknownProblemAnswer.initUnknownProblemAnswer();


        Constants.isOpenChatView = SharedPreUtil.getBoolean(SharedKey.CHAT_VIEW, SharedKey.CHAT_VIEW_IS_OPEN, true);

    }

    private void initInternational() {
        if (!Constants.isI18N()) {
            CsjlogProxy.getInstance().error("not i18n, nuance not init");
            return;
        }


    }

    private void setVoice() {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volume = SharedPreUtil.getInt(SharedKey.VOICENAME, SharedKey.VOICEKEY, 8);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }


    /**
     * 检查Wifi如果是关闭状态则开启Wifi
     */
    public void checkWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int state = wifiManager.getWifiState();
        if (state == WifiManager.WIFI_STATE_DISABLED) {
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 主页跳转
     */
    private void jumpHomePage() {

//        SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, CsjLanguage.CHINESE);
//        refreshLanguage(CsjLanguage.CHINESE);
        BRouter.toAllSceneHome();
    }

    /**
     * 刷新本地语言
     *
     * @param mode
     */
    private void refreshLanguage(int mode) {
        Configuration config = getApplication().getResources().getConfiguration();
        config.locale = CsjLanguage.getLocaleByDef(mode);
//        CsjlogProxy.getInstance().error("language  = " + config.locale.getLanguage());
        getResources().updateConfiguration(config, getApplication().getResources().getDisplayMetrics());
    }

    /**
     * 加载产品数据
     */
    private void loadProductData() {
        LogoProxy.newInstance().getLogo();
        SceneProxy.newInstance().getScene(new SceneProxy.SceneListener() {
            @Override
            public void onSuccess() {
                isHomePageSuccess = true;
                isLoading = false;
                if (getTopActivity().contains(SplashActivity.class.getSimpleName())) {
                    jumpHomePage();
                }
            }

            @Override
            public void onError() {
                isLoading = false;
                isHomePageSuccess = false;
            }
        });
    }

    @OnClick(R.id.bt_default_home)
    public void goDefaultHomePage() {
        jumpHomePage();
    }

    @OnClick(R.id.rl_root)
    public void rl_root() {
        // 点击6次进入默认主页面
        mClickCount++;
        if (mClickCount == 6) {
            jumpHomePage();
            mClickCount = 0;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // onPause状态中直接销毁该页面
        SplashActivity.this.finish();
    }

    @Override
    public void networkAvailability() {
        super.networkAvailability();
        if (!isHomePageSuccess && !isLoading) {
            getModule();
            getSensitiveWords();
            getContentInfo();
            getNaviResource();
            getGreetContent();
            loadProductData();
        }
    }

    /**
     * 获得栈中最顶层的Activity
     *
     * @return
     */
    public String getTopActivity() {
        android.app.ActivityManager manager = (android.app.ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null && runningTaskInfos.size() > 0) {
            return runningTaskInfos.get(0).topActivity.getShortClassName();
        } else
            return "";
    }
}
