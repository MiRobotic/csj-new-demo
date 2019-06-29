package com.csjbot.mobileshop.feature.dance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.tcp.body_action.IAction;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.router.BRouterPath;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jingwc on 2017/10/12.
 */

@Route(path = BRouterPath.DANCE)
public class DanceActivity extends BaseModuleActivity implements DanceContract.View {

    @BindView(R.id.music_view)
    ImageView music_view;

    MediaPlayer mediaPlayer = new MediaPlayer();

    List<String> songs = new ArrayList<>();

    IAction action;

    boolean isPause;

    boolean isUnavailable;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_dance, R.layout.activity_vertical_dance);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();

        songs.add("http://cdn.stormorai.com/qq/download/003uVtt403QZ0X.m4a");
        songs.add("http://cdn.stormorai.com/resource/music_bk/412/138785412.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/others/mID/003txpo62fRmID.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/others/DXl/000Q3vqG3GaDXl.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/37094168.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/519/135163519.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/352/135905352.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/others/bTl/0031MMo90FDbTl.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/993/31313993.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/740/132350740.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/31067128.mp3");
        songs.add("http://cdn.stormorai.com/qq/download/102950619.m4a");
        songs.add("http://cdn.stormorai.com/resource/music_bk/858/31530858.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/643/139191643.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/378/30100378.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/35525334.mp3");

        IntentFilter wakeFilter = new IntentFilter(Constants.WAKE_UP);
        registerReceiver(wakeupReceiver, wakeFilter);

        IntentFilter aFilter = new IntentFilter("com.example.BROADCAST");
        registerReceiver(batteryReceiver, aFilter);

        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DanceActivity.this.finish();
                    }
                }, 1000);
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });

        action = ServerFactory.getActionInstance();

        startDance();
    }

    private void startDance() {
        int random = (int) (Math.random() * songs.size()) + 1;
        if (random < 0 || random > (songs.size() - 1)) {
            random = 0;
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songs.get(random));
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Glide.with(this).load(R.drawable.record_pic).listener(new RequestListener() {

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

        }).into(music_view);


//        music_view.playMusic(songs.get(random), () -> {
//            DanceActivity.this.finish();
//        });

        isDiscard = true;
        action.startDance();
    }

    public void pauseDance() {
        mediaPlayer.pause();
//        music_view.pauseMusic();
        isDiscard = true;

        action.stopDance();
    }

    public void restartDance() {
        mediaPlayer.start();
//        music_view.restartMusic();
        isDiscard = true;
        action.startDance();
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        if (text.contains(getString(R.string.pause))
                || text.contains(getString(R.string.stop))
                || text.contains(getString(R.string.do_not_dance))
                || text.contains("不要跳了")) {
            pauseDance();
        } else if (text.contains(getString(R.string.resume))
                || text.contains(getString(R.string.start))) {
            restartDance();
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        pauseDance();
        isPause = true;

    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, true));
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_MUTE));
        if (isPause) {
            restartDance();
        }
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        isDiscard = true;
        CsjlogProxy.getInstance().error("isDiscard  onDestroy", isDiscard);
        release();
        super.onDestroy();
    }


    private void release() {
        unregisterReceiver(batteryReceiver);
        unregisterReceiver(wakeupReceiver);
//        music_view.release();
        action.stopDance();
        mediaPlayer.reset();
        mediaPlayer.release();
    }


    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseDance();
            DanceActivity.this.finish();
        }
    };

    private BroadcastReceiver wakeupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseDance();
        }
    };

    @Override
    public void networkAvailability() {
        super.networkAvailability();
        if (isUnavailable) {
            restartDance();
        }
        isUnavailable = false;
    }

    @Override
    public void networkUnavailable() {
        super.networkUnavailable();
        isUnavailable = true;
        pauseDance();
    }
}
