package com.csjbot.mobileshop.advertisement.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.VideoView;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.AudioEvent;
import com.csjbot.mobileshop.advertisement.event.AdvertisementEvent;
import com.csjbot.mobileshop.advertisement.factory.ProxyFactory;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.model.http.bean.rsp.AdvertisementInfoBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.widget.GlideImageLoader;
import com.csjbot.mobileshop.widget.MyBanner;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.Md5FileNameGenerator;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 大屏广告service
 *
 * @author jingwc
 * @date 2018/6/12 9:44
 */

public class AdvertisementService extends Service implements OnFaceListener {

    private HttpProxyCacheServer mServer;
    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams mParams;
    private View mLayout;
    private VideoView mVideoView;
    private MyBanner mBanner;
    private List<String> mVideos;
    private List<String> mPictures;
    private int mVideoIndex;
    private PlusBroadCast mPlusBroadCast;

    private MediaPlayer mPlayer;
    private boolean isMute = false;//是否静音
    private boolean isPerson = false;//是否有人在机器人旁

    private boolean isShowing = false;
    private boolean isShowPicture = false;
    /**
     * 常见视频格式
     */
    private String[] videoUrls = new String[]{".mp4", ".MP4", ".avi", ".AVI", ".wma", ".WMA", ".rmvb", ".RMVB", ".rm", ".RM", ".flash", ".FLASH", ".mid", ".MID", ".3gp", ".3GB", ".mov", ".MOV", ".mkv", ".MKV", ".mpg", ".MPG", ".flv", ".FLV"};
    /**
     * 广告是否被暂停
     */
    private boolean isPause = false;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mHandler.postDelayed(() -> getAdvertisement(), 10000);
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mVideos = new ArrayList<>();
        mPictures = new ArrayList<>();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mPlusBroadCast = new PlusBroadCast();
        RobotManager.getInstance().addListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(PLUS_VIDEO_SHOW);
        filter.addAction(PLUS_VIDEO_SHOW_MUTE);
        filter.addAction(PLUS_VIDEO_MIN_VOICE);
        filter.addAction(PLUS_VIDEO_MIN_MUTE);
        filter.addAction(PLUS_VIDEO_BACK);
        filter.addAction(PLUS_VIDEO_MUTE_ONLY);
        filter.addAction(PLUS_VIDEO_HIDE);
        registerReceiver(mPlusBroadCast, filter);
        createFloatWindow();
        mServer = ProxyFactory.getProxy(getApplicationContext());
        getAdvertisement();
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

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 99)
    public void getAdvertisementAction(AdvertisementEvent event) {
        if (event != null) {
            switch (event.getActionType()) {
                case AdvertisementEvent.SHOW_HIDE_EVENT:
                    if (event.isHide()) {
                        if (mVideoView.isPlaying()) {
                            mVideoView.pause();
                        }
                        mBanner.stopAutoPlay();
                        isPause = true;
                    } else {
                        isPause = false;
                        start();
                    }
                    break;
            }
        }
    }

    /**
     * 获取广告资源
     */
    private void getAdvertisement() {
        if (TextUtils.isEmpty(Robot.SN)) {
            mHandler.sendEmptyMessage(1);
            return;
        }

        ServerFactory.createApi().getAdvertisementInfo(Robot.SN, new Observer<ResponseBean<AdvertisementInfoBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ResponseBean<AdvertisementInfoBean> beanResponseBean) {
                mVideos.clear();
                mPictures.clear();
                if (beanResponseBean != null
                        && beanResponseBean.getCode() == 200
                        && beanResponseBean.getRows() != null
                        && beanResponseBean.getRows().getFunctionalAdv() != null) {
                    if (beanResponseBean.getRows().getFunctionalAdv().getRobotResourceEntities() != null
                            && !beanResponseBean.getRows().getFunctionalAdv().getRobotResourceEntities().isEmpty()) {
                        if (TextUtils.equals(beanResponseBean.getRows().getFunctionalAdv().getAdvType(), "2")) {//图片
                            for (AdvertisementInfoBean.FunctionalAdvBean.RobotResourceEntitiesBeanX bean : beanResponseBean.getRows().getFunctionalAdv().getRobotResourceEntities()) {
                                mPictures.add(bean.getUrl());
                            }
                        } else if (TextUtils.equals(beanResponseBean.getRows().getFunctionalAdv().getAdvType(), "4")) {//视频
                            for (AdvertisementInfoBean.FunctionalAdvBean.RobotResourceEntitiesBeanX bean
                                    : beanResponseBean.getRows().getFunctionalAdv().getRobotResourceEntities()) {
                                for (String videoUrl : videoUrls) {
                                    if (bean.getUrl().endsWith(videoUrl)) {
                                        mVideos.add(bean.getUrl());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                CsjlogProxy.getInstance().info("功能位广告: 图片大小：" + mPictures.size() + ",视频： " + mVideos.size());
                if (isShowing && !isPause) {
                    start();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getLog().info("getAdvertisement onError" + e.getMessage());
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private CsjlogProxy getLog() {
        return CsjlogProxy.getInstance();
    }

    public File getExternalCacheDir() {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, getPackageName()), "cache");
        return appCacheDir;
    }

    /**
     * 创建悬浮窗
     */
    @SuppressLint("InflateParams")
    private void createFloatWindow() {
        mParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //设置window type
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        mParams.gravity = Gravity.START;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mParams.x = 0;
        mParams.y = 1313;

        // 设置悬浮窗口长宽数据
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = 607;

        mLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_plus_advertisement, null);

        mVideoView = mLayout.findViewById(R.id.myVideoView);
        mBanner = mLayout.findViewById(R.id.banner);

        mVideoView.setOnPreparedListener(mp -> {
            mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            mPlayer = mp;
            setVolume();
            mVideoView.start();
        });

        mVideoView.setOnCompletionListener(mp -> {
            if (mVideos.isEmpty()) {
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.vertical_default);
                mVideoView.setVideoURI(uri);
                return;
            }
            mVideoIndex++;
            if (mVideoIndex >= mVideos.size()) {
                mVideoIndex = 0;
            }
            String filename = new Md5FileNameGenerator().generate(mVideos.get(mVideoIndex));
            File file = new File(getExternalCacheDir(),filename);
            if(file.exists()){
                if(file.length() > 0){
                    mVideoView.setVideoPath(getProxyUrl(mVideos.get(mVideoIndex)));
                }else{
                    Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.vertical_default);
                    mVideoView.setVideoURI(uri);
                }
            }else{
                mVideoView.setVideoPath(getProxyUrl(mVideos.get(mVideoIndex)));
            }
        });
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
    }

    /**
     * 开始播放广告
     * <p>
     * 优先级
     * 1、图片
     * 2、视频（没有就播放默认的）
     */
    private void start() {
        if (mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }
        mBanner.stopAutoPlay();
        if (!mPictures.isEmpty()) {
            isShowPicture = true;
            mLayout.setX(0);
            mLayout.setY(0);
            mVideoView.setVisibility(View.GONE);
            mBanner.setVisibility(View.VISIBLE);
            mBanner.update(mPictures);
            return;
        }
        mLayout.setX(0);
        mLayout.setY(1313);
        mBanner.setVisibility(View.GONE);
        mVideoView.setVisibility(View.VISIBLE);
        isShowPicture = false;
        if (!mVideos.isEmpty()) {
            String filename = new Md5FileNameGenerator().generate(mVideos.get(mVideoIndex));
            File file = new File(getExternalCacheDir(),filename);
            if(file.exists()){
                if(file.length() > 0){
                    mVideoView.setVideoPath(getProxyUrl(mVideos.get(mVideoIndex)));
                }else{
                    Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.vertical_default);
                    mVideoView.setVideoURI(uri);
                }
            }else{
                mVideoView.setVideoPath(getProxyUrl(mVideos.get(mVideoIndex)));
            }
            mVideoView.start();
        } else {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.vertical_default);
            mVideoView.setVideoURI(uri);
            mVideoView.start();
        }
    }

    /**
     * 获取代理Url地址
     *
     * @param url 原url
     * @return 代理url
     */
    private String getProxyUrl(String url) {
        return (mServer != null && !TextUtils.isEmpty(url)) ? mServer.getProxyUrl(URLDecoder.decode(url)) : "";
    }

    private synchronized void show(boolean mute) {
        isMute = mute;
        setVolume();
        if (!isShowing) {
            start();
            mWindowManager.addView(mLayout, mParams);
            isShowing = true;
        } else {//恢复原始大小
            if (isShowPicture) {
                mLayout.setX(0);
                mLayout.setY(0);
            } else {
                mLayout.setX(0);
                mLayout.setY(1313);
            }
            mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mParams.height = 607;

            mWindowManager.updateViewLayout(mLayout, mParams);
        }
    }

    private synchronized void hide() {
        if (isShowing) {
            if (mVideoView != null) {
                mVideoView.stopPlayback();
                mVideoView.suspend();
            }
            mBanner.stopAutoPlay();
            if (mWindowManager != null && mLayout != null) {
                mWindowManager.removeView(mLayout);
            }
            isShowing = false;
        }
    }

    /**
     * 布局缩小有声音
     */
    private synchronized void minAndVoice() {
        isMute = false;
        setVolume();
        if (isShowing) {
            if (isShowPicture) {
                mLayout.setX(0);
                mLayout.setY(0);
            } else {
                mLayout.setX(0);
                mLayout.setY(1313);
            }
            mParams.width = 100;
            mParams.height = 60;
            mWindowManager.updateViewLayout(mLayout, mParams);
        }
    }

    /**
     * 布局缩小静音
     */
    private synchronized void minAndMute() {
        if (isShowing) {
            if (isShowPicture) {
                mLayout.setX(0);
                mLayout.setY(0);
            } else {
                mLayout.setX(0);
                mLayout.setY(1313);
            }
            mParams.width = 100;
            mParams.height = 60;
            mWindowManager.updateViewLayout(mLayout, mParams);
        }
        mute();
    }

    /**
     * 设置音量
     */
    private synchronized void setVolume() {
        try {
            if (mPlayer != null) {
                if (isPerson || isMute) {
                    mPlayer.setVolume(0.0f, 0.0f);
                } else {
                    mPlayer.setVolume(1.0f, 1.0f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void mute() {
        if (mPlayer != null) {
            isMute = true;
            try {
                mPlayer.setVolume(0.0f, 0.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hide();
        unregisterReceiver(mPlusBroadCast);
        RobotManager.getInstance().removeListener(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public static final String PLUS_VIDEO_SHOW = "PLUS_VIDEO_SHOW";

    public static final String PLUS_VIDEO_SHOW_MUTE = "PLUS_VIDEO_SHOW_MUTE";

    public static final String PLUS_VIDEO_MIN_VOICE = "PLUS_VIDEO_MIN_VOICE";//缩小有声音

    public static final String PLUS_VIDEO_BACK = "PLUS_VIDEO_BACK";

    public static final String PLUS_VIDEO_MIN_MUTE = "PLUS_VIDEO_MIN_MUTE";//缩小没声音

    public static final String PLUS_VIDEO_HIDE = "PLUS_VIDEO_HIDE";//隐藏

    public static final String PLUS_VIDEO_MUTE_ONLY = "PLUS_VIDEO_MUTE_ONLY";//仅仅静音，不做其他变化

    @Override
    public void personInfo(String json) {

    }

    @Override
    public void personNear(boolean person) {
        isPerson = person;
        if (mPlayer == null) {
            return;
        }
        try {
            if (person || isMute) {
                mPlayer.setVolume(0.0f, 0.0f);
            } else {
                mPlayer.setVolume(1.0f, 1.0f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void personList(String json) {

    }

    class PlusBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, PLUS_VIDEO_SHOW)) {
                show(false);
            } else if (TextUtils.equals(action, PLUS_VIDEO_SHOW_MUTE)) {
                show(true);
            } else if (TextUtils.equals(action, PLUS_VIDEO_MIN_VOICE)) {
                minAndVoice();
            } else if (TextUtils.equals(action, PLUS_VIDEO_BACK)) {
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                ActivityManager.RunningTaskInfo info = activityManager.getRunningTasks(1).get(0);
                //获取当前activity栈中activity数量 及 栈顶activity类名，判断是否返回到主页
                if (TextUtils.equals(".Launcher", info.topActivity.getShortClassName()) && info.numActivities == 1) {
                    hide();
                }
            } else if (TextUtils.equals(action, PLUS_VIDEO_MIN_MUTE)) {
                minAndMute();
            } else if (TextUtils.equals(action, PLUS_VIDEO_HIDE)) {
                hide();
            } else if (TextUtils.equals(action, PLUS_VIDEO_MUTE_ONLY)) {
                mute();
            }
        }
    }

}
