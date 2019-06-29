package com.csjbot.mobileshop.feature.music;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.bean.MusicBean;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.tcp.body_action.IAction;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;
import com.jakewharton.rxbinding2.view.RxView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class MusicInternationalActivity extends BaseModuleActivity implements MusicContract.View {
    @BindView(R.id.music_view)
    ImageView music_view;
    @BindView(R.id.tv_sing)
    TextView tvSing;
    @BindView(R.id.tv_singer)
    TextView tvSinger;
    @BindView(R.id.ib_ctrl)
    ImageButton ibCtrl;
    @BindView(R.id.ib_next)
    ImageButton ibNext;
    @BindView(R.id.ib_prev)
    ImageButton ibPrev;


    private MusicContract.Presenter mPresenter;

    private List<File> mMusics = new ArrayList<>();

    private int musicIndex = 0;

    private int totalCount;

    private IAction action = ServerFactory.getActionInstance();

    private MediaPlayer mediaPlayer = new MediaPlayer();


    private static final int ENTERTAINMENT_TYPE_STORY = 1;
    private static final int ENTERTAINMENT_TYPE_MUSIC = 2;
    private static final int ENTERTAINMENT_TYPE_DANCE = 3;

    private int EntertainmentType = ENTERTAINMENT_TYPE_MUSIC;


    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_music_i18n, R.layout.activity_vertical_music_i18n);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @SuppressLint("CheckResult")
    @Override
    public void init() {
        super.init();
        ibCtrl.setEnabled(false);
        ibCtrl.setBackgroundResource(R.drawable.play_btn);

        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (musicIndex == (mMusics.size() - 1)) {
                    MusicInternationalActivity.this.finish();
                    CsjlogProxy.getInstance().error("onCompletion ");
                } else {
                    next();
                }
            }
        });

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

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });


//        music_view.hideCtrl();

        RxView.clicks(ibCtrl)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(view -> {
//                    if (music_view.isPlaying()) {
//                        music_view.pauseMusic();
//                        ibCtrl.setBackgroundResource(R.drawable.play_btn);
//                    } else {
//                        music_view.restartMusic();
//                        ibCtrl.setBackgroundResource(R.drawable.pause_btn);
//                    }
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        ibCtrl.setBackgroundResource(R.drawable.play_btn);
                        if (EntertainmentType == ENTERTAINMENT_TYPE_DANCE) {
                            pauseDance();
                        }
                    } else {
                        mediaPlayer.start();
                        ibCtrl.setBackgroundResource(R.drawable.pause_btn);
                        if (EntertainmentType == ENTERTAINMENT_TYPE_DANCE) {
                            startDance();
                        }
                    }
                });
        RxView.clicks(ibNext)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> next());
        RxView.clicks(ibPrev)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> pre());

        IntentFilter wakeFilter = new IntentFilter(Constants.WAKE_UP);
        registerReceiver(wakeupReceiver, wakeFilter);

        mPresenter = new MusicPresenter();
        mPresenter.initView(this);

        String filePath;
        Bundle bundle = getIntent().getExtras();
        String type = bundle != null ? bundle.getString("EntertainmentType") : "Music";

        if (TextUtils.equals(type, "Music")) {
            EntertainmentType = ENTERTAINMENT_TYPE_MUSIC;
            filePath = Environment.getExternalStorageDirectory() + "/csjbot/music/";
        } else if (TextUtils.equals(type, "Story")) {
            EntertainmentType = ENTERTAINMENT_TYPE_STORY;
            filePath = Environment.getExternalStorageDirectory() + "/csjbot/story/";
        } else {
            EntertainmentType = ENTERTAINMENT_TYPE_DANCE;
            filePath = Environment.getExternalStorageDirectory() + "/csjbot/dance/";
        }

        new Thread(() -> {
            File file = new File(filePath);
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File song : files) {
                        if (song.getName().endsWith("mp3")
                                || song.getName().endsWith("MP3")
                                || song.getName().endsWith("mP3")
                                || song.getName().endsWith("Mp3")) {
                            mMusics.add(song);
                        }
                    }
//                    Collections.addAll(mMusics, file.listFiles());
                    totalCount = mMusics.size();
                    runOnUiThread(() -> {
//                        music_view.pauseMusic();
                        mediaPlayer.stop();
                        startPlay(musicIndex);
                    });
                } else {
                    MusicInternationalActivity.this.finish();
                }
            } else {
                file.mkdirs();
                MusicInternationalActivity.this.finish();
            }

//            if (EntertainmentType != ENTERTAINMENT_TYPE_MUSIC) {
//                try {
//                    Thread.sleep(2000);
//                    if (musicIndex == mMusics.size()) {
//                        CsjlogProxy.getInstance().error("onCompletion MusicInternationalActivity ");
//                        MusicInternationalActivity.this.finish();
//                    } else {
//                        mediaPlayer.reset();
//                        try {
//                            mediaPlayer.setDataSource(mMusics.get(musicIndex).getAbsolutePath());
//                            mediaPlayer.prepareAsync();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    mainHandler.post(() -> {
//                        ibCtrl.setEnabled(true);
//                        ibCtrl.setBackgroundResource(R.drawable.pause_btn);
//                    });
//
//                    startDance();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, true));
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_MUTE));
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }

        // 音乐页面控制播放逻辑
        if (text.contains(getString(R.string.next_music))
                || text.contains("下一首")
                || text.contains("换一首")
                || text.contains("换一个")) {
            next();
        } else if (text.contains(getString(R.string.previous_music))
                || text.contains("上一首")
                || text.contains("上一个")) {
            pre();
        } else if (text.contains(getString(R.string.pause))
                || text.contains(getString(R.string.stop))
                || text.contains("别唱了")) {
//            music_view.pauseMusic();
            mediaPlayer.pause();
            ibCtrl.setBackgroundResource(R.drawable.play_btn);
            pauseDance();
        } else if (text.contains(getString(R.string.resume))
                || text.contains(getString(R.string.start))) {
//            music_view.restartMusic();
            mediaPlayer.start();
            ibCtrl.setBackgroundResource(R.drawable.pause_btn);
        }
        return true;
    }

    private void startDance() {
        if (EntertainmentType == ENTERTAINMENT_TYPE_DANCE) {
            action.startDance();
        }
    }

    public void pauseDance() {
        action.stopDance();
        RobotManager.getInstance().robot.reqProxy.cancelNavi();
        mainHandler.postDelayed(() -> RobotManager.getInstance().robot.reqProxy.cancelNavi(), 200);
    }

    /**
     * 上一首
     */
    private void pre() {
        if ((musicIndex - 1) >= 0) {
//            music_view.pauseMusic();
            mediaPlayer.pause();
            startPlay(--musicIndex);
        }
    }

    /**
     * 下一首
     */
    private void next() {
        if (musicIndex < (totalCount - 1)) {
//            music_view.pauseMusic();
            mediaPlayer.pause();
            startPlay(++musicIndex);
        }
    }


    private List<MusicBean> jsonToMusic(String data) {
        String songs = "";
        try {
            songs = new JSONObject(data).getJSONArray("song_list").toString();
        } catch (JSONException e) {
            speak(R.string.no_music);
        }
        return new Gson().fromJson(songs, new TypeToken<List<MusicBean>>() {
        }.getType());
    }


    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public void startPlay(int index) {
        if (mMusics != null && mMusics.size() > 0) {
            File file = mMusics.get(index);
            if (file == null) {
                return;
            }

            tvSinger.setText("");
            String fileName = getFileNameNoEx(file.getName());
            tvSing.setText(fileName);

            speak(getString(R.string.playing_music_name) + fileName + "", new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {
                    mainHandler.post(() -> {
                        ibCtrl.setEnabled(false);
                        ibCtrl.setBackgroundResource(R.drawable.play_btn);
                    });
                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    CsjlogProxy.getInstance().error("onCompletion ");

                    if (musicIndex == mMusics.size()) {
                        CsjlogProxy.getInstance().error("onCompletion MusicInternationalActivity ");
                        MusicInternationalActivity.this.finish();
                    } else {
                        mediaPlayer.reset();
                        try {
                            mediaPlayer.setDataSource(mMusics.get(musicIndex).getAbsolutePath());
                            mediaPlayer.prepareAsync();

                            if (EntertainmentType == ENTERTAINMENT_TYPE_DANCE) {
                                startDance();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    mainHandler.post(() -> {
                        ibCtrl.setEnabled(true);
                        ibCtrl.setBackgroundResource(R.drawable.pause_btn);
                    });

                }
            });
//            mainHandler.postDelayed(() -> {
//                music_view.playMusic(file.getAbsolutePath(), this::finish);
//            }, 3000);
        } else {
            speak(getString(R.string.no_music));
        }
    }

    public void release() {
        unregisterReceiver(wakeupReceiver);
//        music_view.release();
        mediaPlayer.reset();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        music_view.pauseMusic();
        mediaPlayer.pause();
        if (EntertainmentType == ENTERTAINMENT_TYPE_DANCE) {
            pauseDance();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
        if (EntertainmentType == ENTERTAINMENT_TYPE_DANCE) {
            pauseDance();
        }
    }

    private BroadcastReceiver wakeupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            music_view.pauseMusic();
            mediaPlayer.pause();
            if (EntertainmentType == ENTERTAINMENT_TYPE_DANCE) {
                pauseDance();
            }
        }
    };

}
