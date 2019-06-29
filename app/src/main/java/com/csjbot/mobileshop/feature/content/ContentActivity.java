package com.csjbot.mobileshop.feature.content;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.feature.content.fragment.ContentLevelOneFragment;
import com.csjbot.mobileshop.feature.content.fragment.ContentLevelThreeFragment;
import com.csjbot.mobileshop.feature.content.fragment.ContentLevelTwoFragment;
import com.csjbot.mobileshop.feature.content.listener.SpeakMessageListener;
import com.csjbot.mobileshop.feature.content.listener.VoiceControlListener;
import com.csjbot.mobileshop.router.BRouterPath;

@Route(path = BRouterPath.CONTENT_ACTIVITY)
public class ContentActivity extends BaseModuleActivity implements SpeakMessageListener {
    public static final String CONTENT_LEVEL = "CONTENT_LEVEL";
    public static final String CONTENT_DATA = "CONTENT_DATA";
    public static final String CONTENT_WORD = "CONTENT_WORD";
    private String mData;
    private String mWord;
    private boolean isPause = false;
    private VoiceControlListener mListener;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (isPause) {
                        return;
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_content, ContentLevelOneFragment.newInstance(mData, mWord))
                            .commitAllowingStateLoss();
                    break;
                case 2:
                    if (isPause) {
                        return;
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_content, ContentLevelTwoFragment.newInstance(mData))
                            .commitAllowingStateLoss();
                    break;
                case 3:
                    if (isPause) {
                        return;
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_content, ContentLevelThreeFragment.newInstance(mData))
                            .commitAllowingStateLoss();
                    break;
                default:

                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_content, R.layout.activity_content_vertical);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
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
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof VoiceControlListener){
            mListener = (VoiceControlListener) fragment;
        }

    }

    @Override
    public void init() {
        super.init();
        Intent intent = getIntent();
        if (intent != null) {
            int level = intent.getIntExtra(CONTENT_LEVEL, 1);
            mData = intent.getStringExtra(CONTENT_DATA);
            mWord = intent.getStringExtra(CONTENT_WORD);
            mHandler.sendEmptyMessage(level);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mListener = null;
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        if (mListener != null) {
            if (mListener.voiceControl(text)) {
                return true;
            }
        }

        prattle(answerText);
        return true;
    }

    @Override
    public void speakMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (mSpeak.isSpeaking()) {
                mSpeak.stopSpeaking();
            }
            prattle(msg);
        }
    }
}
