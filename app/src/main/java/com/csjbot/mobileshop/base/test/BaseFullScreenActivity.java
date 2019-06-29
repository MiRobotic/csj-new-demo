package com.csjbot.mobileshop.base.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.android.arouter.launcher.ARouter;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.REQConstants;
import com.csjbot.coshandler.listener.OnCustomerStateListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.listener.OnSpeechErrorListener;
import com.csjbot.coshandler.listener.OnWakeupListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.AdvertisementEvent;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.advertisement.service.ChatService;
import com.csjbot.mobileshop.base.BaseActivity;
import com.csjbot.mobileshop.bean.ExpressionControlBean;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.customer_service.CustomerServer2pageEvent;
import com.csjbot.mobileshop.customer_service.Page2customerServerEvent;
import com.csjbot.mobileshop.customer_service.RepeatHeatEvent;
import com.csjbot.mobileshop.feature.content.ContentActivity;
import com.csjbot.mobileshop.feature.customer.FloatWindowCustomerEvent;
import com.csjbot.mobileshop.feature.customer.FloatingWdCustomerService;
import com.csjbot.mobileshop.feature.customer.OpenCustomerDialogEnvent;
import com.csjbot.mobileshop.feature.dance.DanceActivity;
import com.csjbot.mobileshop.feature.entertainment.EntertainmentActivity;
import com.csjbot.mobileshop.feature.media.MediaPublicizeActivity;
import com.csjbot.mobileshop.feature.music.MusicActivity;
import com.csjbot.mobileshop.feature.navigation.NaviActivity;
import com.csjbot.mobileshop.feature.navigation.setting.NaviSettingActivity;
import com.csjbot.mobileshop.feature.nearbyservice.NearByActivity;
import com.csjbot.mobileshop.feature.news.NewsActivity;
import com.csjbot.mobileshop.feature.payment.PaymentGuideActivity;
import com.csjbot.mobileshop.feature.product.ProductDetailsActivity;
import com.csjbot.mobileshop.feature.product.ProductListActivity;
import com.csjbot.mobileshop.feature.product.ProductTypeActivity;
import com.csjbot.mobileshop.feature.settings.SettingsActivity;
import com.csjbot.mobileshop.feature.settings.network.SettingsNetworkActivity;
import com.csjbot.mobileshop.feature.story.StoryActivity;
import com.csjbot.mobileshop.feature.vipcenter.VipCenterActivity;
import com.csjbot.mobileshop.feature.visit.invite.InviteVisitorActivity;
import com.csjbot.mobileshop.feature.visit.temp.TempVisitorActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.guide_patrol.patrol.patrol_activity.PatrolActivity;
import com.csjbot.mobileshop.home.AllSceneHomeActivity;
import com.csjbot.mobileshop.model.http.bean.rsp.GoodsDetailBean;
import com.csjbot.mobileshop.model.http.bean.rsp.QrCodeBean;
import com.csjbot.mobileshop.model.http.bean.rsp.SensitiveWordsBean;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.model.tcp.tts.ISpeak;
import com.csjbot.mobileshop.network.NetworkConstants;
import com.csjbot.mobileshop.router.BRouter;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.speechrule.ChatControl;
import com.csjbot.mobileshop.util.BlackgagaLogger;
import com.csjbot.mobileshop.util.EnglishChatHandle;
import com.csjbot.mobileshop.util.FileUtil;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.NumberUtils;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.mobileshop.util.SpeakUtil;
import com.csjbot.mobileshop.util.VolumeUtil;
import com.csjbot.mobileshop.widget.CustomerServiceDialog;
import com.csjbot.mobileshop.widget.NewRetailDialog;
import com.csjbot.mobileshop.widget.TitleView;
import com.csjbot.mobileshop.widget.chat_view.ChatVideoEvent;
import com.csjbot.mobileshop.widget.chat_view.ChatView;
import com.csjbot.mobileshop.widget.chat_view.adapter.ChatAdapter;
import com.csjbot.mobileshop.widget.chat_view.bean.ChatBean;
import com.csjbot.mobileshop.widget.chat_view.bean.GraphicAudioBean;
import com.csjbot.mobileshop.widget.chat_view.bean.GraphicBean;
import com.csjbot.mobileshop.widget.chat_view.bean.GraphicHyperlinkBean;
import com.csjbot.mobileshop.widget.chat_view.bean.GraphicPictureBean;
import com.csjbot.mobileshop.widget.chat_view.bean.GraphicVideoBean;
import com.csjbot.mobileshop.widget.chat_view.bean.RawDataChatBean;
import com.csjbot.mobileshop.widget.language.LanguageWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

/**
 * Created by xiasuhuei321 on 2017/8/11.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public abstract class BaseFullScreenActivity extends BaseActivity {
    protected final String TAG = this.getClass().getSimpleName();

    protected TitleView mTitleView;
    private ChatView mChatView;
    protected int mMode;
    protected ISpeak mSpeak;
    protected Activity context;

    private NetworkReceiver receiver;

    protected boolean isResume = false;

    private boolean chatServicePopUp = false;

    protected boolean isPause = false;

    private NewRetailDialog mDialog;

    protected CustomerServiceDialog mHumanDialog;

    private CsjlogProxy mLogProxy;

    private ChatControl mChatControl;

    protected boolean openNoNetworkDialog = true;

    protected volatile String userText;

    protected volatile boolean isDiscard;

    protected volatile boolean isSensitiveWords;

    protected Handler mainHandler = new Handler(Looper.getMainLooper());

    protected volatile boolean isChatting = false;

    protected String answerType = "chat";

    private boolean isCanChat = true;

    protected boolean isFloatWd;


    /**
     * 悬浮窗X坐标
     */
    private static int FLOATWINDOW_POSITION_X = 0;
    /**
     * 悬浮窗Y坐标
     */
    private static int FLOATWINDOW_POSITION_Y = 0;


    //TODO
    private OnCustomerStateListener mOnCustomerStateListener = (type, state) -> {

        switch (type) {//控制ID

            case 0://连接成功
                Constants.sIsCustomerService = true;
                Constants.isNeedComment = true;
                if (mHumanDialog != null) {
                    mHumanDialog.CloseTimeOut();  //关闭超时
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mHumanDialog.setConnnectStatus(Constants.ISCONNECTED);
                            mHumanDialog.changeDialogStatus();
                            //TODO
                            EventBus.getDefault().post(new Page2customerServerEvent()); //通过服务里发送心跳
                        }
                    });
                }
                break;

            case 1:    // 后台或主动挂断
                if (!Constants.sIsCustomerService) {
                    //TODO return 之前
                    if (mHumanDialog.isShowing()) {
                        mHumanDialog.CloseTimeOut();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mHumanDialog.setConnnectStatus(Constants.DISCONNCTED);
                                mHumanDialog.changeDialogStatus();
                            }
                        });
                    }
                    return;
                }
                Constants.sIsCustomerService = false;
                EventBus.getDefault().post(new RepeatHeatEvent(true));  //结束心跳
                if (!mHumanDialog.isShowing()) {
                    EventBus.getDefault().post(new FloatWindowCustomerEvent(false)); //隐藏悬浮窗
                    //显示通话窗口
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mHumanDialog.show();
                            mHumanDialog.setConnnectStatus(Constants.DISCONNCTED);
                            mHumanDialog.changeDialogStatus();
                        }
                    });

                } else {
                    if (FloatingWdCustomerService.sIsShow)    //隐藏悬浮窗
                        EventBus.getDefault().post(new FloatWindowCustomerEvent(false));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mHumanDialog.setConnnectStatus(Constants.DISCONNCTED);
                            mHumanDialog.changeDialogStatus();
                        }
                    });
                }


                break;

            case 2://心跳
                if (!isResume) return;
                if (state == 200) {
                    //TODO
                    EventBus.getDefault().post(new RepeatHeatEvent(false)); //从服务里发送心跳
                }
                break;

        }
    };

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        isFloatWd = FloatingWdCustomerService.sIsShow;
        context = this;
        //隐藏标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

//        CSJToast.showToast(this, "SDK INT " + android.os.Build.VERSION.SDK_INT, 2000);

        // 如果是国外的版本，则不需要这个
        if (!Constants.isI18N()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        }

        beforeSetContentView();
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        init();
        if (TextUtils.equals(Constants.Scene.CurrentScene, Constants.Scene.Fuzhuang)) {
            addSpeechErrorListener();
        }
        addWakeupListener();
        afterViewCreated(savedInstanceState);
    }

    protected void beforeSetContentView() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
        addSpeechListener();
        refreshLogo();

        if (mHumanDialog.isShowing()) {
            mHumanDialog.dismiss();
        }

        if (isOpenChat()) {
            if (getChatView() == null) {
                return;
            }
            if (Constants.isOpenChatView) {
                getChatView().setVisibility(View.VISIBLE);
            } else {
                getChatView().setVisibility(View.GONE);
            }
        }
    }

    private volatile long preDateTime = 0;

    private void addSpeechErrorListener() {
        RobotManager.getInstance().addListener((OnSpeechErrorListener) () -> {
            if (preDateTime > 0) {
                if (System.currentTimeMillis() > (preDateTime + 1000 * 60 * 10)) {
                    runOnUiThread(() -> prattle("网络正在开小差,请稍后"));
                    preDateTime = System.currentTimeMillis();
                }
            } else {
                runOnUiThread(() -> prattle("网络正在开小差,请稍后"));
                preDateTime = System.currentTimeMillis();
            }

        });
    }

    private void addWakeupListener() {
        RobotManager.getInstance().addListener(mWakeupListener);
    }

    private OnWakeupListener mWakeupListener = angle -> {
        if (!isResume || Constants.sIsIntoOtherApp) {
            return;
        }
        boolean wakeupStop = SharedPreUtil.getBoolean(SharedKey.WAKEUP_STOP, SharedKey.WAKEUP_STOP, false);
        if (!wakeupStop) {
            return;
        }
        if (this instanceof AllSceneHomeActivity
                || this instanceof ContentActivity
                || this instanceof PaymentGuideActivity
                || this instanceof MediaPublicizeActivity
                || this instanceof EntertainmentActivity
                || this instanceof NearByActivity
                || this instanceof ProductTypeActivity
                || this instanceof ProductListActivity
                || this instanceof ProductDetailsActivity) {
            runOnUiThread(() -> setRobotChatMsg(getString(R.string.hello) + getString(R.string.may_i_help_you)));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(updateLogoReceiver);
        unregisterReceiver(chatViewBroadcastReceiver);

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        RobotManager.getInstance().removeListener(mWakeupListener);

        //人工客服资源释放
//        EventBus.getDefault().post(new FloatWindowCustomerEvent(false));
//        EventBus.getDefault().post(new RepeatHeatEvent(true));
        if (mHumanDialog != null) {
            if (mHumanDialog.isShowing()) {
                mHumanDialog.dismiss();
                mHumanDialog = null;
            }

        }


    }

    public abstract int getLayoutId();

    protected int getCorrectLayoutId(@LayoutRes int defaultLayoutId, @LayoutRes int verticalLayoutId) {
        return isPlus() ? verticalLayoutId : defaultLayoutId;
    }

    /**
     * 是否是大屏
     *
     * @return
     */
    protected boolean isPlus() {
        return Constants.isPlus();
    }

    /**
     * 是否启用聊天窗口
     *
     * @return
     */
    public boolean isOpenChat() {
        return false;
    }

    /**
     * 是否启用Title
     *
     * @return
     */
    public boolean isOpenTitle() {
        return false;
    }

    /**
     * 不强制要求子类实现这个方法，你可以不初始化
     */
    public void init() {
        initTitle();
        initChat();
        initSpeak();

        receiver = new NetworkReceiver();
        IntentFilter intentFilter = new IntentFilter(NetworkConstants.SEND_ACTION);
        registerReceiver(receiver, intentFilter);
        IntentFilter logoFilter = new IntentFilter(Constants.UPDATE_LOGO);
        registerReceiver(updateLogoReceiver, logoFilter);

        registerReceiver(chatViewBroadcastReceiver, new IntentFilter(ChatService.CHAT_SERVICE_SHOW));

        initWelcomeSpeakText();

        initDialog();

        if (mLogProxy == null) {
            mLogProxy = CsjlogProxy.getInstance();
            mLogProxy.initLog(getApplicationContext());
        }

        mChatControl = ChatControl.newInstance(this);
        String initText = mChatControl.initChild();
        if (!TextUtils.isEmpty(initText)) {
            prattle(initText);
        }

    }

    public ChatControl getChatControl() {
        return mChatControl;
    }

    public CsjlogProxy getLog() {
        return mLogProxy;
    }

    private void initDialog() {
        mDialog = new NewRetailDialog(this);

        //=========================   add   =========================
        mHumanDialog = new CustomerServiceDialog(this);
        //=========================   add   =========================
    }

    //=========================   add   =========================
    protected void showCustomerServiceDialog(CustomerServiceDialog.OnCustomerDialogClickListener listener) {
        if (mHumanDialog != null) {
            mHumanDialog.setListener(listener);
            mHumanDialog.show();
            mHumanDialog.reqConnect();
        }
    }

    protected void dismissCustomerServiceDialog() {
        if (mHumanDialog != null) {
            mHumanDialog.dismiss();
        }
    }
    //=========================   add   =========================


    protected void showNewRetailDialog(String title, String text, NewRetailDialog.OnDialogClickListener listener) {
        if (mDialog != null) {
            mDialog.setTitle(title);
            mDialog.setContent(text);
            mDialog.setListener(listener);
            mDialog.show();
        }
    }

    protected void showNewRetailDialog(int title, int text, NewRetailDialog.OnDialogClickListener listener) {
        if (mDialog != null) {
            mDialog.setTitle(title);
            mDialog.setContent(text);
            mDialog.setListener(listener);
            mDialog.show();
        }
    }

    protected void showNewRetailDialog(String title, String text, String yes, String no, NewRetailDialog.OnDialogClickListener listener) {
        if (mDialog != null) {
            mDialog.setTitle(title);
            mDialog.setContent(text);
            mDialog.setYes(yes);
            mDialog.setNo(no);
            mDialog.setListener(listener);
            mDialog.show();
        }
    }

    protected void showNewRetailDialog(int title, int text, int yes, int no, NewRetailDialog.OnDialogClickListener listener) {
        if (mDialog != null) {
            mDialog.setTitle(title);
            mDialog.setContent(text);
            mDialog.setYes(yes);
            mDialog.setNo(no);
            mDialog.setListener(listener);
            mDialog.show();
        }
    }

    protected void dismissNewRetailDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    protected NewRetailDialog getDialog() {
        return mDialog;
    }

    void initWelcomeSpeakText() {
        CharSequence text = "";
        switch (CsjLanguage.CURRENT_LANGUAGE) {
            case CsjLanguage.CHINESE:
                text = initChineseSpeakText();
                break;
            case CsjLanguage.ENGELISH_US:
            case CsjLanguage.ENGELISH_UK:
            case CsjLanguage.ENGELISH_AUSTRALIA:
            case CsjLanguage.ENGELISH_INDIA:
                text = initEnglishSpeakText();
                break;
            case CsjLanguage.JAPANESE:
                text = initJapanSpeakText();
                break;
            default:
                break;
        }
        if (TextUtils.isEmpty(text)) {
            return;
        }
        speak(text.toString());
        setRobotChatMsg(text);
    }

    protected CharSequence initChineseSpeakText() {
        return null;
    }

    protected CharSequence initEnglishSpeakText() {
        return null;
    }

    protected CharSequence initJapanSpeakText() {
        return null;
    }

    protected boolean isDisableEntertainment() {
        return false;
    }

    protected boolean isHideUserText() {
        return false;
    }

    protected boolean isCanChat() {
        return isCanChat;
    }


    /**
     * 由子类重写该方法,主要用于关键字定向打断(比如产品关键字可打断对话)
     *
     * @param text
     * @return
     */
    protected boolean keywordInterrupt(String text) {

        return false;
    }

    private String cuteAnswers[] = new String[]{
            "Sorry, I can't get directions there.",
            "What, again?",
            "That's what I figured.",
            "I am racking my brains~",
            "Sorry, I am still a baby learning something. That is out of capacity.",
    };

    private Runnable timeoutMsg = () -> runOnUiThread(() -> setRobotChatMsg("请不要走开，爱丽丝还在思考中"));


    /**
     * 添加语音监听事件
     */
    private void addSpeechListener() {
        RobotManager.getInstance().setCustomerStateListener(mOnCustomerStateListener);
        RobotManager.getInstance().addListener((json, type) -> {
            CsjlogProxy.getInstance().error("isResume is {}, isCanChatis is {}, Constants.sIsIntoOtherApp is {}", isResume, isCanChat(), Constants.sIsIntoOtherApp);
            if (!isResume || !isCanChat() || Constants.sIsIntoOtherApp) {//不能聊天直接退出或者当前界面不可见也不能聊天
                return;
            }

            if (chatServicePopUp) {
                return;
            }
            switch (type) {
                case Robot.SPEECH_ASR_ONLY:
                    try {
                        String tempText = new JSONObject(json).getString("text");
                        tempText = sensitiveWordsFilter(tempText);
                        String text = tempText;
                        userText = text;

                        if (isInterruptSpeech()) {
                            isDiscard = false;
                            onRecognitiontext(text);
                            if (Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)) {
                                mainHandler.postDelayed(timeoutMsg, 3000);
                            }
                            runOnUiThread(() -> {
                                if (!isHideUserText()) {
                                    //消息上传至人工客服
                                    setCustomerChatMsg(text);
                                }
                            });
                        } else {
                            if (!mSpeak.isSpeaking()) {
                                isDiscard = false;
                                onRecognitiontext(text);
                                if (Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)) {
                                    mainHandler.postDelayed(timeoutMsg, 3000);
                                }
                                runOnUiThread(() -> {
                                    if (!isHideUserText()) {
                                        //消息上传至人工客服

                                        setCustomerChatMsg(text);
                                    }
                                });
                            } else {
                                if (keywordInterrupt(text)) {
                                    isDiscard = false;
                                    onRecognitiontext(text);
                                    if (Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)) {
                                        mainHandler.postDelayed(timeoutMsg, 3000);
                                    }
                                } else {
                                    isDiscard = true;
                                    CsjlogProxy.getInstance().error("isDiscard  keywordInterrupt " + text);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CsjlogProxy.getInstance().error("isDiscard  {}", isDiscard);

                    break;
                case Robot.SPEECH_LAST_RESULT:
                    CsjlogProxy.getInstance().error("isResume  SPEECH_LAST_RESULT ");
                    if (isDiscard) {
                        CsjlogProxy.getInstance().error("isResume  isDiscard return");
                        return;
                    }

                    // 敏感词
                    if (isSensitiveWords) {
                        CsjlogProxy.getInstance().error("isResume  isSensitiveWords  return");

                        prattle(getString(R.string.not_good_topic));
                        isSensitiveWords = false;
                        return;
                    }

                    if (Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)) {
                        mainHandler.removeCallbacks(timeoutMsg);
                    }

                    // 如果是国际化就走国际化的流程
                    if (Constants.isI18N()) {
                        internationalSpeechMessage(json);
                        return;
                    }
                    CsjlogProxy.getInstance().info("SPEECH_LAST_RESULT " + json);
                    /* V3.0 底层上传的东西
                    {
                        "msg_id": "SPEECH_ISR_LAST_RESULT_NTF",
                        "result": {
                            "data": {
                                "actionList": [],
                                "answer": "我想吃红烧肉",
                                "graphic": "{\"type\":\"2\",\"answer\":\"我想吃红烧肉\",\"imgFile\":[{\"url\":\"http://csjbot-test.su.bcebos.com/mkYycGynG8KsP5SwBn7EKwHwfy7YRxdHmfXaTKmM.jpg\"},{\"url\":\"http://csjbot-test.su.bcebos.com/MFxpxFMmS6Q4FEsEPymmxkNJHd5RyCWZPmrbYJB3.jpg\"}]}",
                                "say": "我想吃红烧肉",
                                "type": "satisfy"
                            },
                            "error_code": 0,
                            "text": "我想吃红烧肉。"
                        }
                    }
                     */
                    /**
                     * json -> RawDataChatBean -> ChatSectionMultipleItemBean -> UI
                     */
                    // 预处理，如果有语义，则走语义，没有做本地处理
                    RawDataChatBean rawDataChatBean = GsonUtils.jsonToObject(json, RawDataChatBean.class);
                    if (rawDataChatBean != null) {
                        if (rawDataChatBean.getResult() != null) {
                            if (rawDataChatBean.getResult().getData() != null) {
                                RawDataChatBean.ResultBean.DataBean bean = rawDataChatBean.getResult().getData();
                                switch (bean.getType()) {
                                    case "satisfy":
                                        if (!(this instanceof VipCenterActivity || this instanceof InviteVisitorActivity || this instanceof TempVisitorActivity)) {
                                            dealSatisfyMessage(bean);
                                        } else {
                                            dealIntent(getIntentType(json), json);
                                        }
                                        return;
                                    case "guide":
                                        /*
                                        {
                                            "msg_id": "SPEECH_ISR_LAST_RESULT_NTF",
                                            "result": {
                                                "data": {
                                                    "actionList": ["1、袁泉帅不帅"],
                                                    "answer": "您要问的是以下哪个问题？1、袁泉帅不帅",
                                                    "say": "您要问的是以下哪个问题？1、袁泉帅不帅",
                                                    "type": "guide"
                                                },
                                                "error_code": 0,
                                                "text": "我特喜欢吃红烧肉，袁泉帅不帅？"
                                            }
                                        }
                                         */
                                        // 引导话术等下
                                    default:
                                        dealIntent(getIntentType(json), json);
                                        return;
                                }
                            } else {
                                CsjlogProxy.getInstance().error("rawDataChatBean.getResult().getData() = null");
                            }
                        } else {
                            CsjlogProxy.getInstance().error("rawDataChatBean.getResult() = null");
                        }
                    } else {
                        CsjlogProxy.getInstance().error("rawDataChatBean = null");
                    }

                    dealIntent(getIntentType(json), json);
                    break;
                case Robot.SPEECH_CUSTOMER_SERVICE:
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(json);
                        String msgId = jsonObject.getString("msg_id");
                        String text = jsonObject.getString("text");
                        if (text.contains("this way")) {
                            text = getString(R.string.customer_this_way);
                        }

                        String tmpText = text;
                        runOnUiThread(() -> {

                            if (!mSpeak.isSpeaking()) {
                                speak(tmpText);
                                setRobotChatMsg(tmpText);
                            }
                        });
                    } catch (JSONException e) {
                        CsjlogProxy.getInstance().error("SPEECH_CUSTOMER_SERVICE " + e.toString());
                    }
                    break;
                default:
                    break;
            }
        });

    }

    private void dealSatisfyMessage(RawDataChatBean.ResultBean.DataBean bean) {
        String graphic = bean.getGraphic();
        CsjlogProxy.getInstance().error("graphic = " + graphic);
        GraphicBean graphicBean = GsonUtils.jsonToObject(graphic, GraphicBean.class);

        if (TextUtils.isEmpty(graphic)) {
            speak(bean.getSay());
            setRobotChatMsg(bean.getSay());
            CsjlogProxy.getInstance().error("speak " + bean.getSay());
            return;
        }

        switch (graphicBean.getType()) {
            case GraphicBean.GRAPHIC_TYPE_TEXT: {
                String Answer = graphicBean.getAnswer();
                speak(Answer);
                setRobotChatMsg(Answer);
            }
            break;
            case GraphicBean.GRAPHIC_TYPE_PICTURE: {
                GraphicPictureBean graphicPictureBean = GsonUtils.jsonToObject(graphic, GraphicPictureBean.class);
                List<ChatBean.PictureBean> pictures = new ArrayList<>();
                //json bean -> view bean
                for (GraphicPictureBean.ImgFileBean fileBean : graphicPictureBean.getImgFile()) {
                    ChatBean.PictureBean pictureBean = new ChatBean.PictureBean();
                    pictureBean.setPictureUrl(fileBean.getUrl());
                    pictures.add(pictureBean);
                }
                speak(graphicBean.getAnswer());
                setRobotPictureChatMsg(graphicBean.getAnswer(), pictures);
            }
            break;
            case GraphicBean.GRAPHIC_TYPE_AUDIO: {
                GraphicAudioBean graphicAudioBean = GsonUtils.jsonToObject(graphic, GraphicAudioBean.class);
                List<ChatBean.PictureBean> videos = new ArrayList<>();
                //json bean -> view bean
                for (GraphicAudioBean.AudioFileBean fileBean : graphicAudioBean.getAudioFile()) {
                    ChatBean.PictureBean pictureBean = new ChatBean.PictureBean();

                    pictureBean.setPictureUrl(fileBean.getUrl());
                    videos.add(pictureBean);
                }
                speak(graphicBean.getAnswer());
                setRobotAudioChatMsg(graphicBean.getAnswer(), videos);
            }
            break;
            case GraphicBean.GRAPHIC_TYPE_HYPERLINK: {
                GraphicHyperlinkBean hyperlinkBean = GsonUtils.jsonToObject(graphic, GraphicHyperlinkBean.class);
                List<ChatBean.PictureBean> links = new ArrayList<>();

                ChatBean.PictureBean pictureBean = new ChatBean.PictureBean();
                pictureBean.setPictureUrl(hyperlinkBean.getLink());
                links.add(pictureBean);

                speak(graphicBean.getAnswer());
                setRobotHyperlinkChat(graphicBean.getAnswer(), links);
            }
            break;
            case GraphicBean.GRAPHIC_TYPE_VIDEO: {
                GraphicVideoBean graphicVideoBean = GsonUtils.jsonToObject(graphic, GraphicVideoBean.class);
                List<ChatBean.PictureBean> videos = new ArrayList<>();
                //json bean -> view bean
                for (GraphicVideoBean.VideoFileBean fileBean : graphicVideoBean.getVideoFile()) {
                    ChatBean.PictureBean pictureBean = new ChatBean.PictureBean();

                    pictureBean.setPictureUrl(fileBean.getUrl());
                    videos.add(pictureBean);
                }
                speak(graphicBean.getAnswer());
                setRobotVideoChatMsg(graphicBean.getAnswer(), videos);
            }
            break;
            default:
                break;
        }
    }


    protected void onRecognitiontext(String text) {

    }

    private String sensitiveWordsFilter(String text) {
        getLog().info("敏感词匹配:" + text);
        List<SensitiveWordsBean> sensitiveWordsBeans = Constants.sensitiveWordsBeans;
        if (sensitiveWordsBeans != null && sensitiveWordsBeans.size() > 0) {
            for (SensitiveWordsBean sensitiveWordsBean : sensitiveWordsBeans) {
                String name = sensitiveWordsBean.getName();
                if (text.contains(name)) {
                    isSensitiveWords = true;
                    getLog().info("已匹配敏感词:" + name);
                    text = text.replace(name, "***");
                }
            }
        }
        return text;
    }

    @SuppressLint("StringFormatInvalid")
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

                answerText = String.format(Locale.getDefault(),
                        getString(R.string.my_name_is), robotName);
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

    protected void koreanSpeechMessage(String json) {
        JSONObject result = null;
        try {
            result = new JSONObject(json).getJSONObject("result");
            String text = result.getString("text");
            String answerText = "";
            try {
                answerText = result.getJSONObject("answer").getString("answer_text");
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            if (TextUtils.isEmpty(answerText) || p.matcher(answerText).find()) {
                return;
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

    protected void japaneseSpeechMessage(String json) {
        JSONObject result = null;
        try {
            result = new JSONObject(json).getJSONObject("result");
            String text = result.getString("text");
            String answerText = "";
            try {
                answerText = result.getJSONObject("answer").getString("answer_text");
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            if (TextUtils.isEmpty(answerText) || p.matcher(answerText).find()) {
                return;
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

    protected void englishSpeechMessage(String json) {
        JSONObject result = null;
        try {
            result = new JSONObject(json).getJSONObject("result");
            String text = result.getString("text");
            String answerText = "";
            try {
                answerText = result.getJSONObject("answer").getString("answer_text");
            } catch (Exception e1) {
                e1.printStackTrace();
                CsjlogProxy.getInstance().debug("english  not in message");
            }


            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            if (TextUtils.isEmpty(answerText) || p.matcher(answerText).find()) {
                answerText = cuteAnswers[new Random().nextInt(cuteAnswers.length - 1)];
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

    protected String getIntentType(String json) {
        String empty = "";
        JSONObject result = null;
        JSONObject data = null;
        try {
            result = new JSONObject(json).getJSONObject("result");
            data = result.getJSONObject("data");

            String text = result.getString("text");

            String type = data.getString("type");

            CsjlogProxy.getInstance().info("type:" + type);

            /**************产品购买(开始)*************/
            try {
                String taskTopAct = this.getClass().getSimpleName();
                if (!taskTopAct.contains(NaviActivity.class.getSimpleName())
                        && !taskTopAct.contains(VipCenterActivity.class.getSimpleName())) {
                    String intent = Constants.Product.getProductIntent(text);
                    if (!TextUtils.isEmpty(intent)) {
                        return intent;
                    }
                }
            } catch (Exception e) {
                getLog().error("产品购买:error " + e.toString());
            }
            /**************产品购买(结束)*************/

            /**************音量控制(开始)*************/
            if (text.contains(getString(R.string.noise_down))
                    || text.contains("音量小一点")
                    || text.contains("小一点声音")
                    || text.contains("小点声音")
                    || text.contains("小点声")) {
                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.VOLUME + "\",\"result\":{\"action\":\"small\"}}";
            } else if (text.contains(getString(R.string.noise_up))
                    || text.contains("音量大一点")
                    || text.contains("大一点声音")
                    || text.contains("大一点音量")
                    || text.contains("大点声")
                    || text.contains("大点声音")) {
                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.VOLUME + "\",\"result\":{\"action\":\"large\"}}";

            }
            /**************音量控制(结束)*************/


            /**************表情控制(开始)*************/
            if (Constants.expressionControlBean == null) {
                getLog().info("expressionControl:Constants.expressionControlBean:null");
                String filePath = Constants.Path.EXPRESSION_CONFIG_PATH + Constants.Path.EXPRESSION_CONFIG_FILE_NAME;
                File file = new File(filePath);
                getLog().info("expressionControl:filePath:" + file.exists());
                if (file.exists()) {

                    runOnUiThread(() -> new Thread(() -> {
                        String expressionJson = FileUtil.readToStringUTF8(filePath);
                        Constants.expressionControlBean = new Gson().fromJson(expressionJson, ExpressionControlBean.class);
                    }).start());


                } else {
                    String expressionJson = "{\n" +
                            "\t\"happy\": [\n" +
                            "\t\t\"好看\", \"漂亮\", \"开心\"\n" +
                            "\t],\n" +
                            "\t\"normal\": [],\n" +
                            "\t\"smile\": [\n" +
                            "\t\t\"你好\", \"微笑\"\n" +
                            "\t],\n" +
                            "\t\"sad\": [\n" +
                            "\t\t\"丑\", \"难看\", \"悲伤\"\n" +
                            "\t],\n" +
                            "\t\"angry\": [\n" +
                            "\t\t\"笨蛋\", \"白痴\", \"神经病\", \"生气\"\n" +
                            "\t],\n" +
                            "\t\"surprise\": [\n" +
                            "\t\t\"惊讶\"\n" +
                            "\t]\n" +
                            "}";
                    new Thread(() -> {
                        Constants.expressionControlBean = new Gson().fromJson(expressionJson, ExpressionControlBean.class);
                        File pathFile = new File(Constants.Path.EXPRESSION_CONFIG_PATH);
                        if (!pathFile.exists()) {
                            pathFile.mkdirs();
                        }
                        try {
                            file.createNewFile();
                            FileUtil.writeToFile(file, expressionJson);
                        } catch (IOException e) {
                            getLog().error("e:" + e.toString());
                        }
                    }).start();

                }
            } else {
                for (String angry : Constants.expressionControlBean.getAngry()) {
                    getLog().info("angry:" + angry);
                    if (text.contains(angry)) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.EXPRESSION + "\",\"result\":{\"action\":\"angry\"}}";
                    }
                }
                for (String happy : Constants.expressionControlBean.getHappy()) {
                    getLog().info("happy:" + happy);
                    if (text.contains(happy)) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.EXPRESSION + "\",\"result\":{\"action\":\"happy\"}}";
                    }
                }
                for (String normal : Constants.expressionControlBean.getNormal()) {
                    getLog().info("normal:" + normal);
                    if (text.contains(normal)) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.EXPRESSION + "\",\"result\":{\"action\":\"normal\"}}";
                    }
                }
                for (String sad : Constants.expressionControlBean.getSad()) {
                    getLog().info("sad:" + sad);
                    if (text.contains(sad)) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.EXPRESSION + "\",\"result\":{\"action\":\"sad\"}}";
                    }
                }
                for (String smile : Constants.expressionControlBean.getSmile()) {
                    getLog().info("smile:" + smile);
                    if (text.contains(smile)) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.EXPRESSION + "\",\"result\":{\"action\":\"smile\"}}";
                    }
                }
                for (String surprise : Constants.expressionControlBean.getSurprise()) {
                    getLog().info(surprise + surprise);
                    if (text.contains(surprise)) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.EXPRESSION + "\",\"result\":{\"action\":\"surprise\"}}";
                    }
                }
            }
            /**************表情控制(结束)*************/


            /**************动作控制(开始)*************/
            if (isOpenActionControl()) {
                String forwardRegEx = "[\"前进\"|\"前进一步\"|\"往前走\"|\"往前走一步\"|\"向前走\"|\"向前走一步\"|\"向前走一下\"|\"往前走一下\"]+[。|.]";
                String backwardRegEx = "[\"后退\"|\"后退一步\"|\"往后退\"|\"往后退一步\"|\"向后退\"|\"向后退一步\"|\"向后退一下\"|\"往后退一下\"]+[。|.]";

                String leftRegEx = "[\"左转\"|\"往左转\"|\"向左转\"]+[。|.]";
                String leftDegreeRegEx = "[\"左转\"|\"往左转\"|\"向左转\"]+[0-9]+度+[。|.]";
                String leftDegreeRegEx2 = "[\"左转\"|\"往左转\"|\"向左转\"].*度+[。|.]";

                String rightRegEx = "[\"右转\"|\"往右转\"|\"向右转\"]+[。|.]";
                String rightDegreeRegEx = "[\"右转\"|\"往右转\"|\"向右转\"]+[0-9]+度+[。|.]";
                String rightDegreeRegEx2 = "[\"右转\"|\"往右转\"|\"向右转\"].*度+[。|.]";

                if (text.matches(forwardRegEx)) {
                    getLog().info("前进---------");
                    return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"forward\"}}";
                } else if (text.matches(backwardRegEx)) {
                    getLog().info("后退---------");
                    return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"backward\"}}";
                } else if (text.contains("左转") || text.contains("往左转") || text.contains("向左转")) {
                    getLog().info("左转意图---------");
                    if (text.matches(leftRegEx)) {
                        getLog().info("左转---------");
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"turnLeft\",\"degree\":0}}";
                    } else if (text.matches(leftDegreeRegEx)) {
                        int degree = NumberUtils.getNumber(text);
                        if (degree > 360) {
                            degree = 360;
                        }
                        getLog().info("左转" + degree + "度---------1");
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"turnLeft\",\"degree\":" + degree + "}}";
                    } else if (text.matches(leftDegreeRegEx2)) {
                        int degree = NumberUtils.numberParser(text);
                        if (degree > 360) {
                            degree = 360;
                        }
                        getLog().info("左转" + degree + "度---------2");
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"turnLeft\",\"degree\":" + degree + "}}";
                    }
                } else if (text.contains("右转") || text.contains("往右转") || text.contains("向右转")) {
                    getLog().info("右转意图---------");
                    if (text.matches(rightRegEx)) {
                        getLog().info("右转---------");
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"turnRight\",\"degree\":0}}";
                    } else if (text.matches(rightDegreeRegEx)) {
                        int degree = NumberUtils.getNumber(text);
                        if (degree > 360) {
                            degree = 360;
                        }
                        getLog().info("右转" + degree + "度---------1");
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"turnRight\",\"degree\":" + degree + "}}";
                    } else if (text.matches(rightDegreeRegEx2)) {
                        int degree = NumberUtils.numberParser(text);
                        if (degree > 360) {
                            degree = 360;
                        }
                        getLog().info("右转" + degree + "度---------2");
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"turnRight\",\"degree\":" + degree + "}}";
                    }
                }
            }
            /**************动作控制(结束)*************/


            if (!isDisableEntertainment()) {
                if (text.contains(getString(R.string.dance)) || text.contains(getString(R.string.dance1))
                        || text.contains(getString(R.string.dance2)) || text.contains(getString(R.string.dance3))) {
                    return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.DANCE + "\",\"result\":{}}";
                }
            }

            if (type.equals("chat")) {
                String serviceId = data.getString("serviceId");
                if (serviceId.equals(Intents.MUSIC)) {
                    if (isDisableEntertainment()) {
                        return empty;
                    }
                    return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.MUSIC + "\",\"result\":{}}";
                } else if (serviceId.equals(Intents.NEWS)) {
                    if (isDisableEntertainment()) {
                        return empty;
                    }
                    return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.NEWS + "\",\"result\":{}}";
                } else if (serviceId.equals(Intents.STORY)) {
                    if (isDisableEntertainment()) {
                        return empty;
                    }
                    return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.STORY + "\",\"result\":{}}";
                }
            }


        } catch (JSONException e) {
            CsjlogProxy.getInstance().error("json解析出错");
        }
        return empty;
    }

    //大厅巡航的时候，不响应这些指令
    private boolean patrolHandle(String intent) {
        boolean isFlag = true;
        String taskTopAct = this.getClass().getSimpleName();
        if (taskTopAct.contains(PatrolActivity.class.getSimpleName())) {
            if (intent != null && (intent.equals(Intents.MUSIC) || intent.equals(Intents.STORY) || intent.equals(Intents.DANCE)
                    || intent.equals(Intents.NEWS) || intent.equals(Intents.ACTION) || intent.equals(Intents.PRODUCT_BUY))) {
                isFlag = false;
            }
        }
        return isFlag;
    }

    protected boolean dealIntent(String intentJson, String source) {
        boolean isNotEmptyIntent = true;
        getLog().warn("intentJson:" + intentJson);
        String intent = "";
        try {
            intent = new JSONObject(intentJson).getString("intent");
            getLog().warn("intent:" + intent);
        } catch (JSONException e) {
            getLog().error("intent json 解析失败:" + e.toString());
        }
        if (patrolHandle(intent)) {
            switch (intent) {
                case Intents.MUSIC:
                    music(source);
                    break;
                case Intents.STORY:
                    story(source);
                    break;
                case Intents.DANCE:
                    dance(source);
                    break;
                case Intents.NEWS:
                    news(source);
                    break;
                case Intents.VOLUME:
                    volume(intentJson, source);
                    break;
                case Intents.EXPRESSION:
                    expression(intentJson, source);
                    isNotEmptyIntent = false;
                    break;
                case Intents.ACTION:
                    action(intentJson, source);
                    break;
                case Intents.PRODUCT_BUY:
                    product(intentJson, source);
                    break;
                default:
                    isNotEmptyIntent = false;
                    break;
            }
        } else {
            runOnUiThread(() -> {
                speak(SpeakUtil.getRobotName() + getString(R.string.patrol_working));
                setRobotChatMsg(SpeakUtil.getRobotName() + getString(R.string.patrol_working));
            });
        }
        return isNotEmptyIntent;
    }

    protected void product(String intentjson, String source) {
        try {
            JSONObject result = new JSONObject(intentjson).getJSONObject("result");
            String action = result.getString("action");
            String productJson = result.getString("productData");
            if (action.equals("productCategory")) {
                ARouter.getInstance()
                        .build(BRouterPath.PRODUCT_CLOTHING_LIST)
                        .withParcelableArrayList(ProductListActivity.PRODUCT_DATA, new Gson().fromJson(productJson, new TypeToken<ArrayList<GoodsDetailBean>>() {
                        }.getType()))
                        .navigation();
            } else if (action.equals("productDetail")) {
                ARouter.getInstance()
                        .build(BRouterPath.PRODUCT_CLOTHING_DETAIL)
                        .withParcelable(ProductDetailsActivity.PRODUCT_DETAIL, new Gson().fromJson(productJson, GoodsDetailBean.class))
                        .navigation();
            } else if (action.equals("productNot")) {
                runOnUiThread(() -> prattle("抱歉，暂未找到您想要的商品哦，您可以了解下本店的其它商品哦"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void music(String source) {
        JSONObject result = null;
        try {
            result = new JSONObject(source).getJSONObject("result");
            String data = result
                    .getJSONObject("data")
                    .getJSONObject("data").toString();
            Bundle bundle = new Bundle();
            bundle.putString("data", data);
            if (this instanceof MusicActivity) {
                runOnUiThread(() -> onShowMessage(data));
            } else {
                jumpActivity(MusicActivity.class, bundle);
                if (this.getClass().getSimpleName().equals(NewsActivity.class.getSimpleName())
                        || this.getClass().getSimpleName().equals(StoryActivity.class.getSimpleName())
                        || this.getClass().getSimpleName().equals(DanceActivity.class.getSimpleName())) {
                    this.finish();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void story(String source) {
        JSONObject result = null;
        try {
            result = new JSONObject(source).getJSONObject("result");
            String data = result
                    .getJSONObject("data").toString();
            Bundle bundle = new Bundle();
            bundle.putString("data", data);
            if (this instanceof StoryActivity) {
                runOnUiThread(() -> onShowMessage(data));
            } else {
                jumpActivity(StoryActivity.class, bundle);
                if (this.getClass().getSimpleName().equals(MusicActivity.class.getSimpleName())
                        || this.getClass().getSimpleName().equals(NewsActivity.class.getSimpleName())
                        || this.getClass().getSimpleName().equals(DanceActivity.class.getSimpleName())) {
                    finish();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void dance(String source) {
        if (!(this instanceof DanceActivity)) {
            jumpActivity(DanceActivity.class);
            if (this.getClass().getSimpleName().equals(MusicActivity.class.getSimpleName())
                    || this.getClass().getSimpleName().equals(NewsActivity.class.getSimpleName())
                    || this.getClass().getSimpleName().equals(StoryActivity.class.getSimpleName())) {
                this.finish();
            }
        }
    }

    protected void news(String source) {
        JSONObject result = null;
        try {
            result = new JSONObject(source).getJSONObject("result");
            String data = result
                    .getJSONObject("data")
                    .getJSONObject("data").toString();
            Bundle bundle = new Bundle();
            bundle.putString("data", data);
            if (this.getClass().getSimpleName().equals(NewsActivity.class.getSimpleName())) {
                runOnUiThread(() -> onShowMessage(data));
            } else {
                jumpActivity(NewsActivity.class, bundle);
                if (this.getClass().getSimpleName().equals(MusicActivity.class.getSimpleName())
                        || this.getClass().getSimpleName().equals(StoryActivity.class.getSimpleName())
                        || this.getClass().getSimpleName().equals(DanceActivity.class.getSimpleName())) {
                    this.finish();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void chat(String source) {
        JSONObject result;
        JSONObject data;
        try {
            result = new JSONObject(source).getJSONObject("result");
            String text = result.getString("text");
            int errorCode = result.getInt("error_code");

            if (errorCode == 10119) {// 未知问题
                if (this instanceof NaviActivity || this instanceof NaviSettingActivity) {
                    return;
                }
                String answer = Constants.UnknownProblemAnswer.getAnswer();
                getLog().info("未知问题答案:" + answer);
                runOnUiThread(() -> prattle(answer));
                return;
            }
            data = result.getJSONObject("data");
            String type = answerType = data.getString("type");
            if (type.equals("chat")) {
                String serviceId = data.getString("serviceId");
                isChatting = serviceId.equals("other");
            }
            String answerText = data.getString("say");

            if (answerText.contains("#$#$")) {
                String replaceName;
                if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_SNOW)) {
                    replaceName = "小雪";
                } else if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
                    replaceName = "艾米";
                } else {
                    replaceName = "爱丽丝";
                }
                answerText = answerText.replace("#$#$", replaceName);
                CsjlogProxy.getInstance().info("answerText:" + answerText);
            }
            String filterAnswerText = answerText;
            runOnUiThread(() -> onSpeechMessage(text, filterAnswerText));
//            runOnUiThread(() -> onAnswerMessage(text,filterAnswerText,source));
        } catch (JSONException e) {

        }

    }

    protected void volume(String intentjson, String source) {
        try {
            JSONObject result = new JSONObject(intentjson).getJSONObject("result");
            String action = result.getString("action");
            if (action.equals("small")) {
                // 调小音量
                ((AudioManager) getSystemService(Context.AUDIO_SERVICE))
                        .adjustStreamVolume(
                                AudioManager.STREAM_MUSIC
                                , AudioManager.ADJUST_LOWER
                                , AudioManager.FX_FOCUS_NAVIGATION_UP);
            } else if (action.equals("large")) {
                // 调大音量
                ((AudioManager) getSystemService(Context.AUDIO_SERVICE))
                        .adjustStreamVolume(
                                AudioManager.STREAM_MUSIC
                                , AudioManager.ADJUST_RAISE
                                , AudioManager.FX_FOCUS_NAVIGATION_UP);
            }
        } catch (JSONException e) {

        }
    }


    protected void expression(String intentjson, String source) {
        if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            return;
        }
        try {
            JSONObject result = new JSONObject(intentjson).getJSONObject("result");
            String action = result.getString("action");
            switch (action) {
                case "happy":
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.HAPPY
                                    , REQConstants.Expression.NO
                                    , 2000);
                    break;
                case "normal":
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.NORMAL
                                    , REQConstants.Expression.NO
                                    , 2000);
                    break;
                case "smile":
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.SMILE
                                    , REQConstants.Expression.NO
                                    , 2000);
                    break;
                case "sad":
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.SADNESS
                                    , REQConstants.Expression.NO
                                    , 2000);
                    break;
                case "angry":
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.ANGRY
                                    , REQConstants.Expression.NO
                                    , 2000);
                    break;
                case "surprise":
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.SURPRISED
                                    , REQConstants.Expression.NO
                                    , 2000);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {

        }
    }

    protected void action(String intentjson, String source) {
        int degree;
        try {
            JSONObject result = new JSONObject(intentjson).getJSONObject("result");
            String action = result.getString("action");
            switch (action) {
                case "forward":
                    new Thread(() -> {
                        int i = 0;
                        while (i < 5) {
                            ServerFactory.getChassisInstance().moveForward();
                            i++;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case "backward":
                    new Thread(() -> {
                        int i = 0;
                        while (i < 5) {
                            ServerFactory.getChassisInstance().moveBack();
                            i++;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case "turnLeft":
                    degree = result.getInt("degree");
                    if (degree != 0) {
                        RobotManager.getInstance().robot.reqProxy.moveAngle(degree);
                    } else {
                        ServerFactory.getChassisInstance().turnLeft();
                    }
                    break;
                case "turnRight":
                    degree = result.getInt("degree");
                    if (degree != 0) {
                        RobotManager.getInstance().robot.reqProxy.moveAngle(-degree);
                    } else {
                        ServerFactory.getChassisInstance().turnRight();
                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 语音是否可被打断
     *
     * @return
     */
    protected boolean isInterruptSpeech() {
        return false;
    }

    protected void onShowMessage(String data) {

    }

    protected void setRobotChatMsg(@NonNull CharSequence text) {
        if (getChatView() != null && !TextUtils.isEmpty(text)) {
            runOnUiThread(() -> getChatView().addRobotNormalChat(text));
        }
    }

    protected void setRobotChatMsg(@StringRes int stringId) {
        if (getChatView() != null) {
            runOnUiThread(() -> getChatView().addRobotNormalChat(getString(stringId)));
        }
    }

    protected void setCustomerChatMsg(@NonNull CharSequence text) {
        if (getChatView() != null && (!TextUtils.isEmpty(text))) {
            runOnUiThread(() -> getChatView().addPeopleNormalChat(text));
        }
    }

    protected void setCustomerChatMsg(@StringRes int stringId) {
        if (getChatView() != null) {
            runOnUiThread(() -> getChatView().addPeopleNormalChat(getString(stringId)));
        }
    }

    protected void setRobotPictureChatMsg(@NonNull String msg, @NonNull List<ChatBean.PictureBean> pictures) {
        if (getChatView() != null) {
            runOnUiThread(() -> getChatView().addRobotPictureChat(msg, pictures));
        }
    }

    protected void setRobotVideoChatMsg(@NonNull String msg, @NonNull List<ChatBean.PictureBean> pictures) {
        if (getChatView() != null) {
            runOnUiThread(() -> getChatView().addRobotVideoChat(msg, pictures));
        }
    }

    protected void setRobotAudioChatMsg(@NonNull String msg, @NonNull List<ChatBean.PictureBean> pictures) {
        if (getChatView() != null) {
            runOnUiThread(() -> getChatView().addRobotAudioChat(msg, pictures));
        }
    }

    protected void setRobotHyperlinkChat(@NonNull String msg, @NonNull List<ChatBean.PictureBean> links) {
        if (getChatView() != null) {
            runOnUiThread(() -> getChatView().addRobotHyperlinkChat(msg, links));
        }
    }

    protected void englishHandle(String text) {
        if (!mSpeak.isSpeaking()) {
            runOnUiThread(() -> {
                setCustomerChatMsg(text);
            });

        }
        String answer = EnglishChatHandle.newInstance().getAnswer(text);
        speak(answer);
        setRobotChatMsg(answer);

    }

    protected boolean onSpeechMessage(String text, String answerText) {
        String result = mChatControl.handleGlobal(text);
        CsjlogProxy.getInstance().info("handleGlobal:result:" + result);
        if (!TextUtils.isEmpty(result)) {
            if (!result.equals(ChatControl.ACTION)) {
                prattle(result);
            }
            return true;
        }
        result = mChatControl.handleChild(text);
        CsjlogProxy.getInstance().info("handleChild:result:" + result);
        if (!TextUtils.isEmpty(result)) {
            if (!result.equals(ChatControl.ACTION)) {
                prattle(result);
            }
            return true;
        }

        if (TextUtils.equals(Constants.Scene.CurrentScene, Constants.Scene.Dianli)//仅在电力场景有效
                && (this instanceof AllSceneHomeActivity//主页
                || this instanceof ContentActivity //内容管理
                || this instanceof MediaPublicizeActivity //媒体宣传
                || this instanceof EntertainmentActivity //互动娱乐
                || this instanceof NearByActivity//周边服务
                || this instanceof PaymentGuideActivity//查缴指南
        )) {//条件达成
            if (TextUtils.equals(answerType, "chat")) {
                for (QrCodeBean paymentQrCode : Constants.QrCode.sPaymentQrCodes) {
                    if (TextUtils.isEmpty(paymentQrCode.getQrCodeKey())) {
                        continue;
                    }
                    if (text.contains(paymentQrCode.getQrCodeKey())) {//包含查缴指南关键字，则跳转到查缴指南界面
                        if (this instanceof PaymentGuideActivity) {//如果当前为查缴指南界面，则不做跳转处理
                            return false;
                        }
                        ARouter.getInstance()
                                .build(BRouterPath.PAYMENT_GUIDE)
                                .withString(PaymentGuideActivity.KEY, paymentQrCode.getQrName())
                                .navigation();
                        return true;
                    }
                }
                for (QrCodeBean floatQrCode : Constants.QrCode.sFloatQrCodes) {//悬浮二维码关键字
                    if (TextUtils.isEmpty(floatQrCode.getQrCodeKey())) {
                        continue;
                    }
                    if (text.contains(floatQrCode.getQrCodeKey())) {//弹出二维码悬浮窗，并播报
                        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.POP_QR_CODE, floatQrCode.getQrName()));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean onAnswerMessage(String text, String say, String json) {
        String result = mChatControl.handleGlobal(text);
        CsjlogProxy.getInstance().info("handleGlobal:result:" + result);
        if (!TextUtils.isEmpty(result)) {
            if (!result.equals(ChatControl.ACTION)) {
                prattle(result);
            }
            return true;
        }
        result = mChatControl.handleChild(text);
        CsjlogProxy.getInstance().info("handleChild:result:" + result);
        if (!TextUtils.isEmpty(result)) {
            if (!result.equals(ChatControl.ACTION)) {
                prattle(result);
            }
            return true;
        }
        return false;
    }

    protected void showAnswer(String say, String json) {
        try {
            JSONObject data = new JSONObject(json).getJSONObject("result").getJSONObject("data");
            String type = data.getString("type");
            String serviceId = data.getString("serviceId");

            if (type.equals("chat")) {// 闲聊
                if (serviceId.equals("other")) {
                    prattle(say);
                }
            } else if (type.equals("guide")) {// 引导
                prattle(say);
            } else if (type.equals("failure")) {// 未知问题
                prattle(say);
            } else if (type.equals("satisfy")) {// 引导答案
                prattle(say);
            } else {

            }
        } catch (JSONException e) {
            getLog().error("showAnswer:e:" + e.toString());
        }
    }


    /**
     * 是否开启动作控制命令
     *
     * @return
     */
    protected boolean isOpenActionControl() {
        return true;
    }


    protected void prattle(String answerText) {
        if (Constants.sIsCustomerService) {
            return;
        }
        if (!mSpeak.isSpeaking()) {
            //消息上传至人工客服
            speak(answerText);
            setRobotChatMsg(answerText);
        }
    }


    private void initSpeak() {
        mSpeak = ServerFactory.getSpeakInstance();
    }

    private void initChat() {
        if (isOpenChat()) {
            mChatView = (ChatView) findViewById(R.id.chat_view);
        }
    }

    private void initTitle() {
        if (isOpenTitle()) {
            mTitleView = (TitleView) findViewById(R.id.title_view);
            initImage();
            mTitleView.setOnViewClickListener(v -> {
                switch (v.getId()) {
                    case R.id.ib_language:
                    case R.id.tv_mainpage_language:
                        new LanguageWindow(this).showAsDropDown(mTitleView, 40, 0);
                        // audio
//                        String say = "{\"msg_id\":\"SPEECH_ISR_LAST_RESULT_NTF\",\"result\":{\"data\":{\"answer\":\"当然好吃拉\",\"actionList\":[],\"say\":\"当然好吃拉\",\"type\":\"satisfy\",\"graphic\":\"{\\\"type\\\":\\\"3\\\",\\\"answer\\\":\\\"当然好吃拉\\\",\\\"audioFile\\\":[{\\\"url\\\":\\\"http://csjbot-test.su.bcebos.com/kX6fwEDNa4ZDr6QJWdc7ws6DnmXPNyHQJ8nx8KWF.MP3\\\",\\\"name\\\":\\\"尚法昆山.MP3\\\"}]}\"},\"error_code\":0,\"text\":\"袁泉帅不帅？\"}}";
//                      // video
//                      String say = "{\"msg_id\":\"SPEECH_ISR_LAST_RESULT_NTF\",\"result\":{\"data\":{\"actionList\":[],\"answer\":\"当然帅拉，但是没有你帅！\",\"graphic\":\"{\\\"type\\\":\\\"4\\\",\\\"answer\\\":\\\"当然帅拉，但是没有你帅！\\\",\\\"videoFile\\\":[{\\\"url\\\":\\\"http://csjbot-test.su.bcebos.com/W2ZGh2RiC2z774DWfFEEWmnKJsXDeWZpxPsyARzz.mp4\\\",\\\"name\\\":\\\"alice_coffee.mp4\\\"}]}\",\"say\":\"当然帅拉，但是没有你帅！\",\"type\":\"satisfy\"},\"error_code\":0,\"text\":\"袁泉帅不帅？\"}}";
                        // picture
//                        String say = "{\"msg_id\":\"SPEECH_ISR_LAST_RESULT_NTF\",\"result\":{\"data\":{\"actionList\":[],\"answer\":\"我想吃红烧肉\",\"graphic\":\"{\\\"type\\\":\\\"2\\\",\\\"answer\\\":\\\"我想吃红烧肉\\\",\\\"imgFile\\\":[{\\\"url\\\":\\\"http://csjbot-test.su.bcebos.com/mkYycGynG8KsP5SwBn7EKwHwfy7YRxdHmfXaTKmM.jpg\\\"},{\\\"url\\\":\\\"http://csjbot-test.su.bcebos.com/MFxpxFMmS6Q4FEsEPymmxkNJHd5RyCWZPmrbYJB3.jpg\\\"},{\\\"url\\\":\\\"http://csjbot-test.su.bcebos.com/iTkP5dAAkxGCnBQ4aeBArWGsXZWA2pYhkhMcbFQN.jpg\\\"},{\\\"url\\\":\\\"http://csjbot-test.su.bcebos.com/Cb4REJeAcdsDYPrwTHm8ZCaYHRzDNiiYAnijDR48.jpg\\\"}]}\",\"say\":\"我想吃红烧肉\",\"type\":\"satisfy\"},\"error_code\":0,\"text\":\"我想吃红烧肉。\"}}";

//                        String say = "{\"msg_id\":\"SPEECH_ISR_LAST_RESULT_NTF\",\"result\":{\"data\":{\"actionList\":[],\"answer\":\"。\",\"graphic\":\"{\\\"type\\\":\\\"10\\\",\\\"answer\\\":\\\"。\\\",\\\"link\\\":\\\"www.baidu.com\\\"}\",\"say\":\"。\",\"type\":\"satisfy\"},\"error_code\":0,\"text\":\"百度链接是什么？\"}}";
//                        Robot.getInstance().pushSpeech(say, Robot.SPEECH_LAST_RESULT);
                        break;
                    case R.id.ib_back:
                        onClickTilteBack();
                        break;
                    case R.id.ib_home:
                        BRouter.toHome();
                        break;
                    case R.id.ib_shop_cart:

                        break;
                    case R.id.ib_customer:
//                        if (Constants.sIsCustomerService) {
//                            return;
//                        }
                        if (isFloatWd && Constants.sIsCustomerService) {   //悬浮窗显示，已连接状态
                            mHumanDialog.setListener(mCustomerServiceDialogListener);
                            if (FloatingWdCustomerService.sIsShow)  //隐藏浮窗
                                EventBus.getDefault().post(new FloatWindowCustomerEvent(false));
                            mHumanDialog.show();
                            mHumanDialog.setConnnectStatus(Constants.ALREADY_CONNECTED);
                            mHumanDialog.changeDialogStatus();
                        } else {
                            displayCustomerDialog(Constants.sIsCustomerService);
                        }
                        isFloatWd = false;
                        break;
                    case R.id.ib_setting:
                        jumpActivity(SettingsActivity.class);
                        break;
                }
            });
        }
    }


    //=========================   add   =========================
    protected void displayCustomerDialog(boolean isConnect) {
        if (isConnect) {
            if (!mHumanDialog.isShowing()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mHumanDialog.show();
                        mHumanDialog.setBtnEnable(true);
                    }
                });
                if (FloatingWdCustomerService.sIsShow)  //隐藏浮窗
                    EventBus.getDefault().post(new FloatWindowCustomerEvent(false));
            }
            return;
        }

        showCustomerServiceDialog(mCustomerServiceDialogListener);
    }


    protected CustomerServiceDialog.OnCustomerDialogClickListener mCustomerServiceDialogListener = new CustomerServiceDialog.OnCustomerDialogClickListener() {
        @Override
        public void connectCustomer() {
            Robot.getInstance().callHumanService(Robot.SN);
            CsjlogProxy.getInstance().debug("CustomerDialog : " + "connectCustomer");
        }

        @Override
        public void disconnectCustomer() {
            Robot.getInstance().hangUpHumanService(Robot.SN);
            CsjlogProxy.getInstance().debug("CustomerDialog : " + "disconnectCustomer");
        }

        @Override
        public void hideDialog() {
            EventBus.getDefault().post(new FloatWindowCustomerEvent(true));
            mHumanDialog.dismiss();
        }

        @Override
        public void speakTip(String content) {
            speak(content);
        }
    };


    //=========================   add   =========================


    /**
     * 点击了title返回按钮
     */
    protected void onClickTilteBack() {
        finish();
    }

    protected void refreshLogo() {
        getLog().info("refreshLogo");
        if (mTitleView != null && isOpenTitle()) {
            getLog().info("refreshLogo setSDLogo");
            mTitleView.setLogoBySd(Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME);
        }
    }

    private void initImage() {
        mMode = SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, CsjLanguage.CHINESE);
        CsjlogProxy.getInstance().info("当前语言： " + mMode);

        String showLanguage = CsjLanguage.getShowLanguageByDef(mMode);
        mTitleView.SetLanguageText(showLanguage);
    }

    /**
     * 获取当前ChatView
     *
     * @return
     */
    protected ChatView getChatView() {
        return mChatView;
    }

    /**
     * 获取当前TitleView
     *
     * @return
     */
    protected TitleView getTitleView() {
        return mTitleView;
    }

    /**
     * 对于某些特殊的Activity，你可能需要重写这个方法拿到savedInstanceState
     * 来恢复一些状态
     *
     * @param savedInstanceState
     */
    public void afterViewCreated(Bundle savedInstanceState) {
        // 这个方法，不要写东西，因为很多子类都没有调用
        // super.afterViewCreated
    }


    /**
     * 根据传入的class进行activity的跳转
     *
     * @param c
     */
    public void jumpActivity(Class<? extends Activity> c) {
        if (c != null) {
            startActivity(new Intent(this, c));
        }
    }

    /**
     * 根据传入的class进行activity的跳转  可传入参数
     */
    public void jumpActivity(Class<? extends Activity> c, Bundle bundle) {
        if (c != null) {
            Intent intent = new Intent(this, c);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    /**
     * 提供说话功能
     *
     * @param text
     * @param listener
     */
    protected void speak(String text, OnSpeakListener listener) {
        if (!TextUtils.isEmpty(text)) {
            mSpeak.startSpeaking(text, listener);
        }
    }

    protected void speak(String text, boolean isSetRobotChatMsg) {
        speak(text, null, isSetRobotChatMsg);
    }

    protected void speak(String text, OnSpeakListener listener, boolean isSetRobotChatMsg) {
        if (listener == null) {
            listener = new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {
                    mainHandler.postDelayed(() -> isDiscard = false, 10 * 1000);
                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    isDiscard = false;
                }
            };
        }

        speak(text, listener);
        if (isSetRobotChatMsg) {
            setRobotChatMsg(text);
        }
    }

    protected void speak(@StringRes int stringId, OnSpeakListener listener, boolean isSetRobotChatMsg) {
        speak(getString(stringId), listener, isSetRobotChatMsg);
    }

    protected void speak(@StringRes int stringId, OnSpeakListener listener) {
        speak(getString(stringId), listener, false);
    }

    /**
     * 重新dispatchTouchEvent方法
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 提供说话功能
     *
     * @param text
     */
    public void speak(String text) {
        if (!TextUtils.isEmpty(text)) {
            mSpeak.startSpeaking(text, new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {
                    mainHandler.postDelayed(() -> isDiscard = false, 10 * 1000);
                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    isDiscard = false;
                }
            });
        }

    }

    public void speak(@StringRes int stringId) {
        speak(getString(stringId), null, false);
    }

    public void speak(@StringRes int stringId, boolean isSetRobotChatMsg) {
        speak(getString(stringId), null, isSetRobotChatMsg);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
        if (mSpeak.isSpeaking()) {
            mSpeak.stopSpeaking();
            isDiscard = false;
        }

        EventBus.getDefault().post(new ChatVideoEvent(ChatAdapter.ROBOT_CHAT_DISMISS, null));
    }

    public void stopSpeak() {
        if (mSpeak.isSpeaking()) {
            mSpeak.stopSpeaking();
            isDiscard = false;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_BACK));
        isDiscard = false;
    }

    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isResume) {
                return;
            }

            int state = intent.getIntExtra(NetworkConstants.NET_WORK_STATE, NetworkConstants.NETWORK_UNAVAILABLE);
            if (state == NetworkConstants.NETWORK_AVAILABLE) {
                BlackgagaLogger.debug("网络可用");
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                networkAvailability();
            } else {
                networkUnavailable();
                BlackgagaLogger.debug("网络不可用");
                if (isResume) {
                    showNewRetailDialog(getString(R.string.net_work_nouse), getString(R.string.network_unavailable),
                            getString(R.string.sure), getString(R.string.cancel), new NewRetailDialog.OnDialogClickListener() {
                                @Override
                                public void yes() {
                                    startActivity(new Intent(BaseFullScreenActivity.this, SettingsNetworkActivity.class));
                                    dismissNewRetailDialog();
                                }

                                @Override
                                public void no() {
                                    dismissNewRetailDialog();
                                }
                            });
                }
            }
        }
    }

    public void networkAvailability() {
        CsjlogProxy.getInstance().info("-------------->语音识别可用");
    }

    public void networkUnavailable() {
        CsjlogProxy.getInstance().info("-------------->语音识别不可用");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                VolumeUtil.addMediaVolume(this);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                VolumeUtil.cutMediaVolume(this);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    private BroadcastReceiver updateLogoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshLogo();
        }
    };

    public void hideVideo() {
        if (!Constants.ChangeLan) {
            sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_HIDE));
        }
        Constants.ChangeLan = false;
    }


    BroadcastReceiver chatViewBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            chatServicePopUp = intent.getBooleanExtra("isShowing", false);
            CsjlogProxy.getInstance().info("chatServicePopUp = " + chatServicePopUp);
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void isCanChatAction(AdvertisementEvent event) {
        if (event != null && isResume) {
            switch (event.getActionType()) {
                case AdvertisementEvent.SHOW_HIDE_EVENT:
                    if (event.isHide()) {
                        isCanChat = false;
                    } else {
                        mChatView.clear();
                        isCanChat = true;
                        fromSplashServiceToHome();
                    }
                    break;
                case AdvertisementEvent.ASK_EVENT:
                    if (mSpeak.isSpeaking()) {
                        mSpeak.stopSpeaking();
                        isDiscard = false;
                    }
                    speak(event.getAskMsg(), true);
                    break;
            }
        }
    }

    protected void fromSplashServiceToHome() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void showHumanDialog(OpenCustomerDialogEnvent event) {
        if (event != null) {
            if (isFloatWd && Constants.sIsCustomerService) {
                mHumanDialog.setListener(mCustomerServiceDialogListener);
                mHumanDialog.show();
                mHumanDialog.setConnnectStatus(Constants.ALREADY_CONNECTED);
                mHumanDialog.changeDialogStatus();
                mHumanDialog.setBtnEnable(true);
            } else {
                if (!mHumanDialog.isShowing() && Constants.sIsCustomerService) {
                    mHumanDialog.show();
                    mHumanDialog.setBtnEnable(true);
                }
            }
            isFloatWd = false;

        }
    }


    //人工客服心跳断开
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void CustomerServerDisconnect(CustomerServer2pageEvent event) {
        if (event != null) {
            Log.d("sss", "心跳断开");
            if (isResume) {
                mHumanDialog.setListener(mCustomerServiceDialogListener);
                mHumanDialog.show();
                speak(context.getString(R.string.connect_failed_tip));
                mHumanDialog.setConnnectStatus(Constants.CONNECT_INTERRUPT);
                mHumanDialog.changeDialogStatus();
            }

        }
    }
}
