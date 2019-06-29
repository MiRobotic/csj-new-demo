package com.csjbot.mobileshop.feature.entertainment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.ai.EntertainmentAI;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.feature.dance.DanceActivity;
import com.csjbot.mobileshop.feature.music.MusicActivity;
import com.csjbot.mobileshop.feature.music.MusicInternationalActivity;
import com.csjbot.mobileshop.feature.story.StoryActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.router.BRouterPath;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.OnClick;

/**
 * Created by jingwc on 2017/10/16.
 */

@Route(path = BRouterPath.ENTERTAINMENT)
public class EntertainmentActivity extends BaseModuleActivity implements EntertainmentContract.View {

    private EntertainmentContract.Presenter mPresenter;

    private EntertainmentAI mAI;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_entertainment, R.layout.activity_vertical_entertainment);
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }


    @Override
    public void init() {
        super.init();
        mPresenter = new EntertainmentPresenter();
        mPresenter.initView(this);

        mAI = EntertainmentAI.newInstance();
        mAI.initAI(this);

        if (CsjLanguage.CURRENT_LANGUAGE == CsjLanguage.CHINESE) {
            setRobotChatMsg("你可以点击屏幕上的按钮，或者通过语音对我说：\n" +
                    "1.讲个故事 \n" +
                    "2.跳个舞吧\n" +
                    "3. 唱首歌吧");
            speak("您可以通过语音对我说：讲个故事");
        } else {
//            String fmt = "%s\n1.%s\n2.%s\n3.%s";
//            String speak = String.format(Locale.getDefault(), fmt,
//                    getString(R.string.entertainment_init_speak),
//                    getString(R.string.tellstory),
//                    getString(R.string.dance),
//                    getString(R.string.sing)
//
//            );
            String speak = getString(R.string.entertainment_init_speak);

            speak(speak, true);
        }
    }

    @OnClick(R.id.ib_storytelling)
    public void storytelling() {
        if (Constants.isI18N()) {
            Bundle bundle = new Bundle();
            bundle.putString("EntertainmentType", "Story");
            jumpActivity(MusicInternationalActivity.class, bundle);
        } else {
            jumpActivity(StoryActivity.class);
        }


    }

    @OnClick(R.id.ib_dance)
    public void dance() {
        if (Constants.isI18N()) {
            Bundle bundle = new Bundle();
            bundle.putString("EntertainmentType", "Dance");
            jumpActivity(MusicInternationalActivity.class, bundle);
        } else {
            jumpActivity(DanceActivity.class);
        }

    }

    @OnClick(R.id.ib_music)
    public void music() {
        if (Constants.isI18N()) {
            Bundle bundle = new Bundle();
            bundle.putString("EntertainmentType", "Music");
            jumpActivity(MusicInternationalActivity.class, bundle);
        } else {
            jumpActivity(MusicActivity.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, false));
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        EntertainmentAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
            prattle(answerText);
        }
        return true;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void internationalSpeechMessage(String json) {
        JSONObject result = null;

        /*
         * {
            "result": {
                "text": "你也好 嘻嘻",
                "data": {
                    "code": 200,
                    "actionList": [],
                    "say": "你也好 嘻嘻",
                    "type": "satisfy",
                    "answer": "How do you do"
                },
                "error_code": 0
            }
         }
         */
        try {
            result = new JSONObject(json).getJSONObject("result");
            String answerText = result.getString("text");
            String userText = result.getJSONObject("data").getString("answer");
            userText = userText.toLowerCase();
            if (userText.contains(getString(R.string.tellstory).toLowerCase())) {
                Bundle bundle = new Bundle();
                bundle.putString("EntertainmentType", "Story");
                jumpActivity(MusicInternationalActivity.class, bundle);
                return;
            } else if (userText.contains(getString(R.string.dance).toLowerCase())) {
                Bundle bundle = new Bundle();
                bundle.putString("EntertainmentType", "Dance");
                jumpActivity(MusicInternationalActivity.class, bundle);
                return;
            } else if (userText.contains(getString(R.string.sing).toLowerCase())) {
                Bundle bundle = new Bundle();
                bundle.putString("EntertainmentType", "Music");
                jumpActivity(MusicInternationalActivity.class);
                return;
            }
            try {
                answerText = result.getJSONObject("answer").getString("answer_text");
            } catch (Exception e1) {
//                e1.printStackTrace();
            }

            if (answerText.contains("Google Assist")) {
                String robotName = "";

                if (BuildConfig.FLAVOR.contains("alice")) {
                    robotName = getString(R.string.robot_name_alice);
                } else if (BuildConfig.FLAVOR.contains("amy")) {
                    robotName = getString(R.string.robot_name_amy);
                } else if (BuildConfig.FLAVOR.contains("snow")) {
                    robotName = getString(R.string.robot_name_snow);
                }

                answerText = String.format(Locale.getDefault(), getString(R.string.my_name_is), robotName);
            }

            // 非日语和汉语不要出现中文
            if (!(CsjLanguage.isJapanese() || CsjLanguage.isChinese())) {
                Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                if (TextUtils.isEmpty(answerText) || p.matcher(answerText).find()) {
                    return;
                }
            }

            final String tempText = answerText;

            runOnUiThread(() -> {
                if (!mSpeak.isSpeaking()) {
                    speak(tempText);
                    setRobotChatMsg(tempText);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
