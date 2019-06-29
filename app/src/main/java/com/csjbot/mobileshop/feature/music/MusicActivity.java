package com.csjbot.mobileshop.feature.music;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.listener.OnSpeechGetResultListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.bean.MusicBean;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.widget.MusicView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;
import com.jakewharton.rxbinding2.view.RxView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by jingwc on 2017/10/16.
 */
@Route(path = BRouterPath.MUSIC)
public class MusicActivity extends BaseModuleActivity implements MusicContract.View {
    @BindView(R.id.music_view)
    MusicView music_view;
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

    private List<MusicBean> mMusics = new ArrayList<>();

    private int musicIndex;

    private int totalCount;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_music, R.layout.activity_vertical_music);
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
        music_view.hideCtrl();

        RxView.clicks(ibCtrl)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(view -> {
                    if (music_view.isPlaying()) {
                        music_view.pauseMusic();
                        ibCtrl.setBackgroundResource(R.drawable.play_btn);
                    } else {
                        music_view.restartMusic();
                        ibCtrl.setBackgroundResource(R.drawable.pause_btn);
                    }
                });
        RxView.clicks(ibNext)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> next());
        RxView.clicks(ibPrev)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> pre());

        IntentFilter wakeFilter = new IntentFilter(Constants.WAKE_UP);
        registerReceiver(wakeupReceiver, wakeFilter);

        mPresenter = new MusicPresenter();
        mPresenter.initView(this);

        new Thread(() -> {
            // 判断是否携带音乐数据而来
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String data = bundle.getString("data");
                if (!TextUtils.isEmpty(data)) {
                    mMusics = jsonToMusic(data);
                    if (mMusics != null) {
                        totalCount = mMusics.size();
                        runOnUiThread(() -> {
                            music_view.pauseMusic();
                            startPlay(musicIndex);
                        });

                    }
                }
            } else {
                RobotManager.getInstance().addListener((OnSpeechGetResultListener) json -> {
                    try {
                        String dataJson = new JSONObject(json).getJSONObject("result").getJSONObject("data").getJSONObject("data").toString();
                        onShowMessage(dataJson);
                    } catch (JSONException e) {
                        CsjlogProxy.getInstance().error(e.toString());
                    }
                });
                ServerFactory.getSpeechInstance().getResult("唱首歌");
            }
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
            music_view.pauseMusic();
            ibCtrl.setBackgroundResource(R.drawable.play_btn);
        } else if (text.contains(getString(R.string.resume))
                || text.contains(getString(R.string.start))) {
            music_view.restartMusic();
            ibCtrl.setBackgroundResource(R.drawable.pause_btn);
        }
        return true;
    }

    /**
     * 上一首
     */
    private void pre() {
        if ((musicIndex - 1) >= 0) {
            music_view.pauseMusic();
            startPlay(--musicIndex);
        }
    }

    /**
     * 下一首
     */
    private void next() {
        if (musicIndex < (totalCount - 1)) {
            music_view.pauseMusic();
            startPlay(++musicIndex);
        }
    }

    @Override
    protected void onShowMessage(String data) {
        super.onShowMessage(data);
        new Thread(() -> {
            musicIndex = 0;
            mMusics = jsonToMusic(data);
            if (mMusics != null) {
                totalCount = mMusics.size();
                runOnUiThread(() -> {
                    music_view.pauseMusic();
                    startPlay(musicIndex);
                });

            }
        }).start();


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

    public void startPlay(int index) {
        if (mMusics != null && mMusics.size() > 0) {
            MusicBean musicBean = mMusics.get(index);
            if (musicBean == null) {
                return;
            }
            if (!TextUtils.isEmpty(musicBean.getAlbumIcon())) {
                music_view.setBg(musicBean.getAlbumIcon());
            }
            tvSinger.setText(musicBean.getArtist());
            tvSing.setText(musicBean.getSongName());
            speak(getString(R.string.playing_music_name) + musicBean.getSongName() + "", new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {
                    mainHandler.post(() -> {
                        ibCtrl.setEnabled(false);
                        ibCtrl.setBackgroundResource(R.drawable.play_btn);
                    });
                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    if (!TextUtils.isEmpty(musicBean.getMusicUrl())) {
                        music_view.playMusic(musicBean.getMusicUrl(), () -> finish());
                        mainHandler.post(() -> {
                            ibCtrl.setEnabled(true);
                            ibCtrl.setBackgroundResource(R.drawable.pause_btn);
                        });
                    }
                }
            });
            // 日语tts目前没有播放完成检测,所以播报歌曲名称之后,延时去播放音乐
            if (CsjLanguage.isJapanese()) {
                mainHandler.postDelayed(() -> {
                    if (!TextUtils.isEmpty(musicBean.getMusicUrl())) {
                        music_view.playMusic(musicBean.getMusicUrl(), this::finish);
                    }
                }, 3000);
            }
        } else {
            speak(getString(R.string.no_music));
        }
    }

    public void release() {
        unregisterReceiver(wakeupReceiver);
        music_view.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        music_view.pauseMusic();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    private BroadcastReceiver wakeupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            music_view.pauseMusic();
        }
    };

}
