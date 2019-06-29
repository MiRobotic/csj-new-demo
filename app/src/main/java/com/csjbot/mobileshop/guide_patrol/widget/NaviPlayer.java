package com.csjbot.mobileshop.guide_patrol.widget;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class NaviPlayer {

    private static NaviPlayer player;
    private static MediaPlayer mediaPlayer;
    private Context mContext;
    private int curIndex = 0;
    private OnPlayerListener onPlayerListener = null;

    public NaviPlayer(Context context) {
        this.mContext = context;
    }

    public static NaviPlayer getInstance(Context context) {
        if (null == player) {
            player = new NaviPlayer(context);
        }
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        return player;
    }

    /**
     * 单曲循环播放
     */
    public void play(String path) {
        try {
            mediaPlayer.reset();//reset mpMediaPlayer
            mediaPlayer.setDataSource(path);//输入路径
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (onPlayerListener != null) {
                        onPlayerListener.onComplete();
                    }
                    play(path);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            curIndex = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }

    public void stopPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void continuePlay() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(curIndex);
            mediaPlayer.start();
        }
    }

    public void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void setPlayerListener(OnPlayerListener listener) {
        onPlayerListener = listener;
    }

    public interface OnPlayerListener {
        void onComplete();
    }
}

