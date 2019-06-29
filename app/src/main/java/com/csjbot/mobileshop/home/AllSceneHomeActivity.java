package com.csjbot.mobileshop.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.FloatQrCodeService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseMainPageActivity;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.feature.navigation.NaviAction;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine.StateMachineManager;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;
import com.csjbot.mobileshop.home.contract.AllSceneHomeContract;
import com.csjbot.mobileshop.home.contract.AllSceneHomePresenterImpl;
import com.csjbot.mobileshop.localbean.NaviBean;
import com.csjbot.mobileshop.model.tcp.bean.AllNaviPointsBean;
import com.csjbot.mobileshop.network.CheckEthernetService;
import com.csjbot.mobileshop.router.BRouterKey;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.service.BatteryService;
import com.csjbot.mobileshop.service.HomeService;
import com.csjbot.mobileshop.service.LocationService;
import com.csjbot.mobileshop.speechrule.SpeechRule;
import com.csjbot.mobileshop.util.CSJToast;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.mobileshop.widget.NewRetailDialog;
import com.csjbot.mobileshop.widget.chat_view.bean.ChatBean;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 所有场景通用Activity
 * 先看看所要显示的场景是否存在 ,创建相对应的主页布局的item和场景欢迎语请根据场景自行创建，
 * 还要初始化对应场景的语言切换悬浮窗和内容管理颜色（因为换肤对PopupWindow不支持换肤）
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 请在{@link AllSceneHomePresenterImpl}中设置，具体请看{@link AllSceneHomePresenterImpl#getHomeModuleItemLayoutId(String)}
 * <p>
 * 主页图标换肤请在请在{@link AllSceneHomePresenterImpl}中设置，
 * 具体请看{@link AllSceneHomePresenterImpl#getHomeIconId(int)}
 * 具体请看{@link AllSceneHomePresenterImpl#getContentIcon(int)}
 * <p>
 * 请在{@link Constants}中设置，具体请看{@link Constants#createWelcomeText(String, String)}
 * <p>
 * 请在{@link Constants}中设置，具体请看{@link Constants#initSceneColor(String)}
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 * @author ShenBen
 * @date 2019/1/18 9:21
 * @email 714081644@qq.com
 */
@Route(path = BRouterPath.ALL_SCENE_HOME_PATH)
public class AllSceneHomeActivity extends BaseMainPageActivity implements AllSceneHomeContract.View {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.ll_dots)
    LinearLayout llDots;

    private AllSceneHomeContract.Presenter mPresenter;
    private String mSceneKey = null;

    private String[] yinhangGuideText = {
            "如果有其他需要，请到前台，让大堂经理为您服务", "如需办理业务请至取票机处取票等待叫号", "2万以下可以通过自助取款机取款"};

    private int yinhangGuideTextindex = -1;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_all_scene_home, R.layout.activity_plus_all_scene_home);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        getTitleView().setLanguageLayoutVisible();
        getTitleView().setCustomerLayoutVisible();
        NaviAction.getInstance().setSpeaker(mSpeak);
        HomeService.IS_SWITCH_LANGUAGE = true;
        StateMachineManager.getInstance().setInitState(NaviTaskStatus.AWAIT);
        Intent intent = getIntent();
        if (intent != null) {
            mSceneKey = intent.getStringExtra(BRouterKey.SCENE_NAME);
        }
        new AllSceneHomePresenterImpl(this, this);
        mPresenter.init();
        mPresenter.setScene(mSceneKey);
        mPresenter.initData();

        startService(new Intent(this, CheckEthernetService.class));
        startService(new Intent(this, LocationService.class));
        startService(new Intent(this, FloatQrCodeService.class));
        mainHandler.postDelayed(() -> startService(new Intent(AllSceneHomeActivity.this, BatteryService.class)), 1000);

        // 一旦进入主页，就把重启保护置0
        SharedPreUtil.putInt(SharedKey.REBOOTTIME, SharedKey.REBOOTTIME_KEY, 0);
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);

        //添加底层想获取机器人导航点的通知,人工客服介入的时候底层会向上层拿导航点
        Robot.getInstance().setGetAllPointsListener(() -> {
            String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY, null);
            List<AllNaviPointsBean> list = new ArrayList<>();
            if (!TextUtils.isEmpty(json)) {
                List<NaviBean> naviBeans = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
                }.getType());
                AllNaviPointsBean bean;
                for (NaviBean naviBean : naviBeans) {
                    if (naviBean.getPos() == null) {
                        continue;
                    }
                    bean = new AllNaviPointsBean();
                    bean.setName(naviBean.getName());
                    bean.setNickName(naviBean.getNickName());
                    bean.setRotation(naviBean.getPos().getRotation());
                    bean.setX(naviBean.getPos().getX());
                    bean.setY(naviBean.getPos().getY());
                    bean.setZ(naviBean.getPos().getZ());
                    list.add(bean);
                }
            }
            RobotManager.getInstance().robot.reqProxy.sendAllNaviPoint(GsonUtils.objectToJson(list));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, false));
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (!isResume) {
            return false;
        }
        // 拦截主页的语音控制返回动作
        getChatControl().addIntercept(this.getClass().getSimpleName(), SpeechRule.Action.BACK, null);
        // 拦截主页的语音控制返回主页动作
        getChatControl().addIntercept(this.getClass().getSimpleName(), SpeechRule.Action.GO, this.getClass().getSimpleName());
        if (super.onSpeechMessage(text, answerText)) {
            return true;
        }
        if (TextUtils.equals(answerType, "chat")) {//闲聊有效
            if (mPresenter.speakToIntent(text)) {//优先级：导航->周边服务->主页模块
                return true;
            }
        }
        if (TextUtils.equals(answerType, "chat")) {//闲聊有效
            if (TextUtils.equals(mSceneKey, Constants.Scene.Fuzhuang)) {
                setRobotChatMsg(answerText);
                speak(answerText, new OnSpeakListener() {
                    @Override
                    public void onSpeakBegin() {

                    }

                    @Override
                    public void onCompleted(SpeechError speechError) {
                        isDiscard = false;
                        prattle("亲，请问您今天需要买什么衣服？");
                    }
                });
            } else if (TextUtils.equals(mSceneKey, Constants.Scene.YinHangScene)) {
                setRobotChatMsg(answerText);
                speak(answerText, new OnSpeakListener() {
                    @Override
                    public void onSpeakBegin() {

                    }

                    @Override
                    public void onCompleted(SpeechError speechError) {
                        getLog().info("yinhangGuideTextindex : " + yinhangGuideTextindex);
                        getLog().info("yinhangGuideText.leng : " + yinhangGuideText.length);
                        if (++yinhangGuideTextindex < yinhangGuideText.length) {
                            prattle(yinhangGuideText[yinhangGuideTextindex]);
                        } else {
                            yinhangGuideTextindex = 0;
                            prattle(yinhangGuideText[yinhangGuideTextindex]);
                        }
                        isDiscard = false;
                    }
                });
            } else {
                prattle(answerText);
            }

        } else {
            prattle(answerText);
        }
        return true;
    }


//    @Override
//    protected void fromSplashServiceToHome() {
//        super.fromSplashServiceToHome();
//        //任何情况都说欢迎语
//        StringBuilder greetWords = new StringBuilder();
//
//        greetWords.append(getString(R.string.hello))
//                .append("，")
//                .append(getString(R.string.welcome))
//                .append("！");
//
//        setRobotChatMsg(Constants.createWelcomeText(mSceneKey, greetWords.toString())[0]);
//        speak(Constants.createWelcomeText(mSceneKey, greetWords.toString())[1]);
//    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        stopService(new Intent(this, FloatQrCodeService.class));
        hideVideo();
    }

    @Override
    public void setPresenter(AllSceneHomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView getRecycler() {
        return rv;
    }

    @Override
    public LinearLayout getDots() {
        return llDots;
    }

    @Override
    public void prattleText(String text) {
        runOnUiThread(() -> {
            if (isSpeaking()) {
                mSpeak.stopSpeaking();
            }
            prattle(text);
        });
    }

    @Override
    public void setRobotChatText(String text) {
        runOnUiThread(() -> setRobotChatMsg(text));
    }

    @Override
    public void speakText(String text) {
        speak(text);
    }

    @Override
    public boolean isSpeaking() {
        return mSpeak.isSpeaking();
    }

    @Override
    public void restoreMapDialog() {
        if (RobotManager.getInstance().getConnectState()) {
            showNewRetailDialog(getString(R.string.map_manager), getString(R.string.is_restore_map), new NewRetailDialog.OnDialogClickListener() {
                @Override
                public void yes() {
                    mPresenter.loadMap();
                    dismissNewRetailDialog();
                }

                @Override
                public void no() {
                    dismissNewRetailDialog();
                }
            });
        } else {
            CSJToast.showToast(this, getString(R.string.not_connect_slam), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void setChatPicture(String msg, List<ChatBean.PictureBean> list) {
        setRobotPictureChatMsg(msg, list);
    }


}
