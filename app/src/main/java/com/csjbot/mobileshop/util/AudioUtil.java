package com.csjbot.mobileshop.util;

import android.media.MediaPlayer;
import android.text.TextUtils;

/**
 * Created by jingwc on 2017/7/20.
 */

public class AudioUtil {

    private MediaPlayer mediaPlayer = null;

    public AudioUtil() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
    }

    public void play(String path, PlayCompleteListener playCompleteListener) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                //重用MediaPlayer对象
                mediaPlayer.reset();
                mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
                    if (playCompleteListener != null) {
                        playCompleteListener.complete();
                    }
                });
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void restart() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public interface PlayCompleteListener {
        void complete();
    }
}
