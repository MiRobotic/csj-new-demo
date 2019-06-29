package com.csjbot.mobileshop.guide_patrol.patrol.patrol_activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.guide_patrol.patrol.bean.PatrolBean;
import com.csjbot.mobileshop.guide_patrol.patrol.core.RobotProxy;
import com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine.StateMachineManager;
import com.csjbot.mobileshop.guide_patrol.patrol.event.PatrolActionEvent;
import com.csjbot.mobileshop.guide_patrol.patrol.event.PatrolEvent;
import com.csjbot.mobileshop.guide_patrol.patrol.patrol_setting.KeyPointView;
import com.csjbot.mobileshop.guide_patrol.patrol.patrol_setting.PatrolSettingActivity;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskUtils;
import com.csjbot.mobileshop.service.PatrolService;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.mobileshop.util.StrUtil;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 孙秀艳 on 2019/1/21.
 */

public class PatrolActivity extends BaseModuleActivity implements PatrolContract.View {

    @BindView(R.id.iv_map)
    ImageView iv_map;
    @BindView(R.id.ll_map)
    RelativeLayout ll_map;
    @BindView(R.id.btn_patrol_action)
    Button btnPatrol;

    private PatrolContract.Presenter mPresenter;
    private ArrayList<KeyPointView> keyPointViews = new ArrayList<>();
    private boolean isBack = false;

    @Override
    public void init() {
        super.init();
        mTitleView.setCustomerLayoutGone();
        mTitleView.setSettingLayoutGone();
        mPresenter = new PatrolPresenter(this);
        mPresenter.initView(this);
        setDefaultMap();
        CsjlogProxy.getInstance().info("sunxy", "status:" + StateMachineManager.getInstance().getCurrentState());
        if (StateMachineManager.getInstance().getCurrentState() != NaviTaskStatus.AWAIT) {
            setRobotChatMsg(R.string.patrol_finish_text);
            speak(R.string.patrol_finish_speak);
        } else {
            setRobotChatMsg(R.string.patrol_start_text);
            speak(R.string.patrol_start_speak);
        }
        startService(new Intent(PatrolActivity.this, PatrolService.class));
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        if (Constants.isPlus()) {
            return R.layout.vertical_activity_patrol;
        } else {
            return R.layout.activity_patrol;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getPatrolData();
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, true));
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_MUTE));
        setBtnStatus();
    }

    @OnClick({R.id.iv_patrol_setting, R.id.btn_patrol_action})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_patrol_setting:
                if (StateMachineManager.getInstance().getCurrentState() == NaviTaskStatus.AWAIT) {
                    jumpActivity(PatrolSettingActivity.class);
                }
                break;
            case R.id.btn_patrol_action:
                patrolAction();
                break;
            default:
                break;
        }
    }

    /**
     * 巡航工作
     */
    private void patrolAction() {
        if (StateMachineManager.getInstance().getCurrentState() != NaviTaskStatus.AWAIT) {
            stopwork();
        } else {
            isBack = false;
            startWork();
        }
    }

    /**
     * 结束巡航
     */
    private void stopwork() {
        speak(R.string.gohome, new OnSpeakListener() {
            @Override
            public void onSpeakBegin() {
                RobotProxy.getInstance().cancelTask();
            }

            @Override
            public void onCompleted(SpeechError speechError) {
                RobotProxy.getInstance().backWelcomePos();
                isBack = true;
            }
        });
    }

    /**
     * 开始巡航
     */
    private void startWork() {
        String json = SharedPreUtil.getString(SharedKey.PATROL_POINT_NAME, SharedKey.PATROL_POINT_KEY);
        List<PatrolBean> navis = GsonUtils.jsonToObject(json, new TypeToken<List<PatrolBean>>() {
        }.getType());
        if (navis == null || navis.size() <= 0) {
            speak(R.string.set_patrol_point);
            Toast.makeText(PatrolActivity.this, R.string.set_patrol_point, Toast.LENGTH_SHORT).show();
        } else if (NaviTaskUtils.isExistYingbinPoint()) {
            speak(R.string.set_yingbin_point);
            Toast.makeText(PatrolActivity.this, R.string.set_yingbin_point, Toast.LENGTH_SHORT).show();
        } else {
            String patrolJson = SharedPreUtil.getString(SharedKey.PATROL_LINE_NAME, SharedKey.PATROL_LINE_KEY);
            List<PatrolBean> patrolCircles = GsonUtils.jsonToObject(patrolJson, new TypeToken<List<PatrolBean>>() {
            }.getType());
            if (patrolCircles == null || patrolCircles.size() < 2) {
                speak(R.string.set_patrol_line);
                Toast.makeText(PatrolActivity.this, R.string.set_patrol_line, Toast.LENGTH_SHORT).show();
            } else {
                speak(R.string.go_work, new OnSpeakListener() {
                    @Override
                    public void onSpeakBegin() {

                    }

                    @Override
                    public void onCompleted(SpeechError speechError) {
                        boolean isCircle = SharedPreUtil.getBoolean(SharedKey.PATROL_PLAN_NAME, SharedKey.PATROL_PLAN_KEY, false);
                        if (isCircle) {
                            RobotProxy.getInstance().startCircle();
                        } else {
                            RobotProxy.getInstance().startSingleCircle();
                        }
                        EventBus.getDefault().post(new PatrolEvent(PatrolEvent.START));
                    }
                });
            }
        }
    }

    @Override
    public void showPatrolPointMap(List<PatrolBean> patrolBeans) {
        int count = ll_map.getChildCount() - 1;
        if (count >= 1) {
            while (count > 0) {
                ll_map.removeViewAt(count);
                count--;
            }
        }

        if (patrolBeans != null && patrolBeans.size() > 0) {
            for (PatrolBean n : patrolBeans) {
                addPointView(n);
            }
        }
    }

    /**
     * 在有数据的情况下添加导引点到地图上
     */
    private void addPointView(PatrolBean naviBean) {
        if (StrUtil.isBlank(naviBean.getName())) {
            return;
        }
        float vectorLength = Constants.isPlus()
                ? 1.96f : 1.22f;
        float vectorwidth = Constants.isPlus()
                ? 1.02f : 1;
        KeyPointView keyPointView = new KeyPointView(this);

        keyPointView.setName(naviBean.getName());
        keyPointView.layout((int) (naviBean.left * vectorLength), (int) (naviBean.top * vectorwidth), (int) (naviBean.right * vectorLength), (int) (naviBean.bottom * vectorwidth));
        keyPointView.setTranslationX(naviBean.getTranslationX() * vectorLength);
        keyPointView.setTranslationY(naviBean.getTranslationY() * vectorwidth);

        keyPointView.setName(mPresenter.getMapPatrolName(naviBean.getName()));

        keyPointViews.add(keyPointView);
        ll_map.addView(keyPointView);
    }

    private void setDefaultMap() {
        String mapPath = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.MAP_PATH);
        if (!TextUtils.isEmpty(mapPath)) {
            CsjlogProxy.getInstance().debug("Navi:地图路径：" + mapPath);
            Glide.with(this).load(mapPath).into(iv_map);
        } else {
            Glide.with(this).load(R.drawable.navi_default_map).into(iv_map);
        }
    }

    private void setBtnStatus() {
        runOnUiThread(() -> {
            if (StateMachineManager.getInstance().getCurrentState() != NaviTaskStatus.AWAIT) {
                btnPatrol.setBackgroundResource(R.drawable.end_btn);
                btnPatrol.setText(R.string.end_patrol);
            } else {
                btnPatrol.setBackgroundResource(R.drawable.start_btn);
                btnPatrol.setText(R.string.start_patrol);
            }
        });
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (CsjLanguage.isEnglish()) {
            return false;
        }
        if (text.contains(getString(R.string.start_patrol))) {
            startWork();
            return false;
        }
        if (text.contains(getString(R.string.end_patrol))) {
            stopwork();
            return false;
        }
        prattle(answerText);
        if (super.onSpeechMessage(text, answerText)) {//解决返回 子界面和整体话术冲突
            return false;
        }
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    protected boolean isHideUserText() {
        return !isCanChat();
    }

    @Override
    protected boolean isCanChat() {
        NaviTaskStatus status = StateMachineManager.getInstance().getCurrentState();
        if (status == NaviTaskStatus.AWAIT || status == NaviTaskStatus.PAUSE) {
            return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void startPatrol(PatrolActionEvent event) {
        if (event != null && !TextUtils.isEmpty(event.getAction())) {
            if (event.getAction().equals("2")) {
                setBtnStatus();
                isBack = false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Robot.getInstance().setPositionListener(null);
        if (isBack) {//返回迎宾点的时候需要取消任务
            RobotProxy.getInstance().cancelTask();
        }
        mainHandler.removeCallbacksAndMessages(null);
    }
}
