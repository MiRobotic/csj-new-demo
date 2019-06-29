package com.csjbot.mobileshop.advertisement.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnDetectPersonListener;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnHeadTouchListener;
import com.csjbot.coshandler.listener.OnMakeSessionIdListener;
import com.csjbot.coshandler.listener.OnWakeupListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.AdvertisementEvent;
import com.csjbot.mobileshop.advertisement.event.AudioEvent;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.factory.ProxyFactory;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.feature.course.MSTPlayserActivity;
import com.csjbot.mobileshop.feature.dance.DanceActivity;
import com.csjbot.mobileshop.feature.music.MusicActivity;
import com.csjbot.mobileshop.feature.music.MusicInternationalActivity;
import com.csjbot.mobileshop.feature.navigation.NaviGuideCommentActivity;
import com.csjbot.mobileshop.feature.settings.SettingSYingbinActivity;
import com.csjbot.mobileshop.feature.settings.SettingVirtualKey;
import com.csjbot.mobileshop.feature.settings.SettingsAboutActivity;
import com.csjbot.mobileshop.feature.settings.SettingsActivity;
import com.csjbot.mobileshop.feature.settings.SettingsEvaluateActivity;
import com.csjbot.mobileshop.feature.settings.SettingsManualPositioningActivity;
import com.csjbot.mobileshop.feature.settings.SettingsNaviActivity;
import com.csjbot.mobileshop.feature.settings.SettingsNaviSoundControlActivity;
import com.csjbot.mobileshop.feature.settings.SettingsOtherActivity;
import com.csjbot.mobileshop.feature.settings.SettingsResetActivity;
import com.csjbot.mobileshop.feature.settings.SettingsRobotStateActivity;
import com.csjbot.mobileshop.feature.settings.SettingsSemanticsActivity;
import com.csjbot.mobileshop.feature.settings.SettingsSpeedActivity;
import com.csjbot.mobileshop.feature.settings.SettingsVolumeActivity;
import com.csjbot.mobileshop.feature.settings.change_skin.ChangeSkinActivity;
import com.csjbot.mobileshop.feature.settings.charge_setting.ChargeSettingActivity;
import com.csjbot.mobileshop.feature.settings.checked_update.SettingsCheckUpdateActivity;
import com.csjbot.mobileshop.feature.settings.network.SettingsNetworkActivity;
import com.csjbot.mobileshop.feature.settings.pwd_setting.PWDManagementActivity;
import com.csjbot.mobileshop.feature.settings.settings_list.SettingsListActivity;
import com.csjbot.mobileshop.feature.settings.synchronous_data_setting.SynchronousDataActivity;
import com.csjbot.mobileshop.feature.story.StoryActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.guide_patrol.patrol.PatrolPWDActivity;
import com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine.StateMachineManager;
import com.csjbot.mobileshop.guide_patrol.patrol.event.PatrolActionEvent;
import com.csjbot.mobileshop.guide_patrol.patrol.event.PatrolPauseEvent;
import com.csjbot.mobileshop.guide_patrol.patrol.patrol_setting.PatrolSettingActivity;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;
import com.csjbot.mobileshop.model.http.bean.rsp.AdvertisementInfoBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.robot_test.UpBodyTestActivity;
import com.csjbot.mobileshop.router.BRouter;
import com.csjbot.mobileshop.service.GlobalNoticeService;
import com.csjbot.mobileshop.service.WeatherService;
import com.csjbot.mobileshop.widget.GlideImageLoader;
import com.csjbot.mobileshop.widget.MyBanner;
import com.csjbot.mobileshop.widget.MyVideoView;
import com.danikula.videocache.HttpProxyCacheServer;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author ShenBen
 * @date 2018/12/4 13:12
 * @email 714081644@qq.com
 */

public class SplashService extends Service implements OnDetectPersonListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, OnFaceListener, OnHeadTouchListener {

    private HttpProxyCacheServer mServer;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    private List<String> mPictureList;
    private List<String> mVideoList;

    private View mView;
    private ImageView mIvLogo;
    private MyVideoView mVideoView;
    private MyBanner mBanner;
    private TextView tvRobotName;
    private static final String LOGO_PATH = Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME;

    /**
     * 显示广告的类型
     * 1:视频
     * 2:图片
     */
    private int showType = 0;
    /**
     * 视频列表当前播放位置
     */
    private int mVideoPosition = 0;
    private boolean isShowing;
    /**
     * 常见视频格式
     */
    private String[] videoUrls = new String[]{".mp4", ".MP4", ".avi", ".AVI", ".wma", ".WMA", ".rmvb", ".RMVB", ".rm", ".RM", ".flash", ".FLASH", ".mid", ".MID", ".3gp", ".3GB", ".mov", ".MOV", ".mkv", ".MKV", ".mpg", ".MPG", ".flv", ".FLV"};
    private CountDownTimer mTimer;
    /**
     * 不显示闪屏页的Activity
     */
    private List<String> mNoShowActivity;

    public static final int SPLASH_SERVICE_SHOW = 1;
    public static final int SPLASH_SERVICE_DISMISS = 2;
    public static final int SPLASH_SERVICE_GET_AD = 3;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SPLASH_SERVICE_SHOW:
                    Log.e("sunxy", "显示待机页面");
                    show();
                    break;
                case SPLASH_SERVICE_DISMISS:
                    dismiss();
                    break;
                case SPLASH_SERVICE_GET_AD:
                    mHandler.postDelayed(() -> getAdvertisement(), 10000);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mNoShowActivity = new ArrayList<>();
        RobotManager.getInstance().addListener((OnDetectPersonListener) this);
        RobotManager.getInstance().addListener((OnFaceListener) this);
        RobotManager.getInstance().addListener((OnHeadTouchListener) this);
        RobotManager.getInstance().addListener(mWakeupListener);
        // 如果是国外版本，就不用 缓存
        if (!Constants.isI18N()) {
            mServer = ProxyFactory.getProxy(this);
        }
        mVideoList = new ArrayList<>();
        mPictureList = new ArrayList<>();

        mTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int second = (int) (millisUntilFinished / 1000);
                if (!isShowing && second == 30 && StateMachineManager.getInstance().getCurrentState() == NaviTaskStatus.AWAIT && !Constants.isNaviMove) {//30秒播报，请问还有什么可以帮您的
                    String top = getTopActivity();
                    if (TextUtils.isEmpty(top)) {
                        return;
                    }
                    for (String s : mNoShowActivity) {
                        if (top.contains(s)) {
                            return;
                        }
                    }
                    EventBus.getDefault().post(new AdvertisementEvent(AdvertisementEvent.ASK_EVENT, getString(R.string.patrol_help)));
                }
            }

            @Override
            public void onFinish() {
                if (StateMachineManager.getInstance().getCurrentState() == NaviTaskStatus.AWAIT && !Constants.isNaviMove) {
                    mHandler.sendEmptyMessage(SPLASH_SERVICE_SHOW);
                }
            }
        };
//        mNoShowActivity.add(NaviActivity.class.getSimpleName());//导航页面不显示
        mNoShowActivity.add(SettingsActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsListActivity.class.getSimpleName());
        mNoShowActivity.add(SettingSYingbinActivity.class.getSimpleName());
        mNoShowActivity.add(SettingVirtualKey.class.getSimpleName());
        mNoShowActivity.add(SettingsAboutActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsCheckUpdateActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsEvaluateActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsManualPositioningActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsNaviActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsNaviSoundControlActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsOtherActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsResetActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsRobotStateActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsSemanticsActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsSpeedActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsVolumeActivity.class.getSimpleName());
        mNoShowActivity.add(ChangeSkinActivity.class.getSimpleName());
        mNoShowActivity.add(ChargeSettingActivity.class.getSimpleName());
        mNoShowActivity.add(SettingsNetworkActivity.class.getSimpleName());
        mNoShowActivity.add(PWDManagementActivity.class.getSimpleName());
        mNoShowActivity.add(SynchronousDataActivity.class.getSimpleName());
        mNoShowActivity.add(UpBodyTestActivity.class.getSimpleName());
        mNoShowActivity.add(MusicActivity.class.getSimpleName());
        mNoShowActivity.add(StoryActivity.class.getSimpleName());
        mNoShowActivity.add(DanceActivity.class.getSimpleName());
//        mNoShowActivity.add(PatrolActivity.class.getSimpleName());
        mNoShowActivity.add(PatrolSettingActivity.class.getSimpleName());
        mNoShowActivity.add(PatrolPWDActivity.class.getSimpleName());
        mNoShowActivity.add(NaviGuideCommentActivity.class.getSimpleName());
//        mNoShowActivity.add(ProductTypeActivity.class.getSimpleName());
//        mNoShowActivity.add(ProductListActivity.class.getSimpleName());
//        mNoShowActivity.add(ProductDetailsActivity.class.getSimpleName());
        mNoShowActivity.add(MSTPlayserActivity.class.getSimpleName());
        mNoShowActivity.add(MusicInternationalActivity.class.getSimpleName());
        createFloatWindow();
        getAdvertisement();

        RobotManager.getInstance().addListener((OnMakeSessionIdListener) sessionId -> Robot.sessionId = sessionId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        RobotManager.getInstance().removeListener((OnDetectPersonListener) this);
        RobotManager.getInstance().removeListener((OnFaceListener) this);
        RobotManager.getInstance().removeListener((OnHeadTouchListener) this);
        RobotManager.getInstance().removeListener(mWakeupListener);
    }

    /**
     * 创建悬浮窗
     */
    @SuppressLint("InflateParams")
    private void createFloatWindow() {


        mWindowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = 552;
        mLayoutParams.windowAnimations = 0;
        mLayoutParams.gravity = Gravity.TOP | Gravity.START;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        LayoutInflater inflate = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflate != null;
        if (isPlus()) {
            mView = inflate.inflate(R.layout.layout_plus_splash, null);
        } else {
            mView = inflate.inflate(R.layout.layout_splash, null);
        }
        LinearLayout mLayout = mView.findViewById(R.id.ll_layout);
        mVideoView = mView.findViewById(R.id.video);
        mBanner = mView.findViewById(R.id.banner);
        mIvLogo = mView.findViewById(R.id.iv_logo);
        tvRobotName = mView.findViewById(R.id.tv_robot_name);

//        tvWakeUp = mView.findViewById(R.id.tv_wake_up_word);

        mLayout.setOnClickListener(v -> {
            if (isShowing) {
                EventBus.getDefault().post(new PatrolPauseEvent());
                com.csjbot.mobileshop.model.tcp.factory.ServerFactory.getRobotState().makeSessionId();
                dismiss();
            }
        });
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);

        mBanner.setOnClickListener(v -> {
            if (isShowing) {
                com.csjbot.mobileshop.model.tcp.factory.ServerFactory.getRobotState().makeSessionId();
                dismiss();
            }
        });
    }

    /**
     * 获取广告
     */
    private void getAdvertisement() {
        if (TextUtils.isEmpty(Robot.SN)) {
            mHandler.sendEmptyMessage(SPLASH_SERVICE_GET_AD);
            return;
        }
        ServerFactory.createApi().getAdvertisementInfo(Robot.SN, new Observer<ResponseBean<AdvertisementInfoBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ResponseBean<AdvertisementInfoBean> beanResponseBean) {
                mVideoList.clear();
                mPictureList.clear();
                if (beanResponseBean != null
                        && beanResponseBean.getCode() == 200
                        && beanResponseBean.getRows() != null
                        && beanResponseBean.getRows().getStandbyAdv() != null) {
                    if (beanResponseBean.getRows().getStandbyAdv().getRobotResourceEntities() != null
                            && !beanResponseBean.getRows().getStandbyAdv().getRobotResourceEntities().isEmpty()) {
                        if (TextUtils.equals(beanResponseBean.getRows().getStandbyAdv().getAdvType(), "2")) {//图片
                            for (AdvertisementInfoBean.StandbyAdvBean.RobotResourceEntitiesBean bean : beanResponseBean.getRows().getStandbyAdv().getRobotResourceEntities()) {
                                mPictureList.add(bean.getUrl());
                            }
                        } else if (TextUtils.equals(beanResponseBean.getRows().getStandbyAdv().getAdvType(), "4")) {//视频
                            for (AdvertisementInfoBean.StandbyAdvBean.RobotResourceEntitiesBean bean
                                    : beanResponseBean.getRows().getStandbyAdv().getRobotResourceEntities()) {
                                for (String videoUrl : videoUrls) {
                                    if (bean.getUrl().endsWith(videoUrl)) {
                                        mVideoList.add(bean.getUrl());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (isShowing) {
                    getAdvertisementData();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                CsjlogProxy.getInstance().error("getAdvertisement onError" + e.getMessage());
                mHandler.sendEmptyMessage(SPLASH_SERVICE_GET_AD);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 显示广告
     */
    @SuppressLint("StringFormatMatches")
    private void show() {
        if (isShowing) {
            return;
        }
        if (Constants.sIsIntoOtherApp) {//如果在其他程序
            return;
        }
        String top = getTopActivity();
        if (TextUtils.isEmpty(top)) {
            return;
        }
        for (String activity : mNoShowActivity) {
            if (top.contains(activity)) {
                return;
            }
        }
        mIvLogo.setImageDrawable(null);
        if (new File(LOGO_PATH).exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(LOGO_PATH);
            mIvLogo.setImageBitmap(bmp);
        } else {
            mIvLogo.setImageResource(R.drawable.small_logo);
        }


        String defaultString = getString(R.string.splash_show);
        String robotName = "";

        String wakeupWords = "";//唤醒词
        if (BuildConfig.FLAVOR.contains("alice")) {
            wakeupWords = getString(R.string.hello_alice);
            robotName = getString(R.string.robot_name_alice);
        } else if (BuildConfig.FLAVOR.contains("amy")) {
            wakeupWords = getString(R.string.hello_amy);
            robotName = getString(R.string.robot_name_amy);
        } else if (BuildConfig.FLAVOR.contains("snow")) {
            wakeupWords = getString(R.string.hello_snow);
            robotName = getString(R.string.robot_name_snow);
        }

        if (Constants.greetContentBean != null) {
            if (!TextUtils.isEmpty(Constants.greetContentBean.getRobotName())) {
                robotName = Constants.greetContentBean.getRobotName();
            }
        }

        defaultString = String.format(Locale.getDefault(), defaultString, robotName);

        if (TextUtils.equals(Constants.Scene.CurrentScene, Constants.Scene.JiuDianScene)
                || TextUtils.equals(Constants.Scene.CurrentScene, "qiche")) {
            tvRobotName.setTextColor(Color.parseColor("#ffffff"));
        }


        String str = defaultString.substring(defaultString.lastIndexOf("\n"));
        SpannableString spannableString = new SpannableString(defaultString);
        spannableString.setSpan(new TextAppearanceSpan(mView.getContext(), R.style.splash_text_size), defaultString.length() - str.length(), defaultString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvRobotName.setText(spannableString);

        getAdvertisementData();
        mWindowManager.addView(mView, mLayoutParams);
        isShowing = true;
        EventBus.getDefault().post(new AdvertisementEvent(AdvertisementEvent.SHOW_HIDE_EVENT, true));
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, true));
        BRouter.toHome();

        try {
            stopService(new Intent(this, WeatherService.class));
            stopService(new Intent(this, GlobalNoticeService.class));
        } catch (Exception e) {
        }
    }

    private void dismiss() {
        if (!isShowing) {
            return;
        }
        if (showType == 1) {
            mVideoView.stopPlayback();
        } else if (showType == 2) {
            mBanner.stopAutoPlay();
        }
        mWindowManager.removeView(mView);
        isShowing = false;
        EventBus.getDefault().post(new AdvertisementEvent(AdvertisementEvent.SHOW_HIDE_EVENT, false));
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, false));
    }

    /**
     * 显示广告
     * 视频优先级最高
     * 其次是图片
     * 否则播放默认视频
     */
    private void getAdvertisementData() {
        if (!mVideoList.isEmpty()) {
            mVideoPosition = 0;
            showType = 1;
            mBanner.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
            startVideo(mVideoList.get(mVideoPosition));
        } else if (!mPictureList.isEmpty()) {
            showType = 2;
            mVideoView.setVisibility(View.GONE);
            mBanner.setVisibility(View.VISIBLE);
            mBanner.update(mPictureList);
        } else {
            showType = 1;
            mBanner.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.vertical_default));
        }
    }

    private void startVideo(String url) {
        if (mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }
        mVideoView.setVideoPath(getProxyUrl(url));
    }

    /**
     * 获取代理Url地址
     *
     * @param url 原url
     * @return 代理url
     */
    private String getProxyUrl(String url) {
        if (Constants.isI18N()) {
            return url;
        }
        return (mServer != null && !TextUtils.isEmpty(url)) ? mServer.getProxyUrl(URLDecoder.decode(url)) : "";
    }

    /**
     * 获得栈中最顶层的Activity
     *
     * @return
     */
    private String getTopActivity() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        assert manager != null;
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            return (runningTaskInfos.get(0).topActivity.getShortClassName());
        } else
            return null;
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void getAudioAction(AudioEvent event) {
        if (event != null) {
            switch (event.getAction()) {
                case UPDATE_DATA:
                    getAdvertisement();
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void startPatrol(PatrolActionEvent event) {
        if (event != null && !TextUtils.isEmpty(event.getAction())) {
            if (event.getAction().equals("1")) {
                mHandler.sendEmptyMessage(SPLASH_SERVICE_SHOW);
            } else if (event.getAction().equals("2")) {
                mHandler.sendEmptyMessage(SPLASH_SERVICE_DISMISS);
            }
        }
    }

    /**
     * 是否是大屏
     *
     * @return
     */
    protected boolean isPlus() {
        return Constants.isPlus();
    }

    @Override
    public void response(int state) {
        if (isShowing) {
            return;
        }
        if (state == 0) {
            if (Constants.sIsIntoOtherApp) {//如果在其他程序
                return;
            }
            mTimer.start();
            CsjlogProxy.getInstance().info("闪屏页-->综合检测:没人");
        } else if (state == 1) {
            mTimer.cancel();
            CsjlogProxy.getInstance().info("闪屏页-->综合检测:有人");
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mVideoList.isEmpty()) {//视频列表为空，播放默认视频
            mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.vertical_default));
            return;
        }
        ++mVideoPosition;
        if (mVideoPosition >= mVideoList.size()) {
            mVideoPosition = 0;
        }
        startVideo(mVideoList.get(mVideoPosition));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mVideoView.start();
    }

    @Override
    public void personInfo(String json) {

    }

    @Override
    public void personNear(boolean person) {
        if (person) {
            mHandler.sendEmptyMessage(SPLASH_SERVICE_DISMISS);
        }
    }

    @Override
    public void personList(String json) {

    }

    private OnWakeupListener mWakeupListener = new OnWakeupListener(){

        @Override
        public void response(int angle) {
            mHandler.sendEmptyMessage(SPLASH_SERVICE_DISMISS);

        }
    };

    /**
     * 摸头回调
     */
    @Override
    public void response() {
        mHandler.sendEmptyMessage(SPLASH_SERVICE_DISMISS);
    }
}
