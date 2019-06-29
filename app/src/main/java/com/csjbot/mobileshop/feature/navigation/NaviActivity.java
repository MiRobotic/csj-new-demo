package com.csjbot.mobileshop.feature.navigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnNaviSearchListener;
import com.csjbot.coshandler.listener.OnPositionListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.ai.NaviAI;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.cart.adapter.NaviMenuAdatper;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.event.ListenerState;
import com.csjbot.mobileshop.feature.navigation.setting.NaviGuideAllPWDActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine.StateMachineManager;
import com.csjbot.mobileshop.guide_patrol.widget.NaviTaskStatus;
import com.csjbot.mobileshop.localbean.NaviBean;
import com.csjbot.mobileshop.model.http.bean.rsp.UnreachablePointBean;
import com.csjbot.mobileshop.model.tcp.bean.Position;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.router.BRouter;
import com.csjbot.mobileshop.router.BRouterKey;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.mobileshop.util.SpeakUtil;
import com.csjbot.mobileshop.util.StrUtil;
import com.csjbot.mobileshop.util.TimeCount;
import com.csjbot.mobileshop.util.TimeoutUtil;
import com.csjbot.mobileshop.widget.CommonVideoView;
import com.csjbot.mobileshop.widget.KeyPointView;
import com.csjbot.mobileshop.widget.chat_view.bean.ChatBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingwc on 2017/9/21.
 * Powered by xiasuhuei321
 * <p>
 * 友好的提示：先摸清楚所有的流程，各种延时可能会看的你懵逼，
 * 但是没办法，应用层协议没有使用现成的，只能自己加上计时器了。
 * <p>
 * 重说三：
 * 先理清楚流程
 * 先理清楚流程
 * 先理清楚流程
 */

@Route(path = BRouterPath.NAVI_MAIN)
public class NaviActivity extends BaseModuleActivity implements NaviContract.View {

    NaviContract.Presenter mPresenter;

    NaviAI mAI;

    // 如果为 true 说明是说了一个没有的点
    boolean isFromAI;

    // 导航点数据
    public List<NaviBean> naviBeanList = null;
    // 这个集合保存的是需要导航的店的信息
    public List<NaviBean> workList = new ArrayList<>();
    boolean hasData = false;

    public static final int GUIDE_AWAIT = 10004;

    public static final int GUIDE_ALL = 10086;
    public static final int GUIDE_SINGLE = 10087;
    public static final int GUIDE_BACK = 10088;
    public static final int GUIDE_DEFAULT = -1;
    public volatile int workType = GUIDE_DEFAULT;

    public static final int MSG_TIMEOUT = -10086;
    // 超时消息下发超时
    public static final int MSG_TIMEOUTMSG_TIMEOUT = -10087;

    public static final int GUIDE_ALL_TIMEOUT = 10100;
    public static final int GUIDE_SINGLE_TIMEOUT = 10101;

    private boolean onStop = false;

    private volatile Runnable currentRunnable;
    private int nodeCount = 0;
    private int guideClickCount = 0;
    private TimeoutUtil naviTimeout = new TimeoutUtil();
    private boolean isGuideAllClick = true;
    private NaviPlayer player;

    // 一键导览暂停标识
    private boolean guideAllPauseFlag = false;
    // 单点导览暂停标识
    private boolean guideSinglePauseFlag = false;

    public static final int COMMENT_REQUEST = 1995;
    public static final int REQUESTCODE = 1996;
    private boolean isNavi = false;//语音指令  是否是要带路
    //    private boolean isAddPositionListener = false;
    private NaviBean naviBeanAI;

    public static String[] suffixs = {
            ".jpg", ".jpeg", ".png", "gif"
    };

    @BindView(R.id.iv_map)
    ImageView iv_map;
    @BindView(R.id.rl_map)
    RelativeLayout rl_map;
    @BindView(R.id.tv_point_name)
    TextView tv_point_name;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.rl_text_root)
    RelativeLayout rl_text_root;
    @BindView(R.id.rl_guide)
    RelativeLayout rl_guide;
    @BindView(R.id.tv_guide)
    TextView tv_guide;
    @BindView(R.id.bt_start_guide)
    Button bt_start_guide;


    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.iv_map_manage)
    TextView iv_map_manage;
    @BindView(R.id.layout_menu_navi)
    LinearLayout layout_menu_navi;//菜单导航布局
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.countdown)
    TextView countDown;//等待倒计时
    @BindView(R.id.naviVideoView)
    CommonVideoView naviVideo;
    LinearLayout point = null;

    /*新行政*/
    TextView tvPointNameImm = null;
    Button bt_start_guide_imm = null;

    LinearLayout point_view = null;

    RelativeLayout bottom_map = null;

    public TaskStatusManager guideAllTask = new TaskStatusManager();
    public TaskStatusManager guideSingle = new TaskStatusManager();
    public boolean isAITalk = false;
    private ArmLooper armLooper = new ArmLooper();
    private boolean isPointKnow;
    private boolean isNaviMap = true;//是否是地图导航模式，true地图导航，false菜单导航
    private boolean isClickMapManager = false;
    private boolean isFinishInRunningDesc = false;//单点导航，途中讲解是否完成
    private boolean isArriveSingle = false;//单点导航，是否到点
    private boolean isFinishInRunningAll = false;//一键导引，途中讲解是否完成
    private String induStry;


    ArrayList<String> pauseStrs = new ArrayList<>();

    ArrayList<String> resumeStrs = new ArrayList<>();

    ArrayList<String> endStrs = new ArrayList<>();

    ArrayList<KeyPointView> keyPointViews = new ArrayList<>();

    boolean isNaviFinishAutoExit;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        if (isPlus()) {
            return R.layout.vertical_activity_navi;
        } else {
            return R.layout.activity_navi;
        }
    }

    /**
     * 导航页面不开启语音控制动作指令
     *
     * @return
     */
    @Override
    protected boolean isOpenActionControl() {
        return false;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        openNoNetworkDialog = false;
        Constants.isNaviMove = false;

        IntentFilter filter = new IntentFilter(Constants.WAKE_UP);
        registerReceiver(receiver, filter);

        IntentFilter aFilter = new IntentFilter("com.example.BROADCAST");
        registerReceiver(aReceiver, aFilter);
        RobotManager.getInstance().addPositionListener(positionListener);

        initRecyclerView();

        pauseStrs = new Gson()
                .fromJson(SharedPreUtil.getString(SharedKey.NAVI_SOUND_CONTROL, SharedKey.NAVI_SOUND_CONTROL_PAUSE)
                        , new TypeToken<ArrayList<String>>() {
                        }.getType());

        getLog().info("pauseStrs:" + pauseStrs);

        resumeStrs = new Gson()
                .fromJson(SharedPreUtil.getString(SharedKey.NAVI_SOUND_CONTROL, SharedKey.NAVI_SOUND_CONTROL_RESUME)
                        , new TypeToken<ArrayList<String>>() {
                        }.getType());

        getLog().info("resumeStrs:" + resumeStrs);

        endStrs = new Gson()
                .fromJson(SharedPreUtil.getString(SharedKey.NAVI_SOUND_CONTROL, SharedKey.NAVI_SOUND_CONTROL_END)
                        , new TypeToken<ArrayList<String>>() {
                        }.getType());

        getLog().info("endStrs:" + endStrs);

        isNaviFinishAutoExit = SharedPreUtil.getBoolean(SharedKey.NAVI_AUTO_EXIT, SharedKey.NAVI_AUTO_EXIT, false);

        naviVideo.setOnClickListener(view -> stopPlayer());
    }

    private BroadcastReceiver aReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (workType == GUIDE_ALL) {
                // 如果正在进行导览
                if (guideAllTask.workStatus == TaskStatusManager.START) {
                    NaviActivity.this.finish();
                }
            }

            if (workType == GUIDE_SINGLE) {
                NaviActivity.this.finish();
            }
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (workType == GUIDE_ALL) {
//                // 如果正在进行导览
//                if (guideAllTask.workStatus == TaskStatusManager.START) {
//                    guideAllPauseFlag = true;
//                    isGuideAllClick = false;
//                    guide();
//                    CsjlogProxy.getInstance().warn("wake up and intterupt guide");
//                }
//            }
//
//            if (workType == GUIDE_SINGLE) {
//                if (guideSingle.workStatus == TaskStatusManager.START) {
//                    guideSinglePauseFlag = true;
//                    guideImm();
//                }
//            }

//            if(workType == GUIDE_BACK){
//                handler.post(()->{
//                    String json = SharedPreUtil.getString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY);
//                    List<Position> positionList = GsonUtils.jsonToObject(json, new TypeToken<List<Position>>() {
//                    }.getType());
//
//                    workState = GUIDE_BACK;
//                    workType = GUIDE_BACK;
//
//                    handler.postDelayed(()->{
//                        Position position = positionList.get(0);
//                        RobotManager.getInstance().robot.reqProxy.navi(position.toJson());
//                         CsjlogProxy.getInstance().debug(json);
//                    },2000);
//                });
//            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)) {
            sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_MUTE));
            sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW_MUTE));
        } else {
            sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_MUTE));
        }
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, true));
        onStop = false;
        isClickMapManager = false;
        isNaviMap = SharedPreUtil.getBoolean(SharedKey.NAVIMODE_NAME, SharedKey.NAVIMODE_KEY, true);

        if (!isNaviMap) {
            rl_map.setBackgroundResource(R.drawable.navigation_point_bg);
        } else {
            rl_map.setBackgroundResource(R.drawable.map_bg);
        }

        setNaviMode();
        Robot.getInstance().setOnNaviSearchListener(searchListener);
        // 刷新一下全局导航的数据

        if (!SharedPreUtil.getBoolean(SharedKey.ISLOADMAP, SharedKey.ISLOADMAP)) {
            Toast.makeText(this, R.string.plz_restore_map_tip, Toast.LENGTH_LONG).show();
        }
        String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
        if (TextUtils.isEmpty(json)) {
            setDefaultMap();
        } else {
            CsjlogProxy.getInstance().debug("Navi:导航地图数据\n" + json);

            setDefaultMap();
            naviBeanList = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
            }.getType());

            if (naviBeanList == null || naviBeanList.size() <= 0) {
                if (isNaviMap) {
                    removeMap();
                } else {
                    mAdapter.setLoadData(naviBeanList);
                    mAdapter.notifyDataSetChanged();
                }
                CsjlogProxy.getInstance().debug("Navi:没有数据");
            } else if (isNaviMap) {
                hasData = true;
                CsjlogProxy.getInstance().debug("Navi:获取到关键点数量：" + naviBeanList.size());
                if (keyPointViews != null && keyPointViews.size() > 0) {
                    keyPointViews.clear();
                }
                initMap(naviBeanList);
            } else if (!isNaviMap) {
                //填充菜单导航数据
                hasData = true;
                CsjlogProxy.getInstance().debug("Navi:获取到关键点数量：" + naviBeanList.size());
                mAdapter.setLoadData(naviBeanList);
                mAdapter.notifyDataSetChanged();
            }
        }

        NaviAction.getInstance().initData();
        if (isFromAI) {
            refreshMap();
        }
    }

    private void setDefaultMap() {
        if (onStop) {
            return;
        }
        menuMapGone();
        String mapPath = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.MAP_PATH);
        if (!TextUtils.isEmpty(mapPath)) {
            CsjlogProxy.getInstance().debug("Navi:地图路径：" + mapPath);
            Glide.with(this).load(mapPath).into(iv_map);
        } else {
            Glide.with(this).load(R.drawable.navi_default_map).into(iv_map);
        }
    }

    private void setNaviMode() {
        naviVideo.setVisibility(View.GONE);
        if (isNaviMap) {//地图导航
            iv_map.setVisibility(View.VISIBLE);
            layout_menu_navi.setVisibility(View.GONE);
        } else {//菜单导航
            int count = rl_map.getChildCount() - 1;
            if (count >= 2) {
                while (count > 1) {
                    rl_map.removeViewAt(count);
                    count--;
                }
            }
            iv_map.setVisibility(View.GONE);
            layout_menu_navi.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化地图，将点绘制到地图上
     */
    private void initMap(List<NaviBean> naviList) {
        int count = rl_map.getChildCount() - 1;
        if (count >= 2) {
            while (count > 1) {
                rl_map.removeViewAt(count);
                count--;
            }
        }

        for (NaviBean n : naviList) {
            addPointView(n);
        }
    }

    private void removeMap() {
        int count = rl_map.getChildCount() - 1;
        if (count >= 2) {
            while (count > 1) {
                rl_map.removeViewAt(count);
                count--;
            }
        }
    }

    private void hidePosition() {
        int count = rl_map.getChildCount() - 1;
        if (count >= 2) {
            while (count > 1) {
                View view = rl_map.getChildAt(count);
                view.setVisibility(View.GONE);
                count--;
            }
        }
    }

    private void showPosition() {
        int count = rl_map.getChildCount() - 1;
        if (count >= 2) {
            while (count > 1) {
                View view = rl_map.getChildAt(count);
                view.setVisibility(View.VISIBLE);
                count--;
            }
        }
    }

    //菜单导航地图显示
    private void menuMapVisiable(NaviBean naviBean) {
        if (!isNaviMap) {//菜单导航
            if (StrUtil.isNotBlank(naviBean.getImagePath()) && naviBean.isOpenImage()) {
                iv_map.setVisibility(View.VISIBLE);
                Glide.with(this).load(Constants.NAVI_PATH + naviBean.getImagePath()).into(iv_map);
            } else {
                iv_map.setVisibility(View.GONE);
            }
        } else {
            iv_map.setVisibility(View.VISIBLE);
            if (StrUtil.isNotBlank(naviBean.getImagePath()) && naviBean.isOpenImage()) {
                Glide.with(this).load(Constants.NAVI_PATH + naviBean.getImagePath()).into(iv_map);
            } else {
                showPosition();
            }
        }
    }

    private void menuMapGone() {
        if (!isNaviMap) {//菜单导航
            hidePosition();
            iv_map.setVisibility(View.GONE);
        } else {
            iv_map.setVisibility(View.VISIBLE);
        }
    }

    private NaviMenuAdatper mAdapter = null;

    private void initRecyclerView() {
        mAdapter = new NaviMenuAdatper(this);
        recyclerView.setAdapter(mAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        mAdapter.setItemClickListener(naviBean -> {
            showGuideSingleBtn();
            showInfo(naviBean);
        });
        recyclerView.setLayoutManager(layoutManager);
    }

    private NaviBean currentSelect = null;

    /**
     * 当用户点击后，展示地图信息
     */
    public void showInfo(NaviBean n) {
        if (n == null) {
            if (keyPointViews != null && keyPointViews.size() > 0 && keyPointViews.size() == naviBeanList.size()) {
                for (int i = 0; i < keyPointViews.size(); i++) {
                    keyPointViews.get(i).setMaker(R.drawable.icon_openmap_focuse_mark);
                }
            }
            return;
        }
        currentSelect = n;
        rl_text_root.setVisibility(View.VISIBLE);
        tv_point_name.setText(n.getName() + "(" + n.getNickName() + ")");
        if (tvPointNameImm != null) {
            point.setVisibility(View.VISIBLE);
            point_view.setVisibility(View.VISIBLE);
            tvPointNameImm.setText(n.getName() + "(" + n.getNickName() + ")");
        }
        tv_desc.setText(n.getDescContent());
        if (n.isPlayVideo()) {
            guideSingle.init();
        }
        refreshMap();
    }

    public void hideInfo() {
        rl_text_root.setVisibility(View.GONE);
        tv_point_name.setText(null);
        tv_desc.setText(null);
    }

    public void refreshMap() {
        if (keyPointViews != null && keyPointViews.size() > 0 && keyPointViews.size() == naviBeanList.size()) {
            for (int i = 0; i < keyPointViews.size(); i++) {
                if (currentSelect != null && StrUtil.isNotBlank(naviBeanList.get(i).getName()) && naviBeanList.get(i).getName().equals(currentSelect.getName())
                        && workType != GUIDE_ALL && workType != GUIDE_BACK) {
                    keyPointViews.get(i).setMaker(R.drawable.icon_openmap_mark);
                } else {
                    keyPointViews.get(i).setMaker(R.drawable.icon_openmap_focuse_mark);
                }
            }
        }
    }

    /**
     * 在有数据的情况下添加导引点到地图上
     */
    private void addPointView(NaviBean naviBean) {
        float vectorLength = Constants.isPlus()
                ? Constants.MULTIPAL_DATA_VERTICAL : Constants.MULTIPAL_DATA;
        float vectorwidth = Constants.isPlus()
                ? Constants.MULTIPAL_DATA_WIDTH_VERTICAL : Constants.MULTIPAL_DATA_WIDTH;
        KeyPointView keyPointView = new KeyPointView(this);

        keyPointView.setName(naviBean.getName());
        keyPointView.layout((int) (naviBean.left * vectorLength), (int) (naviBean.top * vectorwidth), (int) (naviBean.right * vectorLength), (int) (naviBean.bottom * vectorwidth));
        keyPointView.setTranslationX(naviBean.getTranslationX() * vectorLength);
        keyPointView.setTranslationY(naviBean.getTranslationY() * vectorwidth);

        CsjlogProxy.getInstance().debug("Navi:left=" + naviBean.left + " top=" + naviBean.top + " right=" + naviBean.right + " bottom=" + naviBean.bottom);

        keyPointView.setName(naviBean.getName());
        keyPointView.setOnClickListener(v -> {
            workType = GUIDE_SINGLE;
            showInfo(naviBean);
            showGuideSingleBtn();
        });

        keyPointViews.add(keyPointView);
        rl_map.addView(keyPointView);
    }

    @Override
    protected void beforeSetContentView() {
        super.beforeSetContentView();
        if (Constants.Scene.CurrentScene.equals(Constants.Scene.CheGuanSuo) || Constants.Scene.CurrentScene.equals(Constants.Scene.XingZheng)) {
            Constants.Scene.CurrentScene = "";
        }
    }

    @Override
    public void init() {
        super.init();
        sendBroadcast(new Intent("com.csjbot.blackgaga.naviinit"));
        isFromAI = getIntent().getBooleanExtra(BRouterKey.FROM_AI_GUIDE, false);
        isPointKnow = getIntent().getBooleanExtra(BRouterKey.IS_POINT_KNOW, false);
        mPresenter = new NaviPresenter();
        mPresenter.initView(this);
        player = NaviPlayer.getInstance(this);

        getTitleView().setSettingLayoutGone();
        if (!isFromAI) {
            String msg = getString(R.string.navi_init_text);
            String displayMsg = getString(R.string.navi_init_text);
//            if (Constants.Scene.CurrentScene.equals(Constants.Scene.JiuDianScene)) {
//                msg = getString(R.string.navi_where_you_want_go);
//                displayMsg = getString(R.string.navi_where_you_want_go);
//            } else if (Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)) {
//                displayMsg = getString(R.string.navi_fuzhuang_speak);
//                msg = getString(R.string.navi_fuzhuang_display);
//            }
            speak(msg);
            setRobotChatMsg(displayMsg);
        } else {
            if (!isPointKnow)
                speak(R.string.navi_hint_ai, true);
            else {
                currentSelect = NaviAction.getInstance().current;
                if (currentSelect != null) {
                    if (SharedPreUtil.getBoolean(SharedKey.NAVIMODE_NAME, SharedKey.NAVIMODE_KEY, true)) {//地图导航
                        naviAIConfirm(currentSelect);
                    } else {//菜单导航
                        showGuideSingleBtn();
                        guideImm();
                    }
                }
            }
        }

        mAI = NaviAI.newInstance();
        mAI.initAI(this);
    }

    private boolean naviSoundControlCheck(String text) {
        getLog().info("naviSoundControlCheck:" + text);
        if (pauseStrs == null || pauseStrs.size() == 0) {
            return false;
        }
        if (resumeStrs == null || resumeStrs.size() == 0) {
            return false;
        }
        if (endStrs == null || endStrs.size() == 0) {
            return false;
        }
        for (String str : pauseStrs) {
            getLog().info("pauseStrs:child" + str);
            if (text.contains(str)) {
                getLog().info("pauseStrs:success");
                getLog().info("pauseStrs:workType:" + workType);
                if (workType == GUIDE_ALL) {
                    if (guideAllTask.workStatus == TaskStatusManager.START) {
                        getLog().info("pauseStrs:performClick");
                        tv_guide.performClick();
                    }
                } else if (workType == GUIDE_SINGLE) {
                    if (guideSingle.workStatus == TaskStatusManager.START) {
                        bt_start_guide.performClick();
                        if (bt_start_guide_imm != null)
                            bt_start_guide_imm.performClick();
                    }
                }
                return true;
            }
        }
        for (String str : resumeStrs) {
            if (text.contains(str)) {
                if (workType == GUIDE_ALL) {
                    if (guideAllTask.workStatus == TaskStatusManager.PAUSE) {
                        tv_guide.performClick();
                    }
                } else if (workType == GUIDE_SINGLE) {
                    if (guideSingle.workStatus == TaskStatusManager.PAUSE) {
                        bt_start_guide.performClick();
                        if (bt_start_guide_imm != null)
                            bt_start_guide_imm.performClick();
                    }
                }
                return true;
            }
        }
        for (String str : endStrs) {
            if (text.contains(str)) {
                backWelcome.performClick();
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean keywordInterrupt(String text) {
        if (pauseStrs == null || pauseStrs.size() == 0) {
            return false;
        }
        if (resumeStrs == null || resumeStrs.size() == 0) {
            return false;
        }
        if (endStrs == null || endStrs.size() == 0) {
            return false;
        }
        getLog().info("NaviActivity:keywordInterrupt");
        for (String str : pauseStrs) {
            if (text.contains(str)) {
                getLog().info("NaviActivity:keywordInterrupt:true");
                return true;
            }
        }
        for (String str : resumeStrs) {
            if (text.contains(str)) {
                getLog().info("NaviActivity:keywordInterrupt:true");
                return true;
            }
        }
        for (String str : endStrs) {
            if (text.contains(str)) {
                getLog().info("NaviActivity:keywordInterrupt:true");
                return true;
            }
        }
        return super.keywordInterrupt(text);
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        getLog().info("onSpeechMessage");
        if (CsjLanguage.isEnglish()) {
            return false;
        }
        if (mAI.isExistIntent(text)) {//只显示控制机器人的话术
            setCustomerChatMsg(text);
        } else {
            if (guideSingle.workStatus != TaskStatusManager.START && guideAllTask.workStatus != TaskStatusManager.START && workType != GUIDE_BACK) {
                for (UnreachablePointBean pointBean : Constants.sUnreachablePoints) {
                    if (text.contains(pointBean.getLocation())) {
                        List<ChatBean.PictureBean> list = new ArrayList<>();
                        ChatBean.PictureBean pictureBean = new ChatBean.PictureBean();
                        pictureBean.setPictureName(pointBean.getFileName());
                        pictureBean.setPictureUrl(pointBean.getFileUrl());
                        list.add(pictureBean);
                        speak(pointBean.getSpeech());
                        setRobotPictureChatMsg(pointBean.getSpeech(), list);
                        return true;
                    }
                }
                prattle(SpeakUtil.getNaviNoMatchSpeak());
            }
        }
        text = text.subSequence(0, text.length() - 1).toString();
        if (isNavi) {
            if (getString(R.string.navi_confirm1).equalsIgnoreCase(text) || getString(R.string.navi_confirm2).equalsIgnoreCase(text) || getString(R.string.navi_confirm3).equalsIgnoreCase(text) || getString(R.string.navi_confirm4).equalsIgnoreCase(text)
                    || getString(R.string.navi_confirm5).equalsIgnoreCase(text) || getString(R.string.navi_confirm6).equalsIgnoreCase(text) || getString(R.string.navi_confirm7).equalsIgnoreCase(text) || getString(R.string.navi_confirm8).equalsIgnoreCase(text)) {
                setCustomerChatMsg(text);
                isAITalk = true;
                guideImm();
            } else if (getString(R.string.navi_refuse1).equalsIgnoreCase(text) || getString(R.string.navi_refuse2).equalsIgnoreCase(text) || getString(R.string.navi_refuse3).equalsIgnoreCase(text) || getString(R.string.navi_refuse4).equalsIgnoreCase(text)
                    || getString(R.string.navi_refuse5).equalsIgnoreCase(text) || getString(R.string.navi_refuse6).equalsIgnoreCase(text)) {
                setCustomerChatMsg(text);
                showInfo(null);
                speak(R.string.navi_need_other_service, true);
            } else {
                showInfo(null);
            }
            isNavi = false;
        }

        if (naviSoundControlCheck(text)) {
            return true;
        }

        NaviAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
//            prattle(SpeakUtil.getNaviNoMatchSpeak());
            if (super.onSpeechMessage(text, answerText)) {//解决返回 子界面和整体话术冲突
                return false;
            }
//            prattle(answerText);
        }
        return true;
    }

    public void naviAIConfirm(NaviBean naviBean) {
        isNavi = true;
        naviBeanAI = naviBean;
        workType = GUIDE_DEFAULT;
        showInfo(naviBeanAI);
        showGuideSingleBtn();
        speak(naviBean.getName() + getString(R.string.navi_need_go), true);
    }

    @Override
    protected boolean isDisableEntertainment() {
        return true;
    }

    @Override
    protected boolean isHideUserText() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    long clickTime = 0;

    // 一键导引
    @OnClick(R.id.tv_guide)
    public void guide() {
//        RobotProxy.getInstance().cancelTask();//结束巡航任务
        StateMachineManager.getInstance().setInitState(NaviTaskStatus.AWAIT);
        if (Constants.Scene.CurrentScene.equals(Constants.Scene.Dianli)) {
            if (tv_guide.getText().equals(getString(R.string.guide))) {
                Intent intent = new Intent(this, NaviGuideAllPWDActivity.class);
                startActivityForResult(intent, REQUESTCODE);
            } else {
                guideAll();
            }
        } else {
            guideAll();
        }
    }

//    private void addPostionListener() {
//        if (!isAddPositionListener) {
//            isAddPositionListener = true;
//            RobotManager.getInstance().addPositionListener(positionListener);
//        }
//    }

    public void guideAll() {
        if (!isExistYingbinPoint()) {
            return;
        }
//        addPostionListener();
        // 限制点击频率
        hideGuideSingleBtn();
        guideClickCount++;
        long lastTime = System.currentTimeMillis() - clickTime;
        clickTime = System.currentTimeMillis();

        if (lastTime >= 3000) {
            guideClickCount = 0;
            CsjlogProxy.getInstance().debug("上一次点击距现在时间超过3s");
        }

        if (guideClickCount >= 1 && isGuideAllClick) {
            Toast.makeText(context, R.string.click_too_more, Toast.LENGTH_SHORT).show();
            tv_guide.setEnabled(false);
            handler.postDelayed(() -> {
                guideClickCount = 0;
                tv_guide.setEnabled(true);
            }, 3000);
            return;
        }

        isGuideAllClick = true;

        // 获取一键导览数据
        String json = SharedPreUtil.getString(SharedKey.GUIDE_NAME, SharedKey.GUIDE_KEY);
        workList = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
        }.getType());

        // 没有数据，跳转到设置界面
        if (workList == null || workList.size() == 0) {
            speak(R.string.set_none_navi_guide, new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {
                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    BRouter.getInstance()
                            .build(BRouterPath.SETTING_CONFIRM)
                            .withString(BRouterKey.ACTIVITY_NAME, BRouterPath.NAVI_SETTING)
                            .withBoolean(BRouterKey.GUIDE_ALL, true)
                            .navigation();
                }
            }, true);
            return;
        }

//        bt_start_guide.setEnabled(false);

        // 工作种类判断，正在单项导览就无法进行
        if (guideSingle.workStatus == TaskStatusManager.START) {
            speak(R.string.working_ing, true);
            return;
        } else if (workType == GUIDE_BACK) {
            speak(R.string.working_ing, true);
            return;
        }

        hideWaitTime();

        workType = GUIDE_ALL;

        switch (guideAllTask.workStatus) {
            case TaskStatusManager.START:
                guideAllPauseFlag = true;
                guideAllTask.pause();
                pauseGuideAll();
                pauseGuideAllPlay();
                break;

            case TaskStatusManager.PAUSE:
                // 从暂停状态恢复
                guideAllPauseFlag = false;
                guideAllTask.resume();
                resumeGuideAll();
                resumeGuideAllPlay();
                break;

            case TaskStatusManager.AWAIT:
                // 开始工作
//                ServerFactory.getActionInstance().nodAction();
                guideAllPauseFlag = false;
                guideAllTask.start();
                startGuideAll();
                break;

            default:
                break;
        }
    }

    /**
     * 开始一键导览
     */
    public void startGuideAll() {
        if (guideSingle.workStatus == TaskStatusManager.START) {
            speak(R.string.working_ing, true);
            CsjlogProxy.getInstance().info("Navi:正在执行单步导览，一键导览无法进行");
            // 进入这个方法之前已经将这个任务的标识置为了开始，所以重置一下
            guideAllTask.init();
            return;
        }

        tv_guide.setEnabled(false);
        startingGuideAll();
        speak(getStartTip(workList.get(0)), new OnSpeakListener() {
            @Override
            public void onSpeakBegin() {
            }

            @Override
            public void onCompleted(SpeechError speechError) {
                handler.sendEmptyMessageDelayed(GUIDE_ALL_TIMEOUT, 5000);
                runGuideAllTask(workList);
            }
        }, true);
    }

    /**
     * 暂停一键导览
     */
    public void pauseGuideAll() {
        tv_guide.setEnabled(false);
        if (workList != null) {
            CsjlogProxy.getInstance().debug("workList size: " + workList.size() + " execount: " + executeTaskCount);
        }
        if (currentRunnable != null && workList != null &&
                executeTaskCount >= workList.size()) {
            handler.removeCallbacks(currentRunnable);
            CsjlogProxy.getInstance().debug("取消延时回调");
        } else {
            CsjlogProxy.getInstance().debug("为什么不走？？？");
        }

        handler.removeMessages(GUIDE_ALL_TIMEOUT);
        cancelTask();
    }

    public void pauseGuideAllPlay() {
        if (executeTaskCount >= workList.size()) {
            return;
        }
        NaviBean naviBean = workList.get(executeTaskCount);
        if (naviBean.isOpenMusic()) {//导览中播放背景音乐
            player.stopPlay();
        }
        if (naviBean.isOpenSpeak()) {//导览中播放途中讲解文字
            mSpeak.stopSpeaking();
        }
        stopPlayer();
        return;
    }

    /**
     * 恢复一键导览
     */
    public void resumeGuideAll() {
        guideAllTask.resume();
        if (executeTaskCount >= workList.size()) {
            tv_guide.setText(R.string.stop_guide);
            iv_icon.setBackgroundResource(R.drawable.btn_stop_icon);
        } else {
            startingGuideAll();
        }
        runGuideAllTaskSpeek();
    }

    public void resumeGuideAllPlay() {
        if (executeTaskCount >= workList.size()) {
            return;
        }
        NaviBean naviBean = workList.get(executeTaskCount);
        if (naviBean.isOpenMusic()) {//导览中播放背景音乐
            player.stopPlay();
        }
        if (naviBean.isOpenSpeak()) {//导览中播放途中讲解文字
            mSpeak.resumeSpeaking();
        }
        return;
    }

    private void runGuideAllTaskSpeek() {
        if (executeTaskCount >= workList.size()) {
            runGuideAllTask(workList);
            return;
        }
        speak(getStartTip(workList.get(executeTaskCount)), new OnSpeakListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                runGuideAllTask(workList);
            }
        }, true);
    }

    private void runGuideAllTask(List<NaviBean> naviBeanList) {
        if (naviBeanList == null) {
            CsjlogProxy.getInstance().debug("Navi:要执行的任务为空?");
            return;
        }
        CsjlogProxy.getInstance().info("Navi:开始一键导引，executeTaskCount = {}， workList size = {} ", executeTaskCount, workList.size());
        if (executeTaskCount >= workList.size()) {
            CsjlogProxy.getInstance().debug("Navi:导引所有任务点已经走完，导引结束");
            tv_guide.setText(R.string.guide);
            iv_icon.setBackgroundResource(R.drawable.btn_start_icon);
            runOnUiThread(() -> {
                if (guideAllTask.workStatus == TaskStatusManager.START) {
                    currentRunnable = () -> speak(getString(R.string.navi_finish), new OnSpeakListener() {
                        @Override
                        public void onSpeakBegin() {

                        }

                        @Override
                        public void onCompleted(SpeechError speechError) {
                            backWelCome();
                            hideInfo();
                            NaviActivity.this.executeTaskCount = 0;
                            enableButton();
                        }
                    }, true);
                }
                handler.postDelayed(currentRunnable, 1000);
            });
        } else {
            Log.e("sunxy", "走点runGuideAllTask" + executeTaskCount);
            NaviBean naviBean = workList.get(executeTaskCount);
            showInfo(naviBean);
            hideWaitTime();
            RobotManager.getInstance().robot.reqProxy.navi(naviBean.toJson());
            CsjlogProxy.getInstance().debug("Navi:走点消息已发送：" + naviBean.toJson());
        }
    }

    // 失败重试次数
    private volatile int retryCount = 1;

    public void guideAllMoveResult(int code) {
        if (onStop) {
            return;
        }

        // 如果暂停，则不继续执行
        if (guideAllPauseFlag) {
            return;
        }

        if (code == 0) {
            tv_guide.setEnabled(true);
            stopPlayer();
            player.stopPlay();
            currentRunnable = null;
            NaviBean naviBean = workList.get(executeTaskCount);
            executeTaskCount++;
            CsjlogProxy.getInstance().debug("Navi:地点名称：" + naviBean.getName() + " 描述：" + naviBean.getDescription());
            hidePosition();
//            removeMap();
            menuMapVisiable(naviBean);
            if (naviBean.isOpenSpeak()) {//两种情况 一种只有途中讲解  一种是途中讲解和背景音乐都有
                if (isFinishInRunningAll) {
                    guideAllSpeakMoveResult(naviBean);
                }
            } else {//两种情况 一种是只有背景音乐  一种是背景音乐和途中讲解均无
                guideAllSpeakMoveResult(naviBean);
            }
            retryCount = 1;
        } else if (code == 20004) {
            tv_guide.setEnabled(false);
            runOnUiThread(() -> handler.postDelayed(() -> {
                Toast.makeText(context, R.string.route_fail_next, Toast.LENGTH_SHORT).show();
                executeTaskCount++;
                tv_guide.setEnabled(true);
                runGuideAllTaskSpeek();
//                runGuideAllTask(workList);
            }, 1000));
            retryCount = 1;
        } else if (code == 20100) {
            if (retryCount / 3 == 0) {
                executeTaskCount++;
                retryCount = 1;
                runOnUiThread(() -> Toast.makeText(context, R.string.this_failed_next, Toast.LENGTH_SHORT).show());
            } else {
                retryCount++;
            }

            tv_guide.setEnabled(false);
            runOnUiThread(() -> handler.postDelayed(() -> {
//                Toast.makeText(context, "路径规划失败，即将前往下个点", Toast.LENGTH_SHORT).show();
                tv_guide.setEnabled(true);
                runGuideAllTask(workList);
            }, 1000));
        } else {
            tv_guide.setEnabled(false);
            runOnUiThread(() -> handler.postDelayed(() -> {
                Toast.makeText(context, R.string.route_fail_next, Toast.LENGTH_SHORT).show();
                executeTaskCount++;
                tv_guide.setEnabled(true);
                runGuideAllTaskSpeek();
//                runGuideAllTask(workList);
            }, 1000));
            retryCount = 1;
        }
    }

    private String getArriveTip(NaviBean naviBean) {
        String str = "";
        if (naviBean != null) {
            if (StrUtil.isNotBlank(naviBean.getArriveTipContent())) {
                str = naviBean.getArriveTipContent();
            } else {
                str = naviBean.getName() + getString(R.string.arrived);
            }
        }
        return str;
    }

    private String getStartTip(NaviBean naviBean) {
        String str;
        if (naviBean == null || StrUtil.isBlank(naviBean.getStartTipContent())) {
            str = /*naviBean.getName() + */getString(R.string.go_with_next);
        } else {
            str = naviBean.getStartTipContent();
        }
        return str;
    }

    private void guideAllSpeakMoveResult(NaviBean naviBean) {
//        player.stopPlay();
        isFinishInRunningAll = false;
        if (guideAllPauseFlag) return;
//        stopPlayer();
        runOnUiThread(() -> speak(getArriveTip(naviBean), new OnSpeakListener() {
            @Override
            public void onSpeakBegin() {
            }

            @Override
            public void onCompleted(SpeechError speechError) {
                if (guideAllPauseFlag) return;
                tv_guide.setEnabled(true);
                currentRunnable = () -> {
                    if (guideAllPauseFlag) return;
                    speak(naviBean.getDescContent(), new OnSpeakListener() {
                        @Override
                        public void onSpeakBegin() {
                        }

                        @Override
                        public void onCompleted(SpeechError speechError) {
                            setDefaultMap();
                            if (isNaviMap) {
                                showPosition();
                            }
                            if (guideAllPauseFlag) return;
                            currentRunnable = () -> {
                                if (guideAllPauseFlag) {
                                    return;
                                }
                                hideWaitTime();
                                //                            runGuideAllTask(workList);
                                runGuideAllTaskSpeek();
                                showGuideAllBtn();
                                enableButton();
                            };
                            int time = naviBean.getWaitTime() == 0 ? 2000 : naviBean.getWaitTime();
                            handler.postDelayed(currentRunnable, time);
                            showWaitTime(time);
                        }
                    }, true);
                };
                handler.postDelayed(currentRunnable, 1000);
            }
        }, true));
    }

    private void pauseGuideSingle() {
        handler.removeMessages(GUIDE_SINGLE_TIMEOUT);
        cancelTask();
        pauseGuidePlay();
        stopPlayer();
        currentSelect = null;
        refreshMap();
    }

    //暂停导览过程中背景音和途中讲解的暂停工作
    private void pauseGuidePlay() {
        if (currentSelect != null) {
            if (currentSelect.isOpenMusic()) {//导览中播放背景音乐
                player.stopPlay();
            }
            if (currentSelect.isOpenSpeak()) {//导览中播放途中讲解文字
                mSpeak.pauseSpeaking();
            }
        }
        return;
    }

    private void resumeGuideSingle() {
        startGuideSingle();
        resumeGuidePlay();
//        startPlayer(currentSelect);
    }

    //恢复导览过程中背景音和途中讲解的恢复工作
    private void resumeGuidePlay() {
        //V2.2.1 会报错误 NullPointerException
        if (currentSelect != null) {
            if (currentSelect.isOpenMusic()) {//导览中播放背景音乐
                player.stopPlay();
            }

            if (currentSelect.isOpenSpeak()) {//导览中播放途中讲解文字
                mSpeak.resumeSpeaking();
            }
        }
    }

    private void initGuideAll() {
        tv_guide.setText(R.string.guide);
        iv_icon.setBackgroundResource(R.drawable.btn_start_icon);
        tv_guide.setEnabled(true);
    }

    private void initGuideSingle() {
        bt_start_guide.setText(R.string.go_imm);
        if (bt_start_guide_imm != null) {
            bt_start_guide_imm.setText(R.string.go_imm);
            bt_start_guide_imm.setEnabled(true);
        }
        hideInfo();
        bt_start_guide.setEnabled(true);

    }

    /**
     * 单项导览移动结果回调
     */
    public void guideSingleMoveResult(NaviBean naviBean, boolean isSuccess) {
        if (guideSinglePauseFlag) {
            return;
        }

        // 已经结束了
        isArriveSingle = true;
        CsjlogProxy.getInstance().debug("Navi:单项导航");
        if (!isSuccess) {
            guideImm();
            return;
        }
        player.stopPlay();
        stopPlayer();
        hidePosition();
        menuMapVisiable(naviBean);
        if (!isFromAI) singleBack();
        else {
            if (currentSelect != null && currentSelect.isOpenSpeak()) {//两种情况 一种只有途中讲解  一种是途中讲解和背景音乐都有
                if (isFinishInRunningDesc) {
                    guideSingleSpeakDesc();
                }
            } else {//两种情况 一种是只有背景音乐  一种是背景音乐和途中讲解均无
                guideSingleSpeakDesc();
            }
        }
    }

    private void guideSingleSpeakDesc() {
        if (guideSinglePauseFlag) return;
        runOnUiThread(() -> speak(getArriveTip(currentSelect), new OnSpeakListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                if (guideSinglePauseFlag) return;
                currentRunnable = () -> {
                    if (guideSinglePauseFlag) return;
                    String content = "";
                    if (currentSelect != null) {
                        content = currentSelect.getDescContent();
                    }
                    speak(content, new OnSpeakListener() {
                        @Override
                        public void onSpeakBegin() {
                        }

                        @Override
                        public void onCompleted(SpeechError speechError) {
                            if (guideSinglePauseFlag) return;
                            hideGuideSingleBtn();
                            currentRunnable = () -> {
                                if (guideSinglePauseFlag) return;
                                hideWaitTime();
                                hideInfo();
                                showGuideAllBtn();
                                String name = "";
                                if (currentSelect != null) {
                                    name = currentSelect.getName();
                                }
                                speak(name + getString(R.string.arrive_already_3q), new OnSpeakListener() {
                                    @Override
                                    public void onSpeakBegin() {
                                    }

                                    @Override
                                    public void onCompleted(SpeechError speechError) {
                                        if (guideSinglePauseFlag) return;
                                        setDefaultMap();
                                        if (isNaviMap) {
                                            showPosition();
                                        }
                                        stopPlayer();
                                        BRouter.getInstance()
                                                .build(BRouterPath.NAVI_GUIDE_COMMENT)
                                                .withBoolean(BRouterKey.ISFROMAI, true)
                                                .get()
                                                .navigation(NaviActivity.this, COMMENT_REQUEST);
                                    }
                                });
                            };
                            int time = 2000;
                            if (currentSelect != null) {
                                time = currentSelect.getWaitTime() == 0 ? 2000 : currentSelect.getWaitTime();
                            }
                            handler.postDelayed(currentRunnable, time);
                            showWaitTime(time);
                        }
                    }, true);
                };
                handler.postDelayed(currentRunnable, 2000);
            }
        }, true));
    }

    private void singleBack() {
        if (guideSinglePauseFlag) {
            return;
        }
        if (currentSelect != null && currentSelect.isOpenSpeak()) {//两种情况 一种只有途中讲解  一种是途中讲解和背景音乐都有
            if (isFinishInRunningDesc) {
                guideSingleBackSpeakDesc();
            }
        } else {//两种情况 一种是只有背景音乐  一种是背景音乐和途中讲解均无
            guideSingleBackSpeakDesc();
        }
    }

    private void guideSingleBackSpeakDesc() {
        player.stopPlay();
        if (guideSinglePauseFlag) return;
        runOnUiThread(() -> speak(getArriveTip(currentSelect), new OnSpeakListener() {
            @Override
            public void onSpeakBegin() {
            }

            @Override
            public void onCompleted(SpeechError speechError) {
                if (guideSinglePauseFlag) return;
                currentRunnable = () -> {
                    if (guideSinglePauseFlag) return;
                    String content = "";
                    if (currentSelect != null) {
                        content =currentSelect.getDescContent();
                    }
                    speak(content, new OnSpeakListener() {
                        @Override
                        public void onSpeakBegin() {
                        }

                        @Override
                        public void onCompleted(SpeechError speechError) {
                            if (guideSinglePauseFlag) {
                                return;
                            }
                            hideGuideSingleBtn();
                            setDefaultMap();
                            if (isNaviMap) {
                                showPosition();
                            }
                            int time = 3000;
                            if (currentSelect != null) {
                                time = currentSelect.getWaitTime() == 0 ? 3000 : currentSelect.getWaitTime();
                            }
                            showWaitTime(time);
                            handler.postDelayed(() -> {
                                if (guideSinglePauseFlag) {
                                    return;
                                }
                                hideWaitTime();
                                hideInfo();
                                BRouter.getInstance()
                                        .build(BRouterPath.NAVI_GUIDE_COMMENT)
                                        .withBoolean(BRouterKey.ISFROMAI, true)
                                        .get()
                                        .navigation(NaviActivity.this, COMMENT_REQUEST);

                                enableButton();
                            }, time);
                        }
                    }, true);
                };
                handler.postDelayed(currentRunnable, 2000);
            }
        }, true));
    }

    private void commentFinishBack() {
        speak(R.string.navi_finish, new OnSpeakListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                hideGuideSingleBtn();
                bt_start_guide.setText(R.string.go_imm);
                if (bt_start_guide_imm != null)
                    bt_start_guide_imm.setText(R.string.go_imm);
                workType = GUIDE_BACK;
                backWelCome();
            }
        }, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == COMMENT_REQUEST) {
            commentFinishBack();
        } else if (requestCode == REQUESTCODE) {
            guideAll();
        }
    }

    /**
     * 分发移动结果
     */
    OnPositionListener positionListener = new OnPositionListener() {
        @Override
        public void positionInfo(String json) {

        }

        @Override
        public void moveResult(String json) {
            if (StateMachineManager.getInstance().getCurrentState() != NaviTaskStatus.AWAIT) {
                return;
            }
            runOnUiThread(() -> {
                handler.removeMessages(MSG_TIMEOUT);
                handler.removeMessages(MSG_TIMEOUTMSG_TIMEOUT);
                if (currentRunnable != null) {
                    handler.removeCallbacks(currentRunnable);
                }
                if (!openListener) return;

                CsjlogProxy.getInstance().debug("进入了移动结果回调，返回json：" + json);
                boolean isSuccess = false;
                int error_code = 0;
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    error_code = jsonObj.optInt("error_code");

                    if (error_code != 0) {
                        CsjlogProxy.getInstance().debug("Navi:走点失败，状态码：" + error_code);
                        isSuccess = false;
                    } else {
                        isSuccess = true;
                    }

                    // 前方有障碍物
                    if (error_code == 20100) {
                        speak(R.string.turn_back, true);
                        startGuideAll();
                        return;
                    }

                    // 发送过于频繁（已经限制）
                    if (error_code == 20006) {
//                         CsjlogProxy.getInstance().debug("发送过于频繁");
//                        if (workType == GUIDE_BACK) backYinbin();
                    }
                } catch (Exception e) {
                    CsjlogProxy.getInstance().error(e);
                    e.printStackTrace();
                }

                if (workType == GUIDE_ALL) {
                    CsjlogProxy.getInstance().debug("Navi:开始分发移动结果");
                    armLooper.stopLooper();
                    guideAllMoveResult(error_code);
                    handler.removeMessages(MSG_TIMEOUT);
                    handler.removeMessages(MSG_TIMEOUTMSG_TIMEOUT);
                } else if (workType == GUIDE_SINGLE) {
                    armLooper.stopLooper();
                    guideSingleMoveResult(currentSelect, isSuccess);
                    // 开启到点超时计时
                    handler.removeMessages(MSG_TIMEOUT);
                    handler.removeMessages(MSG_TIMEOUTMSG_TIMEOUT);
                } else if (workType == GUIDE_BACK) {
                    handler.removeMessages(MSG_TIMEOUT);
                    handler.removeMessages(MSG_TIMEOUTMSG_TIMEOUT);
                    if (!isSuccess) {
                        backWelCome();
                    } else {
                        armLooper.stopLooper();
                        workType = GUIDE_DEFAULT;
                        speak(R.string.back_already, new OnSpeakListener() {
                            @Override
                            public void onSpeakBegin() {

                            }

                            @Override
                            public void onCompleted(SpeechError speechError) {
                                Constants.isNaviMove = false;
                                handler.post(() -> {
                                    try {
                                        if (isNaviFinishAutoExit) {
                                            NaviActivity.this.finish();
                                        }
                                    } catch (Exception e) {
                                    }
                                });
                            }
                        }, true);
                        enableButton();
                        showGuideAllBtn();
                        hideGuideSingleBtn();
//                        showGuideSingleBtn();
//                        bt_start_guide.setEnabled(true);
                        mAdapter.setIndex(-1);
                        // 重置标识
                        currentSelect = null;
                        executeTaskCount = 0;
                        guideSingle.init();
                        guideAllTask.init();
                        initGuideSingle();
                        initGuideAll();
                        nodeCount = 0;
                    }
                }
            });
        }

        @Override
        public void moveToResult(String json) {
            // 删除超时消息
            if (StateMachineManager.getInstance().getCurrentState() != NaviTaskStatus.AWAIT) {
                return;
            }
            Constants.isNaviMove = true;
            handler.removeMessages(GUIDE_ALL_TIMEOUT);
            handler.removeMessages(GUIDE_SINGLE_TIMEOUT);
            if (!openListener) return;
            CsjlogProxy.getInstance().debug("消息下发成功");
            runOnUiThread(() -> {
                // 表示消息下发成功
                disableButton();
                if (workType == GUIDE_ALL) {
                    armLooper.startLooper();
                    if (executeTaskCount == 0 && nodeCount == 0) {
                        ServerFactory.getActionInstance().nodAction();
                        nodeCount++;
                    }

                    setDefaultMap();
                    inRunningGuideAll();
                    currentRunnable = () -> {
                        Log.e("sunxy", "消息下发成功" + executeTaskCount);
                        inRunningPlayAll();
                    };
                    handler.postDelayed(currentRunnable, 1500);
                    startPlayer(workList.get(executeTaskCount));
                    // 发送到点超时计时消息
                    handler.sendEmptyMessageDelayed(MSG_TIMEOUT, 20000);
                } else if (workType == GUIDE_SINGLE) {
                    armLooper.startLooper();
                    ServerFactory.getActionInstance().nodAction();

                    inRunningGuideSingle();
                    currentRunnable = () -> {
                        inRunningPlaySingle(currentSelect);
                    };
                    handler.postDelayed(currentRunnable, 1500);
                    startPlayer(currentSelect);
                    handler.sendEmptyMessageDelayed(MSG_TIMEOUT, 20000);
                } else if (workType == GUIDE_BACK) {
                    armLooper.startLooper();
                    // 返回迎宾点消息已经下发，重置一键导览与单步导览状态
                    initGuideAll();
                    initGuideSingle();
                    guideSingle.init();
                    guideAllTask.init();
                }
            });
        }

        @Override
        public void cancelResult(String json) {
            Constants.isNaviMove = false;
            if (StateMachineManager.getInstance().getCurrentState() != NaviTaskStatus.AWAIT) {
                return;
            }
            handler.removeMessages(CANCEL_TASK);
            try {
                if (!openListener) return;
                JSONObject jo = new JSONObject(json);
                int error_code = jo.optInt("error_code");
                if (error_code == 0) {
                    runOnUiThread(() -> {
                        enableButton();
                        // 取消任务成功
                        CsjlogProxy.getInstance().debug("取消任务成功");
                        handler.removeMessages(CANCEL_TASK);
                        if (workType == GUIDE_BACK) {
                            backWelcome.setEnabled(true);
                        }

                        if (workType == GUIDE_SINGLE) {
                            if (!isClickMapManager) {
                                speak(R.string.current_cancel, true);
                            }
                            isArriveSingle = false;
                            hideWaitTime();
                            guideSinglePauseFlag = true;
                            setDefaultMap();
                            player.stopPlay();
                            stopPlayer();
                            if (isNaviMap) {
                                showPosition();
//                                initMap(naviBeanList);
                            }
                            showGuideAllBtn();
                            hideGuideSingleBtn();
//                            showGuideSingleBtn();
                            if (currentRunnable != null) {
                                handler.removeCallbacks(currentRunnable);
                            }
                            bt_start_guide.setText(R.string.go_imm);
                            bt_start_guide.setEnabled(true);
                            if (bt_start_guide_imm != null) {
                                bt_start_guide_imm.setText(R.string.go_imm);
                                bt_start_guide_imm.setEnabled(true);
                            }
                            guideSingle.pause();
                            armLooper.stopLooper();
                        }

                        guideAllTask.pause();
                        if (workType == GUIDE_ALL) {
                            tv_guide.setEnabled(true);
                            tv_guide.setText(R.string.guide);
                            armLooper.stopLooper();
                            iv_icon.setBackgroundResource(R.drawable.btn_start_icon);
                            if (currentRunnable != null) {
                                handler.removeCallbacks(currentRunnable);
                            }
                            hideWaitTime();
                            guideAllPauseFlag = true;
                            player.stopPlay();
                            setDefaultMap();
                            if (isNaviMap) {
                                showPosition();
//                                initMap(naviBeanList);
                            }
                            showGuideAllBtn();
                            hideGuideSingleBtn();
//                            showGuideSingleBtn();
                            if (!isClickMapManager) {
                                speak(R.string.pause_guide_all_task, true);
                            }
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private boolean isExistYingbinPoint() {
        boolean isExist = true;
        String json = SharedPreUtil.getString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY);
        List<Position> positionList = GsonUtils.jsonToObject(json, new TypeToken<List<Position>>() {
        }.getType());

        if (positionList == null || positionList.size() <= 0) {
            Toast.makeText(context, R.string.no_yinbin, Toast.LENGTH_SHORT).show();
            speak(R.string.no_yinbin, true);
            CsjlogProxy.getInstance().debug("Navi:尚未设置迎宾点");
            isExist = false;
        }
        return isExist;
    }

    @BindView(R.id.rl_back_welcome)
    View backWelcome;

    /**
     * 返回迎宾点
     */
    @OnClick(R.id.rl_back_welcome)
    public void backWelCome() {
//        RobotProxy.getInstance().cancelTask();//结束巡航任务
        StateMachineManager.getInstance().setInitState(NaviTaskStatus.AWAIT);
//        addPostionListener();
        setDefaultMap();
        if (isNaviMap) {
            showPosition();
        }
        player.stopPlay();
        stopPlayer();
        hideWaitTime();

        if (!isExistYingbinPoint()) {
            return;
        }

        if (workType == GUIDE_BACK) {
//            return;
        }
        stopSpeak();
        backWelcome.setEnabled(false);
        cancelTask();
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable);
        }
        executeTaskCount = 0;
        workType = GUIDE_BACK;
        refreshMap();
        String json = SharedPreUtil.getString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY);
        List<Position> positionList = GsonUtils.jsonToObject(json, new TypeToken<List<Position>>() {
        }.getType());
        Position position = positionList.get(0);

        handler.postDelayed(() -> {
            RobotManager.getInstance().robot.reqProxy.navi(position.toJson());
            CsjlogProxy.getInstance().debug(json);
        }, 1000);

    }

    /**
     * 立即前往，单项导航
     */
    @OnClick({R.id.bt_start_guide})
    public void guideImm() {
//        RobotProxy.getInstance().cancelTask();//结束巡航任务
        CsjlogProxy.getInstance().info("Navi:返回迎宾点");
        StateMachineManager.getInstance().setInitState(NaviTaskStatus.AWAIT);
        if (!isExistYingbinPoint()) {
            return;
        }
//        addPostionListener();
        hideGuideAllBtn();
        isArriveSingle = false;
        if (guideAllTask.workStatus == TaskStatusManager.START) {
            speak(R.string.working_ing, true);
            CsjlogProxy.getInstance().debug("Navi:正在执行一键导览，单步导览无法进行");
            return;
        } else if (workType == GUIDE_BACK) {
            speak(R.string.working_ing, true);
            CsjlogProxy.getInstance().debug("Navi:正在执行一键导览，单步导览无法进行");
            return;
        }

        workType = GUIDE_SINGLE;

        CsjlogProxy.getInstance().info("Navi:立即前往");
        switch (guideSingle.workStatus) {
            case TaskStatusManager.AWAIT:
                guideSinglePauseFlag = false;
                guideSingle.start();
                startGuideSingle();
                break;

            case TaskStatusManager.PAUSE:
                guideSinglePauseFlag = false;
                guideSingle.resume();
                resumeGuideSingle();
                break;

            case TaskStatusManager.START:
                guideSinglePauseFlag = true;
                guideSingle.pause();
                pauseGuideSingle();
                break;

        }
    }


    private void startGuideSingle() {
        if (currentSelect == null) {
            speak(getString(R.string.no_select_point), true);
        } else {
            hideGuideAllBtn();
            showInfo(currentSelect);
            speak(getStartTip(currentSelect), new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {
                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    handler.sendEmptyMessageDelayed(GUIDE_SINGLE_TIMEOUT, 5000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startingGuideSingle();
                        }
                    });
                    RobotManager.getInstance().robot.reqProxy.navi(currentSelect.toJson());
                }
            }, true);
        }
    }

    /**
     * 播放视频
     */
    private void startPlayer(NaviBean naviBean) {
        if (naviBean != null && naviBean.isPlayVideo()) {
            String videoPath = Constants.NAVI_PATH + naviBean.getImagePath();
            File file = new File(videoPath);
            if (file.exists()) {
                Uri uri = Uri.parse(videoPath);
                naviVideo.startPlay(uri);
                new Handler().postDelayed(() -> {
                    iv_map.setVisibility(View.GONE);
                    layout_menu_navi.setVisibility(View.GONE);
                    naviVideo.setVisibility(View.VISIBLE);
                }, 1000);
            }
        }
    }

    //停止视频播放
    private void stopPlayer() {
        naviVideo.stopPlay();
        if (isNaviMap) {
            iv_map.setVisibility(View.VISIBLE);
            layout_menu_navi.setVisibility(View.GONE);
        } else {
            iv_map.setVisibility(View.GONE);
            layout_menu_navi.setVisibility(View.VISIBLE);
        }
        naviVideo.setVisibility(View.GONE);
    }

    /**
     * 管理地图
     */
    @OnClick(R.id.iv_map_manage)
    public void manageMap() {
        isClickMapManager = true;
        hideWaitTime();
        BRouter.getInstance()
                .build(BRouterPath.SETTING_CONFIRM)
                .withString(BRouterKey.ACTIVITY_NAME, BRouterPath.NAVI_SETTING)
                .navigation();
        cancelTask();
    }

    volatile int executeTaskCount = 0;
    boolean cancelFlag = false;
    boolean inRunning = false;

    private static final int CANCEL_TASK = 11111111;

    /**
     * 取消任务
     */
    public void cancelTask() {
        handler.sendEmptyMessageDelayed(CANCEL_TASK, 5000);
        RobotManager.getInstance().robot.reqProxy.cancelNavi();
        inRunning = false;
        cancelFlag = true;
        naviTimeout.cancel();
        handler.removeMessages(MSG_TIMEOUT);
        handler.removeMessages(MSG_TIMEOUTMSG_TIMEOUT);
        handler.removeMessages(GUIDE_ALL_TIMEOUT);
        handler.removeMessages(GUIDE_SINGLE_TIMEOUT);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stopPlay();
        player.releasePlayer();
        unregisterReceiver(receiver);
        unregisterReceiver(aReceiver);
        Robot.getInstance().setOnNaviSearchListener(null);
//        Robot.getInstance().setPositionListener(null);
        if (StateMachineManager.getInstance().getCurrentState() == NaviTaskStatus.AWAIT) {
            cancelTask();
        }
        handler.removeCallbacksAndMessages(null);
        armLooper.stopLooper();
        RobotManager.getInstance().removePositionListener(positionListener);

        sendBroadcast(new Intent("com.csjbot.blackgaga.naviondestroy"));
    }

    /**
     * 切换到一键导引按钮启动中状态
     */
    public void startingGuideAll() {
        tv_guide.setText(R.string.start_guide_all);
        tv_guide.setEnabled(false);
    }

    /**
     * 切换到一键导引运行中状态
     */
    public void inRunningGuideAll() {
        tv_guide.setEnabled(true);
        tv_guide.setText(R.string.stop_guide);
        iv_icon.setBackgroundResource(R.drawable.btn_stop_icon);
        guideAllTask.start();
    }

    //一键导引运行中状态
    public void inRunningPlayAll() {
        int currentIndex = executeTaskCount;
        NaviBean naviBean = workList.get(executeTaskCount);
        CsjlogProxy.getInstance().debug("Navi: isOpenMusic = {} , TaskCount = {}, currentIndex = {}, MusicPath = {}"
                , naviBean.isOpenMusic(), executeTaskCount, currentIndex, naviBean.getMusicPath());
        if (isNaviMap) {
            showPosition();
        }
        if (naviBean.isOpenMusic()) {//导览中播放背景音乐
            player.play(Constants.NAVI_PATH + naviBean.getMusicPath());
        }
        if (naviBean.isOpenSpeak()) {//导览中播放途中讲解文字
            speak(naviBean.getDescInRunningContent(), new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {
                    isFinishInRunningAll = false;
                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    isFinishInRunningAll = true;
                    if (executeTaskCount > currentIndex) {
                        guideAllSpeakMoveResult(naviBean);
                    }
                }
            }, true);
        }
        return;
    }

    /**
     * 切换到单步导览启动中状态
     */
    public void startingGuideSingle() {
        bt_start_guide.setText(R.string.starting_single);
        bt_start_guide.setEnabled(false);
        if (bt_start_guide_imm != null) {
            bt_start_guide_imm.setText(R.string.starting_single);
            bt_start_guide_imm.setEnabled(false);
        }
    }

    /**
     * 切换到单步导览运行中状态
     */
    public void inRunningGuideSingle() {
        bt_start_guide.setEnabled(true);
        guideSingle.start();
        bt_start_guide.setText(R.string.cancel);
        if (bt_start_guide_imm != null) {
            bt_start_guide_imm.setText(R.string.cancel);
            bt_start_guide_imm.setEnabled(true);
        }
    }

    //单点导览进行时 背景音播放还是途中讲解
    public void inRunningPlaySingle(NaviBean naviBean) {
        if (isNaviMap) {
            showPosition();
        }
        if (naviBean != null && naviBean.isOpenMusic()) {//导览中播放背景音乐
            player.play(Constants.NAVI_PATH + naviBean.getMusicPath());
        }
        if (naviBean != null && naviBean.isOpenSpeak()) {//导览中播放途中讲解文字
            speak(naviBean.getDescInRunningContent(), new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {
                    isFinishInRunningDesc = false;
                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    isFinishInRunningDesc = true;
                    if (isArriveSingle) {
                        if (!isFromAI) {
                            guideSingleBackSpeakDesc();
                        } else {
                            guideSingleSpeakDesc();
                        }
                    }
                }
            }, true);
        }
        return;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CANCEL_TASK:
                    CsjlogProxy.getInstance().debug("消息还是发到这了？");
                    if (workType == GUIDE_BACK) {
                        backWelcome.setEnabled(true);
                    }

                    if (workType == GUIDE_ALL) {
                        inRunningGuideAll();
                        setDefaultMap();
                        if (isNaviMap) {
                            showPosition();
                        }
                    }

                    if (workType == GUIDE_SINGLE) {
                        inRunningGuideSingle();
                        setDefaultMap();
                        if (isNaviMap) {
                            showPosition();
                        }
                    }

                    Toast.makeText(context, R.string.cancel_failed, Toast.LENGTH_SHORT).show();
                    break;

                case MSG_TIMEOUT:
                    CsjlogProxy.getInstance().debug("超时，查询导航状态");
                    listener.timeOut();
                    break;

                case MSG_TIMEOUTMSG_TIMEOUT:
                    CsjlogProxy.getInstance().debug("超时消息下发超时");
                    listener.timeOut();
                    break;

                case GUIDE_ALL_TIMEOUT:
                    CsjlogProxy.getInstance().debug("Navi:一键导览消息下发超时");
                    Toast.makeText(context, R.string.start_failed_check, Toast.LENGTH_SHORT).show();
                    initGuideAll();
                    player.stopPlay();
                    hideWaitTime();
                    isFinishInRunningAll = false;
                    // 重置标识
                    guideAllTask.workStatus = TaskStatusManager.AWAIT;
                    workType = GUIDE_DEFAULT;
                    setDefaultMap();
                    if (isNaviMap) {
                        showPosition();
                    }
                    showGuideAllBtn();
                    hideGuideSingleBtn();
//                    showGuideSingleBtn();
                    break;

                case GUIDE_SINGLE_TIMEOUT:
                    CsjlogProxy.getInstance().debug("Navi:单项导览消息下发超时");
                    Toast.makeText(context, R.string.start_failed_check, Toast.LENGTH_SHORT).show();
                    initGuideSingle();
                    player.stopPlay();
                    hideWaitTime();
                    isFinishInRunningDesc = false;
                    isArriveSingle = false;
                    // 重置标识
                    guideSingle.workStatus = TaskStatusManager.AWAIT;
                    workType = GUIDE_DEFAULT;
                    setDefaultMap();
                    if (isNaviMap) {
                        showPosition();
                    }
                    showGuideAllBtn();
                    hideGuideSingleBtn();
//                    showGuideSingleBtn();
                    break;

                default:

                    break;
            }
        }
    };

    TimeoutUtil.TimeoutListener listener = () -> {
        Robot.getInstance().reqProxy.search();
        handler.sendEmptyMessageDelayed(MSG_TIMEOUTMSG_TIMEOUT, 5000);
    };

    OnNaviSearchListener searchListener = json -> runOnUiThread(() -> {
        handler.removeMessages(MSG_TIMEOUT);
        handler.removeMessages(MSG_TIMEOUTMSG_TIMEOUT);
        try {
            JSONObject jo = new JSONObject(json);
            int state = jo.optInt("state");
            if (state == 0) {
                if (workType == GUIDE_ALL && guideAllTask.workStatus == TaskStatusManager.START) {
                    runGuideAllTaskSpeek();
                } else if (workType == GUIDE_SINGLE && guideSingle.workStatus == TaskStatusManager.START) {
                    backWelCome();
                }
                CsjlogProxy.getInstance().debug("Navi：state==0，尝试重新走点");
            } else if (state == 1) {
                handler.sendEmptyMessageDelayed(MSG_TIMEOUT, 20000);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

    private void disableButton() {
        iv_map_manage.setEnabled(false);
    }

    private void enableButton() {
        iv_map_manage.setEnabled(true);
    }

    private volatile boolean openListener = true;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeListener(ListenerState closeListener) {
        openListener = closeListener.isOpenListener;
    }

    private static TimeCount timeCount;

    private void showWaitTime(long time) {
        countDown.setVisibility(View.VISIBLE);
        timeCount = new TimeCount(time, 1000, new TimeCount.MyTime() {
            @Override
            public void onTick(long millisUntilFinished) {
                String str = getString(R.string.waiting) + millisUntilFinished / 1000 + "s";
                countDown.setText(str);
            }

            @Override
            public void onFinish() {
                countDown.setVisibility(View.GONE);
            }
        });
        timeCount.start();
    }

    private void hideWaitTime() {
        if (timeCount != null) {
            timeCount.cancel();
            timeCount = null;
        }
        countDown.setVisibility(View.GONE);
    }

    private void showGuideAllBtn() {
        rl_guide.setVisibility(View.VISIBLE);
    }

    private void hideGuideAllBtn() {
        rl_guide.setVisibility(View.GONE);
    }

    public void showGuideSingleBtn() {
        bt_start_guide.setVisibility(View.VISIBLE);
    }

    private void hideGuideSingleBtn() {
        bt_start_guide.setVisibility(View.GONE);
    }
}