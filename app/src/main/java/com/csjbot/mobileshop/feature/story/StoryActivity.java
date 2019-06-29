package com.csjbot.mobileshop.feature.story;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.model.tcp.tts.ISpeak;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.listener.OnSpeechGetResultListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.iflytek.cloud.SpeechError;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by jingwc on 2017/10/16.
 */

public class StoryActivity extends BaseModuleActivity {

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private List<Integer> list = Arrays.asList(0, 1, 2, 3);
    private int index = -1;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_story, R.layout.activity_vertical_story);
    }

    @Override
    public boolean isOpenChat() {
        return false;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        Collections.shuffle(list);//随机顺序
        tv_title.setMovementMethod(ScrollingMovementMethod.getInstance());
        IntentFilter wakeFilter = new IntentFilter(Constants.WAKE_UP);
        registerReceiver(wakeupReceiver, wakeFilter);

        RobotManager.getInstance().addListener((OnSpeechGetResultListener) json -> {
            try {
                String dataJson = new JSONObject(json).getJSONObject("result").getJSONObject("data").toString();
                runOnUiThread(() -> {
                    onShowMessage(dataJson);
                });
            } catch (JSONException e) {
                CsjlogProxy.getInstance().error(e.toString());
            }
        });

        // 判断是否携带音乐数据而来
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String data = bundle.getString("data");
            if (!TextUtils.isEmpty(data)) {
                showData(jsonToStory(data));
            }
        } else {
            ServerFactory.getSpeechInstance().getResult("讲个故事");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, true));
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_MUTE));
    }

    @Override
    protected boolean isInterruptSpeech() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wakeupReceiver);
    }

    public void showData(String[] contents) {
        if (contents == null || contents.length == 0) {
            return;
        }
        String answer = contents[0];
        String title = contents[1];
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(answer)) {
            tv_content.setText(answer);
            if (mSpeak.isSpeaking()) {
                mSpeak.stopSpeaking();
            }
            mSpeak.startSpeaking(answer, new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {

                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    if (!mHumanDialog.isShowing()) {
                        finish();
                    }
                }
            });
        }
    }

    private void pauseStory() {
        mSpeak.pauseSpeaking();
    }

    private void restartStroy() {
        mSpeak.resumeSpeaking();
    }


    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return true;
        }

        // 故事页面控制播放逻辑
        if (text.contains(getString(R.string.pause))
                || text.contains(getString(R.string.stop))) {
            pauseStory();
        } else if (text.contains(getString(R.string.resume))
                || text.contains(getString(R.string.start))) {
            restartStroy();
        }
        if (text.contains("换一个")
                || text.contains("换个故事")) {
            ServerFactory.getSpeechInstance().getResult("讲个故事");
        }
        return true;
    }

    @Override
    protected void onShowMessage(String data) {
        showData(jsonToStory(data));
    }

    private String[] jsonToStory(String data) {
        String story = "";
        String title = "";
        if (TextUtils.equals(Constants.Scene.CurrentScene, Constants.Scene.Dianli)) {
            index++;
            if (index > 3) {
                index = 0;
            }
            int i = list.get(index);
            title = StoryConstants.TITLES[i];
            story = StoryConstants.STORYS[i];
        } else {
            try {
                story = new JSONObject(data).getJSONObject("data").getString("answer");
                title = new JSONObject(data)
                        .getJSONObject("semantic")
                        .getJSONObject("slots")
                        .getJSONObject("NAME")
                        .getString("originalValue");
                if (!TextUtils.isEmpty(title)) {
                    if (title.equals("RANDOM_STORY")) {
                        title = null;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new String[]{story, title};
    }

    private BroadcastReceiver wakeupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseStory();
        }
    };
}
