package com.csjbot.mobileshop.advertisement.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.factory.ProxyFactory;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.widget.GlideImageLoader;
import com.csjbot.mobileshop.widget.MyBanner;
import com.csjbot.mobileshop.widget.MyVideoView;
import com.csjbot.mobileshop.widget.chat_view.ChatVideoEvent;
import com.csjbot.mobileshop.widget.chat_view.adapter.ChatAdapter;
import com.danikula.videocache.HttpProxyCacheServer;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author ShenBen
 * @date 2018/12/4 13:12
 * @email 714081644@qq.com
 */

public class ChatService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;


    private View mView;
    private MyVideoView mVideoView;
    private MyBanner mBanner;

    private ImageView chat_video_load_gif;
    private ImageView iv_audio_detail;
    private ImageView cahat_view_loading_logo;

    private View loadingLayout;
    private ImageButton ib_close;

    private HttpProxyCacheServer mServer;
    private MediaPlayer mediaPlayer;

    private FrameLayout chat_view_frameLayout;

    private boolean isShowing;

    public static final String CHAT_SERVICE_SHOW = "CHAT_SERVICE_SHOW"; // chat view show


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mServer = ProxyFactory.getProxy(this);

        createFloatWindow();
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
        mView = inflate.inflate(R.layout.layout_chat_video_service_view, null);

        RelativeLayout mLayout = mView.findViewById(R.id.chat_video_relativelaout);
        mVideoView = mView.findViewById(R.id.chat_view_videw_view);
        mBanner = mView.findViewById(R.id.chat_view_banner);
        chat_video_load_gif = mView.findViewById(R.id.chat_video_load_gif);
        ib_close = mView.findViewById(R.id.ib_close);
        chat_view_frameLayout = mView.findViewById(R.id.chat_view_frameLayout);

        if (Constants.isPlus()) {
            ViewGroup.LayoutParams layoutParams = chat_view_frameLayout.getLayoutParams();
            layoutParams.width = 980;
            layoutParams.height = 620;
        }

        loadingLayout = mView.findViewById(R.id.chat_view_video_loading_layout);
        iv_audio_detail = mView.findViewById(R.id.iv_audio_detail);
        cahat_view_loading_logo = mView.findViewById(R.id.cahat_view_loading_logo);

        ib_close.setOnClickListener(v -> dismiss());

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

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
        mediaPlayer.setOnCompletionListener(mp -> dismiss());
    }

    private RequestListener requestListener = new RequestListener() {

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
            if (resource instanceof GifDrawable) {
                //加载一次
                ((GifDrawable) resource).setLoopCount(GifDrawable.LOOP_FOREVER);
            }
            return false;
        }

    };

    private void setLogoBySd(String filePath) {
        if (cahat_view_loading_logo != null) {
            cahat_view_loading_logo.setImageDrawable(null);
            if (new File(filePath).exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(filePath);
                cahat_view_loading_logo.setImageBitmap(bmp);
            } else {
                cahat_view_loading_logo.setImageResource(R.drawable.logo_only);
            }
        }
    }

    /**
     * 显示广告
     */
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

        mWindowManager.addView(mView, mLayoutParams);
        // 按道理来说，人在面前对话的话，广告会静音，但是为了防止意外，这里还是会强制广告静音
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MUTE_ONLY));


        Intent disCardListing = new Intent(ChatService.CHAT_SERVICE_SHOW);
        disCardListing.putExtra("isShowing", true);
        sendBroadcast(disCardListing);

        isShowing = true;
    }

    private void setAllViewGone() {
        mBanner.setVisibility(View.GONE);
        mVideoView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        iv_audio_detail.setVisibility(View.GONE);
    }

    private void dismiss() {
        if (!isShowing) {
            return;
        }


        mediaPlayer.reset();

        mWindowManager.removeView(mView);
        setAllViewGone();

        isShowing = false;

        Intent disCardListing = new Intent(CHAT_SERVICE_SHOW);
        disCardListing.putExtra("isShowing", false);
        sendBroadcast(disCardListing);

        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW));
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
    public void onChatEvent(ChatVideoEvent event) {
        if (event != null) {
            CsjlogProxy.getInstance().info("event.getEventType() " + event.getEventType());
            setAllViewGone();
            switch (event.getEventType()) {
                case ChatAdapter.ROBOT_CHAT_VIDEO: {
                    String url = (String) event.getObj();
                    setupVideo(url);
                    show();
                }
                break;
                case ChatAdapter.ROBOT_CHAT_PICTURE: {
                    List<String> pictures = (List<String>) event.getObj();
                    setupPicture(pictures);
                    CsjlogProxy.getInstance().info("event ChatAdapter.ROBOT_CHAT_PICTURE");
                    show();
                }
                break;
                case ChatAdapter.ROBOT_CHAT_AUDIO: {
                    String url = (String) event.getObj();
                    setupAudio(url);
                    show();
                }
                break;
                case ChatAdapter.ROBOT_CHAT_DISMISS:
                default:
                    dismiss();
                    break;
            }
        }
    }

    private void setupVideo(String url) {
        Glide.with(this).load(R.drawable.chat_video_load_gif).listener(requestListener).into(chat_video_load_gif);
        mVideoView.setVideoPath(getProxyUrl(url));
        mVideoView.setVisibility(View.VISIBLE);
        setLogoBySd(Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void setupPicture(List<String> pictures) {
        mBanner.isAutoPlay(true);
        mBanner.startAutoPlay();
        mBanner.update(pictures);

        mBanner.setVisibility(View.VISIBLE);
    }

    private void setupAudio(String url) {
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            dismiss();
            e.printStackTrace();
        }

        Glide.with(this).load(R.drawable.record_pic).listener(requestListener).into(iv_audio_detail);
        iv_audio_detail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mHandler.postDelayed(this::dismiss, 1000);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mVideoView.start();
        mHandler.postDelayed(() -> loadingLayout.setVisibility(View.GONE), 200);
    }

}
