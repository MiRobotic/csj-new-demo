package com.csjbot.mobileshop.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.csjbot.mobileshop.R;

/**
 * Created by 孙秀艳 on 2017/10/19.
 * VideoView播放器
 */

public class CommonVideoView extends RelativeLayout implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private VideoView videoView;
    private ImageView imageView;
    private Context context;
    private MediaPlayer mediaPlayer;
    private Uri uri = null;
    private int seekTo = 0;
    private OnPlayerListener onPlayerListener = null;
    private boolean isFlag = true;//是否显示默认图片
    private Bitmap firstFrame;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public CommonVideoView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CommonVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommonVideoView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    private void setFirstFrameDrawable(final Uri url){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(url.getPath());
        firstFrame = mmr.getFrameAtTime();
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(firstFrame);
    }

    /**
     * 开始播放
     */
    public synchronized void startPlay(Uri url) {
        uri = url;
        try {
            if (isFlag) {
                setFirstFrameDrawable(url);
            }
            videoView.setVideoURI(url);
//            videoView.start();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    videoView.start();
                }
            }, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (onPlayerListener != null) {
            onPlayerListener.onComplete();
        }
        try {
            mediaPlayer.reset();
            startPlay(uri);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        mediaPlayer = mp;
        mp.start();
        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // video started
                    isFlag = false;
                    imageView.setVisibility(GONE);
                    videoView.setBackgroundColor(Color.TRANSPARENT);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.common_video_view, null);
        videoView = (VideoView) view.findViewById(R.id.videoView);
        imageView = view.findViewById(R.id.iv_default);

        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);

        //注册在设置或播放过程中发生错误时调用的回调函数。如果未指定回调函数，或回调函数返回false，VideoView 会通知用户发生了错误。
        videoView.setOnErrorListener(this);
        addView(view);
    }

    /**
     * 暂停
     */
    public void pausePlay() {
        if (videoView != null && videoView.isPlaying()) {
            seekTo = videoView.getCurrentPosition();
            videoView.pause();
        }
    }

    public void continuePlay() {
        try {
            videoView.seekTo(seekTo);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    videoView.start();
                }
            }, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPlay() {
        isFlag = true;
        videoView.stopPlayback();
        videoView.setVideoURI(null);
    }

    public void setPlayerListener (OnPlayerListener listener) {
        onPlayerListener = listener;
    }

    public interface OnPlayerListener {
        void onComplete();
    }
}
