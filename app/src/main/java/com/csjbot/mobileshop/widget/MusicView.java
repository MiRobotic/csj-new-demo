package com.csjbot.mobileshop.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.csjbot.mobileshop.GlideApp;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.util.AudioUtil;

/**
 * Created by jingwc on 2017/10/18.
 */

public class MusicView extends FrameLayout {

    Context mContext;

    ImageView ivBg;

    ImageView ivStart;

    ImageView ivPause;

    ObjectAnimator animator;

    AudioUtil audioUtil;

    boolean isPlay;
    private boolean isHide = false;

    public MusicView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MusicView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.layout_music, this, true);
        ivBg = findViewById(R.id.iv_bg);
        ivStart = findViewById(R.id.iv_start);
        ivPause = findViewById(R.id.iv_pause);
        ivStart.setOnClickListener(v -> {
            if (isPlay) {
                startAnim();
                audioUtil.restart();
            }
        });
        ivPause.setOnClickListener(v -> {
            if (isPlay) {
                pauseAnim();
                audioUtil.pause();
            }
        });

        animator = ObjectAnimator.ofFloat(ivBg, "rotation", 0f, 360f);
        animator.setDuration(6000);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);

        audioUtil = new AudioUtil();

    }

    public void setBg(String imageUrl) {
        GlideApp.with(this).load(imageUrl).error(R.drawable.music_def_bg).placeholder(R.drawable.music_def_bg).into(ivBg);
    }


    public void playMusic(String path) {
        if (!TextUtils.isEmpty(path)) {
            startAnim();
            isPlay = true;
            audioUtil.play(path, () -> {
                pauseAnim();
                isPlay = false;
            });
        }
    }

    public void playMusic(String path, OnMusicCompleteListener musicCompleteListener) {
        if (!TextUtils.isEmpty(path)) {
            startAnim();
            isPlay = true;
            audioUtil.play(path, () -> {
                musicCompleteListener.complete();
                pauseAnim();
                isPlay = false;
            });
        }
    }

    public void pauseMusic() {
        audioUtil.pause();
        pauseAnim();
    }

    public void restartMusic() {
        audioUtil.restart();
        startAnim();
    }

    public void release() {
        isPlay = false;
        audioUtil.release();
        releaseAnim();
    }

    void startAnim() {
        if (animator.isPaused()) {
            animator.resume();
        } else {
            animator.start();
        }
        if (!isHide) {
            ivPause.setVisibility(View.VISIBLE);
            ivStart.setVisibility(View.GONE);
        }
    }

    private void pauseAnim() {
        animator.pause();
        if (!isHide) {
            ivPause.setVisibility(View.GONE);
            ivStart.setVisibility(View.VISIBLE);
        }
    }

    private void releaseAnim() {
        animator.cancel();
        ivBg.clearAnimation();
    }

    public void hideCtrl() {
        ivPause.setVisibility(GONE);
        ivStart.setVisibility(GONE);
        isHide = true;
    }

    public boolean isPlaying() {
        return audioUtil.isPlaying();
    }

    public interface OnMusicCompleteListener {
        void complete();
    }
}
