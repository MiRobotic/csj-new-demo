package com.csjbot.mobileshop.home.contract;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.csjbot.coshandler.listener.OnCameraListener;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnMapListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.dialog.LoadMapFileDialog;
import com.csjbot.mobileshop.feature.Learning.service.FloatingWindowBackService;
import com.csjbot.mobileshop.feature.customer.FloatingWdCustomerService;
import com.csjbot.mobileshop.feature.navigation.NaviAction;
import com.csjbot.mobileshop.feature.navigation.NaviTextHandler;
import com.csjbot.mobileshop.feature.nearbyservice.PoiSearchActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.home.AllSceneHomeActivity;
import com.csjbot.mobileshop.home.adapter.HomeIconAdapter;
import com.csjbot.mobileshop.home.bean.HomeIconBean;
import com.csjbot.mobileshop.localbean.NaviBean;
import com.csjbot.mobileshop.model.http.apiservice.event.UpdateContentTypeEvent;
import com.csjbot.mobileshop.model.http.apiservice.event.UpdateHomeModuleEvent;
import com.csjbot.mobileshop.model.http.bean.rsp.ContentInfoBean;
import com.csjbot.mobileshop.model.http.bean.rsp.GreetContentBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ModuleBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.bean.rsp.UnreachablePointBean;
import com.csjbot.mobileshop.model.tcp.body_action.IAction;
import com.csjbot.mobileshop.model.tcp.chassis.IChassis;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.router.BRouter;
import com.csjbot.mobileshop.router.BRouterKey;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.service.HomeService;
import com.csjbot.mobileshop.util.AudioUtil;
import com.csjbot.mobileshop.util.CSJToast;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.mobileshop.util.SpeakUtil;
import com.csjbot.mobileshop.widget.chat_view.bean.ChatBean;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridLayoutManager;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridSnapHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import skin.support.widget.SkinCompatImageView;

/**
 * @author ShenBen
 * @date 2019/1/18 9:26
 * @email 714081644@qq.com
 */
public class AllSceneHomePresenterImpl implements AllSceneHomeContract.Presenter {

    private static final String JIAOYU_SCENE = "jiaoyu";
    private static final String JICHANG_SCENE = "jichang";


    private Context mContext;
    private AllSceneHomeContract.View mView;
    private AllSceneHomeModel mModel;//网络请求相关
    private RecyclerView mRv;
    private LinearLayout mDots;
    private String mSceneKey;//场景标识
    private boolean isPlus;//是否是大屏
    private int rows = 2;//主页模块行数
    private int columns = 3;//主页模块列数
    private int pageSize = 0;//页数
    private List<HomeIconBean> mList;//总的主页模块
    private List<HomeIconBean> mModuleList;//主页模块list
    private List<HomeIconBean> mContentList;//内容管理list
    private HomeIconAdapter mAdapter;

    private boolean isPause = false;//当前Activity是否可见
    private boolean isLoadMapFinish = false;//是否加载地图完成
    private boolean isLoadMapSuccess = false;//是否加载地图成功
    private String clickNaviType = Constants.NaviType.none;
    private volatile boolean isPersonNear = false;//检测是否有人
    private volatile boolean isComplete = false;//检测人脸完成
    private IChassis mChassis;//机器人肢体控制操作
    private Bitmap mBitmap;//人脸图像bitmap
    private AudioUtil mAuidoUtil;//音乐播放工具
    private OnMapListener mMapListener = new OnMapListener() {//地图相关监听
        @Override
        public void saveMap(boolean state) {

        }

        @Override
        public void loadMap(boolean state) {
            isLoadMapSuccess = state;
            mHandler.sendEmptyMessage(state ? 1 : 2);
        }
    };

    private NaviTextHandler.PointMatchListener mPointMatchListener = new NaviTextHandler.PointMatchListener() {//导航点相关监听
        @Override
        public void noMatch(String msg) {

            boolean flag = false;
            for (UnreachablePointBean unreachablePoint : Constants.sUnreachablePoints) {//是否命中不可到达点

                if (msg.contains(NaviTextHandler.strConvertPinyin(unreachablePoint.getLocation()))) {
                    List<ChatBean.PictureBean> list = new ArrayList<>();
                    ChatBean.PictureBean pictureBean = new ChatBean.PictureBean();
                    pictureBean.setPictureUrl(unreachablePoint.getFileUrl());
                    pictureBean.setPictureName(unreachablePoint.getFileName());
                    list.add(pictureBean);
                    mView.setChatPicture(unreachablePoint.getSpeech(), list);
                    mView.speakText(unreachablePoint.getSpeech());

                    flag = true;
                }
            }
            if (flag) {
                return;
            }
            mView.prattleText(SpeakUtil.getNaviNoMatchSpeak());
        }

        @Override
        public void match(NaviBean naviBean, boolean flag) {
            CsjlogProxy.getInstance().info("NAVI:match");
            goNaviActAI(naviBean, true);
        }

        @Override
        public void goNavi() {
            goNaviAct();
        }
    };
    private NaviAction.NaviActionListener mNaviListener = new NaviAction.NaviActionListener() {//导览相关
        @Override
        public void arrived(NaviBean current) {
            if (isPause)
                return;
            if (NaviAction.getInstance().getWorkType() == NaviAction.GUIDE_BACK) {
                mView.speakText(mContext.getString(R.string.back_already));
            }

            if (current != null) {
                mView.speakText(current.getName() + mContext.getString(R.string.destination));
            }
        }

        @Override
        public void error(int code) {
        }

        @Override
        public void sendSuccess() {

        }
    };
    private OnCameraListener mCameraListener = reponse -> {//拍照接口相关
        if (isComplete) {
            mBitmap = reponse;
        }
    };

    private OnFaceListener mFaceListener = new OnFaceListener() {
        @Override
        public void personInfo(String json) {
            if (isPersonNear && !isPause && !isComplete) {
                try {
                    JSONObject jsonObject = (JSONObject) new JSONObject(json).getJSONArray("face_list").get(0);
                    JSONObject faceRecg = jsonObject.getJSONObject("face_recg");
                    int confidence = faceRecg.getInt("confidence");
                    String name = faceRecg.getString("name");
                    if (confidence > 80) {
                        if (mView.isSpeaking()) {
                            return;
                        }
                        isComplete = true;
                        welcome(faceRecg.getString("person_id"), name);
                        welcomeAction();
                    }
                } catch (JSONException e) {
                }
            }
        }

        @Override
        public void personNear(boolean person) {
            Constants.Face.person = person;
            isPersonNear = person;
            if (person && !isPause) {
                isComplete = false;
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isComplete) {
                    if (mView.isSpeaking()) {
                        return;
                    }
                    isComplete = true;
                    welcome(null, null);
                    welcomeAction();
                }
            }
        }

        @Override
        public void personList(String json) {

        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://加载地图成功
                    SharedPreUtil.putBoolean(SharedKey.ISLOADMAP, SharedKey.ISLOADMAP, true);
                    if (clickNaviType.equals(Constants.NaviType.Guide)) {
                        ARouter.getInstance().build(BRouterPath.NAVI_MAIN).navigation();
                    } else if (clickNaviType.equals(Constants.NaviType.Patrol)) {
                        ARouter.getInstance().build(BRouterPath.PATROL_PWD).navigation();
                    }
                    CSJToast.showToast(mContext, mContext.getString(R.string.restore_map_success));
                    break;
                case 2://加载地图失败
                    CsjlogProxy.getInstance().info("恢复地图失败");
                    restoreMapException();
//                    CSJToast.showToast(mContext, mContext.getString(R.string.restore_map_fail));
                    break;
                case 3://请检查底层连接状态
                    CSJToast.showToast(mContext, mContext.getString(R.string.check_linux));
                    break;
            }
        }
    };

    public void restoreMapException() {
        new Handler(Looper.getMainLooper()).post(() -> {
            LoadMapFileDialog dialog = new LoadMapFileDialog(mContext);
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        });
    }

    private long mLastTime;

    public AllSceneHomePresenterImpl(Context mContext, AllSceneHomeContract.View mView) {
        this.mContext = mContext;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void init() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mModel = new AllSceneHomeModelImpl();
        RobotManager.getInstance().addListener(mMapListener);//添加恢复地图监听
        isPlus = Constants.isPlus();
        mRv = mView.getRecycler();
        mDots = mView.getDots();
        mList = new ArrayList<>();
        mModuleList = new ArrayList<>();
        mContentList = new ArrayList<>();
        mChassis = ServerFactory.getChassisInstance();
        mAuidoUtil = new AudioUtil();
        addFaceListener();
        NaviAction.getInstance().registerActionListener(mNaviListener);
        if (!"qiantai".equals(mSceneKey)) {
            NaviTextHandler.listener = mPointMatchListener;
        }
    }

    /**
     * 初始化每个场景特有的功能
     *
     * @param sceneKey
     * @return
     */
    private void initHomeModuleFunction(String sceneKey) {
        switch (sceneKey) {
            case "shebao"://社保场景
            case "gongshang"://工商场景
            case "shuiwu"://税务场景
            case "xingzheng"://行政场景
            case "gongan"://公安场景
            case "fayuan"://法院场景
            case "jianchayuan"://检察院场景

                break;
            case "fuzhuang"://服装场景
                break;
            case "dianli"://电力场景
                break;
            case "yinhang"://银行场景
                break;
            case "jiaoyu":
                break;
            case "jichang":

                break;
            case "CSJ_BOT"://默认场景
            default://其他场景
                break;
        }
    }

    /**
     * 设置场景
     * {@link #getHomeModuleItemLayoutId} 设置对应item布局id
     *
     * @param sceneKey 场景标识
     */
    @Override
    public void setScene(String sceneKey) {
        if (TextUtils.isEmpty(sceneKey)) {
            sceneKey = "CSJ_BOT";
        }
        mSceneKey = sceneKey;
        if (TextUtils.equals(mSceneKey, JIAOYU_SCENE)) {
            mContext.startService(new Intent(mContext, FloatingWindowBackService.class));
        }

        mContext.startService(new Intent(mContext, FloatingWdCustomerService.class));

        mAdapter = new HomeIconAdapter(getHomeModuleItemLayoutId(mSceneKey), mList);
        PagerGridLayoutManager manager = new PagerGridLayoutManager(rows, columns, PagerGridLayoutManager.HORIZONTAL);
        mRv.setLayoutManager(manager);
        manager.setPageListener(new PagerGridLayoutManager.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {

            }

            @Override
            public void onPageSelect(int pageIndex) {
                SkinCompatImageView imageView;
                for (int i = 0; i < mDots.getChildCount(); i++) {
                    imageView = (SkinCompatImageView) mDots.getChildAt(i);
                    if (pageIndex == i) {
                        imageView.setImageResource(R.drawable.iv_point_selected);
                    } else {
                        imageView.setImageResource(R.drawable.iv_point_unselected);
                    }
                }
            }
        });
        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(mRv);
        mRv.setHasFixedSize(true);
        mRv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastTime < 1000) {//1秒钟只能点击一次
                return;
            }
            mLastTime = currentTime;
            switch (mList.get(position).getModuleId()) {
                case Constants.LEAD_WAY_ID://引领带路
                    goNaviAct();
                    break;
                case Constants.INVITED_VISITOR://受邀访客
                    ARouter.getInstance().build(BRouterPath.INVITE_VISIT).navigation();
                    break;
                case Constants.TEMP_VISITOR://临时访客
                    ARouter.getInstance().build(BRouterPath.TEMP_VISIT).navigation();
                    break;
                case Constants.LOCAL_SERVICE_ID://周边服务
                    if (mSceneKey.equals(Constants.Scene.JiChangScene)) {
                        ARouter.getInstance().build(BRouterPath.JICHANG_NEAR_BY_MAIN).navigation();
                    } else {
                        ARouter.getInstance().build(BRouterPath.NEAR_BY_MAIN).navigation();
                    }
                    break;
                case Constants.VIP_CENTER_ID://会员注册
                    ARouter.getInstance().build(BRouterPath.VIP_CENTER).navigation();
                    break;
                case Constants.ENTERTAINMENT_ID://互动娱乐
                    if (TextUtils.equals(JIAOYU_SCENE, mSceneKey)) {//教育场景跳转到指定娱乐界面
                        ARouter.getInstance().build(BRouterPath.JIAO_YU_ENTERTAINMENT).navigation();
                    } else {
                        ARouter.getInstance().build(BRouterPath.ENTERTAINMENT).navigation();
                    }
                    break;
                case Constants.PRODUCT_LIST_ID://商品列表
                    ARouter.getInstance().build(BRouterPath.PRODUCT_CLOTHING_TYPE).navigation();
                    break;
                case Constants.EXHIBITION_EXPLANATION_ID://展览讲解

                    break;
                case Constants.PAYMENT_GUIDE_ID://查缴指南
                    ARouter.getInstance().build(BRouterPath.PAYMENT_GUIDE).navigation();
                    break;
                case Constants.MEDIA_PUBLICIZE_ID://媒体宣传
                    ARouter.getInstance().build(BRouterPath.MEDIA_PUBLICIZE).navigation();
                    break;
                case Constants.HALL_TOUR_ID://大厅巡视
                    goPatrolAct();
                    break;
                case Constants.LEARN_ENCYCLOPEDIA_ID://学习百科
                    ARouter.getInstance().build(BRouterPath.LEARN_EDUCATION).navigation();
                    break;
                case Constants.MST_PLAYER_ID://课件播放
                    ARouter.getInstance().build(BRouterPath.MST_PLAYER).navigation();
                    break;
                default://内容管理
                    BRouter.jumpActivityByContent(TextUtils.isEmpty(mList.get(position).getReplaceTitle()) ?
                            mList.get(position).getTitle() : mList.get(position).getReplaceTitle());
                    break;
            }
        });
    }

    @Override
    public void initData() {
        getHomeModule();
        getAllContent();
        setAdapterData();
        initHomeModuleFunction(mSceneKey);
    }

    @Override
    public void resume() {
        if (!"qiantai".equals(mSceneKey)) {
            new Handler().postDelayed(() -> NaviTextHandler.listener = mPointMatchListener, 2000);
        }
        isPause = false;
        isLoadMapSuccess = SharedPreUtil.getBoolean(SharedKey.ISLOADMAP, SharedKey.ISLOADMAP, false);
        mContext.sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW));
        Constants.sIsIntoOtherApp = false;

        if (mSceneKey.equals("jichang")) {
//            mContext.startService(new Intent(mContext,WeatherService.class));
//            mContext.startService(new Intent(mContext,GlobalNoticeService.class));
        }
    }

    @Override
    public boolean speakToIntent(String text) {
        if (CsjLanguage.CURRENT_LANGUAGE == CsjLanguage.CHINESE) {//中文环境下
            if (!"qiantai".equals(mSceneKey) && NaviTextHandler.handle(text)) {
                return true;
            }
            for (String str : Constants.NearbyKeyWord.intents) {//是否命中周边服务
                if (text.contains(str)) {
                    for (String keyWord : Constants.NearbyKeyWord.keyWords) {
                        if (text.contains(keyWord)) {
                            ARouter.getInstance()
                                    .build(BRouterPath.NEAR_BY_SEARCH)
                                    .withString(PoiSearchActivity.KEYWORD, keyWord)
                                    .navigation();
                            return true;
                        }
                    }
                    if (mSceneKey.equals(Constants.Scene.JiChangScene)) {
                        ARouter.getInstance().build(BRouterPath.JICHANG_NEAR_BY_MAIN).navigation();
                    } else {
                        ARouter.getInstance().build(BRouterPath.NEAR_BY_MAIN).navigation();
                    }
                    return true;
                }
            }
        }
        if (!mList.isEmpty()) {
            String title;
            for (HomeIconBean bean : mList) {//是否命中主页模块
                title = TextUtils.isEmpty(bean.getReplaceTitle()) ? bean.getTitle() : bean.getReplaceTitle();
                if (text.contains(title)) {
                    switch (bean.getModuleId()) {
                        case Constants.LEAD_WAY_ID://引领带路
                            goNaviAct();
                            break;
                        case Constants.INVITED_VISITOR://受邀访客
                            ARouter.getInstance().build(BRouterPath.INVITE_VISIT).navigation();
                            break;
                        case Constants.TEMP_VISITOR://临时访客
                            ARouter.getInstance().build(BRouterPath.TEMP_VISIT).navigation();
                            break;
                        case Constants.LOCAL_SERVICE_ID://周边服务
                            if (mSceneKey.equals(Constants.Scene.JiChangScene)) {
                                ARouter.getInstance().build(BRouterPath.JICHANG_NEAR_BY_MAIN).navigation();
                            } else {
                                ARouter.getInstance().build(BRouterPath.NEAR_BY_MAIN).navigation();
                            }
                            break;
                        case Constants.VIP_CENTER_ID://会员注册
                            ARouter.getInstance().build(BRouterPath.VIP_CENTER).navigation();
                            break;
                        case Constants.ENTERTAINMENT_ID://互动娱乐
                            if (TextUtils.equals(JIAOYU_SCENE, mSceneKey)) {//教育场景跳转到指定娱乐界面
                                ARouter.getInstance().build(BRouterPath.JIAO_YU_ENTERTAINMENT).navigation();
                            } else {
                                ARouter.getInstance().build(BRouterPath.ENTERTAINMENT).navigation();
                            }
                            break;
                        case Constants.PRODUCT_LIST_ID://商品列表
                            ARouter.getInstance().build(BRouterPath.PRODUCT_CLOTHING_TYPE).navigation();
                            break;
                        case Constants.EXHIBITION_EXPLANATION_ID://展览讲解

                            break;
                        case Constants.PAYMENT_GUIDE_ID://查缴指南
                            ARouter.getInstance().build(BRouterPath.PAYMENT_GUIDE).navigation();
                            break;
                        case Constants.MEDIA_PUBLICIZE_ID://媒体宣传
                            ARouter.getInstance().build(BRouterPath.MEDIA_PUBLICIZE).navigation();
                            break;
                        case Constants.HALL_TOUR_ID://大厅巡视
                            goPatrolAct();
                            break;
                        case Constants.LEARN_ENCYCLOPEDIA_ID://学习百科
                            ARouter.getInstance().build(BRouterPath.LEARN_EDUCATION).navigation();
                            break;
                        case Constants.MST_PLAYER_ID://课件播放
                            ARouter.getInstance().build(BRouterPath.MST_PLAYER).navigation();
                            break;
                        default://内容管理
                            BRouter.jumpActivityByContent(TextUtils.isEmpty(bean.getReplaceTitle()) ?
                                    bean.getTitle() : bean.getReplaceTitle());
                            break;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void loadMap() {
        mChassis.loadMap();
        isLoadMapSuccess = false;
        mHandler.postDelayed(() -> {
            if (!isLoadMapSuccess) {
                mHandler.sendEmptyMessage(3);
            }
        }, Constants.internalCheckLinux);
    }

    @Override
    public void pause() {
        isPause = true;

        if (mSceneKey.equals("jichang")) {
//            mContext.stopService(new Intent(mContext,WeatherService.class));
//            mContext.stopService(new Intent(mContext,GlobalNoticeService.class));
        }
    }

    @Override
    public void destroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        RobotManager.getInstance().addListener((OnMapListener) null);
        RobotManager.getInstance().removeCameraListener(mCameraListener);
        RobotManager.getInstance().removeListener(mFaceListener);
        NaviAction.getInstance().unregisterActionListener(mNaviListener);
        NaviTextHandler.listener = null;
        if (mBitmap != null) {
            if (!mBitmap.isRecycled()) {
                mBitmap.recycle();
                mBitmap = null;
            }
        }
        if (TextUtils.equals(mSceneKey, JIAOYU_SCENE)) {
            mContext.stopService(new Intent(mContext, FloatingWindowBackService.class));
        }
//        mContext.stopService(new Intent(mContext, FloatingWdCustomerService.class));
        Constants.clear2();
    }


    /**
     * 添加人脸监听
     */
    private void addFaceListener() {
        RobotManager.getInstance().addListener(mCameraListener);
        RobotManager.getInstance().addListener(mFaceListener);
    }

    /**
     * 根据场景和大小屏获取适合的主页模块item 布局
     * 同时还需要设置x行x列
     *
     * @param sceneKey 场景标识
     * @return 布局
     */
    private int getHomeModuleItemLayoutId(String sceneKey) {
        switch (sceneKey) {
            case "shebao"://社保场景
            case "gongshang"://工商场景
            case "shuiwu"://税务场景
            case "xingzheng"://行政场景
            case "gongan"://公安场景
            case "fayuan"://法院场景
            case "jianchayuan"://检察院场景
                rows = 2;
                columns = 2;
                return isPlus ? R.layout.item_shebao_home_icon_vertical : R.layout.item_shebao_home_icon;
            case "fuzhuang"://服装场景
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_fuzhuang_home_icon_vertical : R.layout.item_fuzhuang_home_icon;
            case "dianli"://电力场景
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_dian_li_home_icon_vertical : R.layout.item_dian_li_home_icon;
            case "yinhang"://银行场景
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_yin_hang_home_icon_vertical : R.layout.item_yin_hang_home_icon;
            case "jiaoyu"://教育场景
                rows = 2;
                columns = 2;
                return isPlus ? R.layout.item_jiao_yu_home_icon_vertical : R.layout.item_jiao_yu_home_icon;
            case "shangsha"://商厦场景
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_shang_sha_icon_vertical : R.layout.item_shang_sha_icon;
            case "cheguansuo"://车管所场景
                rows = 1;
                columns = 3;
                return isPlus ? R.layout.item_che_guan_suo_home_icon_vertical : R.layout.item_che_guan_suo_home_icon;
            case "jiadian"://家电场景
            case "xiedian"://鞋店场景
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_jia_dian_icon_vertical : R.layout.item_jia_dian_icon;
            case "bowuguan"://博物馆场景
                rows = 1;
                columns = 3;
                return isPlus ? R.layout.item_bo_wu_guan_home_icon_vertical : R.layout.item_bo_wu_guan_home_icon;
            case "jiudian":  //酒店
                if (isPlus) {
                    rows = 1;
                    columns = 4;
                } else {
                    rows = 1;
                    columns = 3;
                }
                return isPlus ? R.layout.item_jiu_dian_home_icon_vertical : R.layout.item_jiu_dian_home_icon;
            case "shouji": // 手机连锁
                rows = 1;
                columns = 3;
                return isPlus ? R.layout.item_shou_ji_icon_vertical : R.layout.item_shou_ji_icon;
            case "yaodian"://药店
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_yao_dian_icon_vertical : R.layout.item_yao_dian_icon;
            case "shipin"://食品
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_shipin_vertical : R.layout.item_shipin_icon;
            case "yanjingdian"://眼镜店
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_yan_jing_dian_icon_vertical : R.layout.item_yan_jing_dian_icon;
            case "canting"://餐厅
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_can_ting_icon_vertical : R.layout.item_can_ting_icon;
            case "yiyuan"://医院
                rows = 1;
                columns = 3;
                return isPlus ? R.layout.item_yi_yuan_icon_vertical : R.layout.item_yi_yuan_icon;
            case "jichang":  //机场
                rows = 1;
                columns = 3;
                return isPlus ? R.layout.item_ji_chang_home_icon_vertical : R.layout.item_ji_chang_home_icon;
            case "chezhan": //车站
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_che_zhan_home_icon_vertical : R.layout.item_che_zhan_home_icon;
            case "zhanguan"://展馆
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_zhan_guan_home_icon_vertical : R.layout.item_zhan_guan_home_icon;
            case "qiantai"://前台
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_qian_tai_home_icon_vertical : R.layout.item_qian_tai_home_icon;
            case "chaoshi":
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_chaoshi_vertical : R.layout.item_chaoshi_icon;
            case "CSJ_BOT"://默认场景
            case "lvyou":
            default://其他场景
                rows = 2;
                columns = 3;
                return isPlus ? R.layout.item_csjbot_home_icon_vertical : R.layout.item_csjbot_home_icon;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void updateHomeModule(UpdateHomeModuleEvent event) {
        if (event != null) {
            if (event.isNeedUpdate()) {
                getHomeModule();
                setAdapterData();
            }
        }
    }

    /**
     * 获取主页模块
     */
    private void getHomeModule() {
        if (!mModuleList.isEmpty()) {
            mList.removeAll(mModuleList);
            mModuleList.clear();
        }
        String json = SharedPreUtil.getString(SharedKey.HOME_MODULE, SharedKey.HOME_MODULE);
        if (TextUtils.isEmpty(json)) {//json为空，则用默认
            mModuleList.add(new HomeIconBean(getHomeDefualtTitle(Constants.LEAD_WAY_ID), getHomeIconId(Constants.LEAD_WAY_ID), Constants.LEAD_WAY_ID));
            mModuleList.add(new HomeIconBean(getHomeDefualtTitle(Constants.VIP_CENTER_ID), getHomeIconId(Constants.VIP_CENTER_ID), Constants.VIP_CENTER_ID));
            mModuleList.add(new HomeIconBean(getHomeDefualtTitle(Constants.ENTERTAINMENT_ID), getHomeIconId(Constants.ENTERTAINMENT_ID), Constants.ENTERTAINMENT_ID));
            mModuleList.add(new HomeIconBean(getHomeDefualtTitle(Constants.LOCAL_SERVICE_ID), getHomeIconId(Constants.LOCAL_SERVICE_ID), Constants.LOCAL_SERVICE_ID));
            mModuleList.add(new HomeIconBean(getHomeDefualtTitle(Constants.PRODUCT_LIST_ID), getHomeIconId(Constants.PRODUCT_LIST_ID), Constants.PRODUCT_LIST_ID));
        } else {//否则用远程
            ResponseBean<List<ModuleBean>> bean = GsonUtils.jsonToObject(json,
                    new TypeToken<ResponseBean<List<ModuleBean>>>() {
                    }.getType());
            if (bean != null) {
                int index = -1;
                List<ModuleBean> list = new ArrayList<>(bean.getRows());
                HomeIconBean homeIconBean;
                for (ModuleBean moduleBean : list) {
                    if (moduleBean.isEnable()) {
                        index += 2;
                        homeIconBean = new HomeIconBean();
                        homeIconBean.setTitle(moduleBean.getModuleName());
                        homeIconBean.setReplaceTitle(moduleBean.getReplaceName());
                        homeIconBean.setModuleId(moduleBean.getModuleId());
                        int moduleID = moduleBean.getModuleId();
                        if (moduleID == Constants.PRODUCT_LIST_ID) {
                            Constants.WELCOME_PRODUCT_MODULENAME = moduleBean.getModuleName();
                        }
                        homeIconBean.setIcon(getHomeIconId(moduleID));
                        homeIconBean.setSort(index);
                        mModuleList.add(homeIconBean);
                    }
                }
            }
        }
        if (TextUtils.equals(JIAOYU_SCENE, mSceneKey)) {//如果是教育场景，自动添加学习百科、课程介绍
            mModuleList.add(new HomeIconBean(getHomeDefualtTitle(Constants.LEARN_ENCYCLOPEDIA_ID),
                    Constants.LEARN_ENCYCLOPEDIA_ID, getHomeIconId(Constants.LEARN_ENCYCLOPEDIA_ID), Constants.LEARN_ENCYCLOPEDIA_ID));
            if (TextUtils.equals(BuildConfig.robotType, "snow")) {//小雪场景添加课件播放
                mModuleList.add(new HomeIconBean(getHomeDefualtTitle(Constants.MST_PLAYER_ID),
                        Constants.MST_PLAYER_ID, getHomeIconId(Constants.MST_PLAYER_ID), Constants.MST_PLAYER_ID));
            }
        }
        mList.addAll(mModuleList);
    }

    /**
     * 功能模块默认的名字
     *
     * @param moduleId
     * @return
     */
    private String getHomeDefualtTitle(int moduleId) {
        switch (moduleId) {
            case Constants.LEAD_WAY_ID://引领带路
                return mContext.getString(R.string.lead_way);
            case Constants.LOCAL_SERVICE_ID://周边服务
                return mContext.getString(R.string.local_service);
            case Constants.VIP_CENTER_ID://会员注册
                return mContext.getString(R.string.member_registration);
            case Constants.ENTERTAINMENT_ID://互动娱乐
                return mContext.getString(R.string.interactive_entertainment);
            case Constants.PRODUCT_LIST_ID://商品列表
                return mContext.getString(R.string.product_list);
            case Constants.EXHIBITION_EXPLANATION_ID://展览讲解
                return mContext.getString(R.string.nav_explain);
            case Constants.PAYMENT_GUIDE_ID://查缴指南
                return "查缴指南";
            case Constants.MEDIA_PUBLICIZE_ID://媒体宣传
                return "媒体宣传";
            case Constants.HALL_TOUR_ID://大厅巡视
                return "大厅巡视";
            case Constants.LEARN_ENCYCLOPEDIA_ID://学习百科
                return "学习百科";
            case Constants.MST_PLAYER_ID://课件播放
                return "课件播放";
            case Constants.INVITED_VISITOR://受邀访客
                return "受邀访客";
            case Constants.ALREADY_CONNECTED://临时访客
                return "临时访客";
            default:
                return "";
        }
    }

    /**
     * 功能模块ICON
     *
     * @param moduleId
     * @return
     */
    private int getHomeIconId(int moduleId) {
        switch (moduleId) {
            case Constants.LEAD_WAY_ID://引领带路
                return R.drawable.iv_navigation;
            case Constants.LOCAL_SERVICE_ID://周边服务
                return R.drawable.iv_peripheral_services;
            case Constants.VIP_CENTER_ID://会员注册
                return R.drawable.iv_member_center;
            case Constants.ENTERTAINMENT_ID://互动娱乐
                return R.drawable.iv_entertainment;
            case Constants.PRODUCT_LIST_ID://商品列表
                return R.drawable.iv_product;
            case Constants.EXHIBITION_EXPLANATION_ID://展览讲解
                return R.drawable.iv_exhibition_explanation;
            case Constants.PAYMENT_GUIDE_ID://查缴指南
                return R.drawable.iv_payment_guide;
            case Constants.MEDIA_PUBLICIZE_ID://媒体宣传
                return R.drawable.iv_media_publicty;
            case Constants.HALL_TOUR_ID://大厅巡视
                return R.drawable.iv_hall_tour;
            case Constants.LEARN_ENCYCLOPEDIA_ID://学习百科
                return R.drawable.iv_learn_encyclopedia;
            case Constants.MST_PLAYER_ID:
                return R.drawable.iv_mst_player;
            case Constants.TEMP_VISITOR://临时访客
                return R.drawable.temporary_visitor;
            case Constants.INVITED_VISITOR://受邀访客
                return R.drawable.invited_visitors;
            default:
                return 0;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 99)
    public void getContentType(UpdateContentTypeEvent event) {
        if (event != null) {
            if (event.isNeedUpdate()) {
                getAllContent();
                setAdapterData();
            }
        }
    }

    /**
     * 获取内容模块
     */
    private void getAllContent() {
        if (!mContentList.isEmpty()) {
            mList.removeAll(mContentList);
            mContentList.clear();
        }
        List<ContentInfoBean> list = new ArrayList<>(Constants.sContentInfos);
        if (list.isEmpty()) {
            String json = SharedPreUtil.getString(SharedKey.CONTENT_NAME, SharedKey.CONTENT_NAME);
            if (!TextUtils.isEmpty(json)) {
                ResponseBean<List<ContentInfoBean>> listResponseBean = GsonUtils.jsonToObject(json,
                        new TypeToken<ResponseBean<List<ContentInfoBean>>>() {
                        }.getType());
                if (listResponseBean != null) {
                    list = new ArrayList<>(listResponseBean.getRows());
                }
            }
        }
        int index = 0;
        HomeIconBean homeIconBean;
        for (ContentInfoBean bean : list) {
            if (bean.isHomeShow()) {
                index += 2;
                homeIconBean = new HomeIconBean();
                homeIconBean.setIcon(getContentIcon(index));
                homeIconBean.setModuleId(Integer.MAX_VALUE);
                homeIconBean.setTitle(bean.getName());
                homeIconBean.setReplaceTitle(bean.getName());
                homeIconBean.setSort(index == 6 ? Integer.MAX_VALUE : index);
                mContentList.add(homeIconBean);
            }
        }
        mList.addAll(mContentList);
    }

    /**
     * 内容管理的图标
     *
     * @param index
     * @return
     */
    private int getContentIcon(int index) {
        switch (index) {
            case 2:
                return R.drawable.iv_content_1;
            case 4:
                return R.drawable.iv_content_2;
            case 6:
                return R.drawable.iv_content_3;
            default:
                return R.drawable.iv_content_1;
        }
    }

    /**
     * 更新Adapter
     */
    private void setAdapterData() {
        Collections.sort(mList);
        mAdapter.setNewData(mList);
        updateDots();
    }

    private void updateDots() {
        double a = (double) mList.size() / (double) (rows * columns);
        pageSize = (int) Math.ceil(a);
        mDots.removeAllViews();
        if (pageSize > 1) {
            SkinCompatImageView dot;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;
            for (int i = 0; i < pageSize; i++) {
                //小圆点
                dot = new SkinCompatImageView(mContext);
                dot.setScaleType(ImageView.ScaleType.CENTER_CROP);
                dot.setLayoutParams(params);
                if (i == 0) {
                    dot.setImageResource(R.drawable.iv_point_selected);
                } else {
                    dot.setImageResource(R.drawable.iv_point_unselected);
                }
                mDots.addView(dot);
            }
        }
    }

    /**
     * 迎宾
     *
     * @param faceId   人脸唯一标识
     * @param userName 会员名
     */
    private void welcome(String faceId, String userName) {
        if (!TextUtils.isEmpty(faceId)) {//会员欢迎语
            mModel.getVipGreet(faceId, vipGreetBean -> {
                String[] tempStrs;//显示、语音话术
                if (vipGreetBean != null) {
                    String comma = Constants.isI18N() ? "" : "，";
                    if (!TextUtils.isEmpty(vipGreetBean.getAudioUrl())) {//有音频，不说话
                        mAuidoUtil.play(vipGreetBean.getAudioUrl(), null);

                        if (!TextUtils.isEmpty(vipGreetBean.getGreetWords())) {
//                            tempStrs = Constants.createWelcomeText(mSceneKey, vipGreetBean.getGreetWords() + comma);
                            // 如果vip 问候语不为空，则只播放VIP问候语，不要加入废话
                            mView.setRobotChatText(vipGreetBean.getGreetWords());
                        } else {
                            String tempStr = mContext.getString(R.string.hello) + "，" + userName + "！";
//                            tempStrs = Constants.createWelcomeText(mSceneKey, tempStr);
//                            mView.setRobotChatText(tempStrs[0]);
                            mView.setRobotChatText(tempStr);
                        }
                    } else if (!TextUtils.isEmpty(vipGreetBean.getGreetWords())) {//欢迎语不为空，则播报欢迎语
//                        tempStrs = Constants.createWelcomeText(mSceneKey, vipGreetBean.getGreetWords() + comma);
                        mView.setRobotChatText(vipGreetBean.getGreetWords());
                        mView.speakText(vipGreetBean.getGreetWords());
                    } else {//音频、欢迎语全为空，则用全局默认的
                        setGlobalWelcome(true, userName);
                    }
                } else {
                    setGlobalWelcome(true, userName);
                }
            }, throwable -> {
                CsjlogProxy.getInstance().error("获取会员热词失败: error: " + throwable.getMessage());
                setGlobalWelcome(true, userName);
            });
        } else {//非会员欢迎语
            setGlobalWelcome(false, null);
        }
        if (mBitmap != null) {
            mModel.uploadBitmap(mBitmap, faceId, userName);//上传访客图片
        }
    }

    /**
     * 迎宾动作
     */
    private void welcomeAction() {
        new Thread(() -> {
            IAction action = ServerFactory.getActionInstance();
            switch (BuildConfig.robotType) {
                case BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS:
                case BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS_i18n:
                case BuildConfig.ROBOT_TYPE_DEF_ALICE:
                case BuildConfig.ROBOT_TYPE_DEF_ALICE_i18n:
                    action.nodAction();
                    action.righLargeArmUp();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    action.rightLargeArmDown();
                    break;
                case BuildConfig.ROBOT_TYPE_DEF_SNOW:
                case BuildConfig.ROBOT_TYPE_DEF_SNOW_i18n:
                    int time = 5;
                    while (time != 0) {
                        try {
                            Thread.sleep(1000);
                            action.snowDoubleArm();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        time--;
                    }
                    break;
            }
        }).start();
    }


    /**
     * [{
     * "startTime": "01:00",
     * "endTime": "01:30",
     * "words": "11"
     * }, {
     * "startTime": "01:30",
     * "endTime": "02:00",
     * "words": "22"
     * }] 转换为 list，并且取出要说的word
     *
     * @param timePeriodInfo
     * @return
     */
    private String getWelcomeWordsFromTime(String timePeriodInfo) {
        String welcomeWord = null;
        List<GreetContentBean.TimePeriodInfo> timePeriodInfoBeanList =
                new Gson().fromJson(timePeriodInfo,
                        new TypeToken<List<GreetContentBean.TimePeriodInfo>>() {
                        }.getType());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        for (GreetContentBean.TimePeriodInfo timeInfo : timePeriodInfoBeanList) {
            Date nowTime = null;
            try {
                nowTime = sdf.parse(sdf.format(new Date()));
                Date beginTime = sdf.parse(timeInfo.getStartTime());
                Date endTime = sdf.parse(timeInfo.getEndTime());
                Calendar nowCalendar = Calendar.getInstance();
                nowCalendar.setTime(nowTime);
                Calendar beginCalendar = Calendar.getInstance();
                beginCalendar.setTime(beginTime);
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(endTime);

                if (nowCalendar.after(beginCalendar) && nowCalendar.before(endCalendar)) {//时间段匹配
                    welcomeWord = timeInfo.getWords();
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return welcomeWord;
    }

    /**
     * 设置全局欢迎语
     *
     * @param isVip    是否是会员
     * @param userName 会员名
     */
    private void setGlobalWelcome(boolean isVip, String userName) {
        String[] tempStrs;
        GreetContentBean greetContentBean = Constants.greetContentBean;
        if (greetContentBean == null) {//没有全局默认的情况
            if (isVip) {
                String tempStr = mContext.getString(R.string.hello) + "，" + userName + "！";
                tempStrs = Constants.createWelcomeText(mSceneKey, tempStr);
                mView.setRobotChatText(tempStrs[0]);
                mView.speakText(tempStrs[1]);
            } else {
                String tempStr = mContext.getString(R.string.hello) + "，" + mContext.getString(R.string.welcome) + "！";
                tempStrs = Constants.createWelcomeText(mSceneKey, tempStr);
                mView.setRobotChatText(tempStrs[0]);
                mView.speakText(tempStrs[1]);
            }
            return;
        }
        String greetContent = null;
        boolean isTimeEnable = false;
        if (TextUtils.equals(greetContentBean.getTimeEnable(), "1")) {//时间段问候开启
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
//            try {
//                Date nowTime = sdf.parse(sdf.format(new Date()));
//                Date beginTime = sdf.parse(greetContentBean.getStartTime());
//                Date endTime = sdf.parse(greetContentBean.getEndTime());
//                Calendar nowCalendar = Calendar.getInstance();
//                nowCalendar.setTime(nowTime);
//                Calendar beginCalendar = Calendar.getInstance();
//                beginCalendar.setTime(beginTime);
//                Calendar endCalendar = Calendar.getInstance();
//                endCalendar.setTime(endTime);
//                if (nowCalendar.after(beginCalendar) && nowCalendar.before(endCalendar)) {//时间段匹配
//                    isTimeEnable = true;
//                    greetContent = greetContentBean.getWords();//欢迎语为时间段设置的欢迎语
//                } else {//时间段不匹配
//                    greetContent = greetContentBean.getGreetContent();
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

            String welcomeWord = getWelcomeWordsFromTime(greetContentBean.getTimePeriodInfo());
            if (welcomeWord != null) {
                isTimeEnable = true;
                greetContent = welcomeWord;
            } else {
                greetContent = greetContentBean.getGreetContent();
            }

        } else {//不开启等其他情况用全局的
            greetContent = greetContentBean.getGreetContent();
        }

        String comma = Constants.isI18N() ? "" : "，";
        if (isVip) {
            if (isTimeEnable || TextUtils.equals("0", greetContentBean.getGreetType())) {//文本

                String tempStr = mContext.getString(R.string.hello) + "，" + userName + "！"
                        + (TextUtils.isEmpty(greetContent) ? "" : greetContent);

                if (TextUtils.isEmpty(greetContent)) {
                    tempStrs = Constants.createWelcomeText(mSceneKey, tempStr); // + comma);
                    mView.setRobotChatText(tempStrs[0]);
                    mView.speakText(tempStrs[1]);
                } else {
                    mView.setRobotChatText(tempStr);
                    mView.speakText(tempStr);
                }
            } else if (TextUtils.equals("1", greetContentBean.getGreetType())) {//音频
                mAuidoUtil.play(greetContent, null);
                String tempStr = mContext.getString(R.string.hello) + "，" + userName + "！";
//                tempStrs = Constants.createWelcomeText(mSceneKey, tempStr);
                if (!TextUtils.isEmpty(greetContent)) {
//                    mView.setRobotChatText(tempStrs[0]);
                    mView.setRobotChatText(tempStr);
                } else {
                    mView.setRobotChatText(greetContent);
                }
            }
        } else {
            if (isTimeEnable || TextUtils.equals("0", greetContentBean.getGreetType())) {//文本
                String defaultText = mContext.getString(R.string.hello) + "，" + mContext.getString(R.string.welcome) + "！";
                if (TextUtils.isEmpty(greetContent)) {
                    String tempStr = TextUtils.isEmpty(greetContent) ? defaultText : greetContent; //+ comma;
                    tempStrs = Constants.createWelcomeText(mSceneKey, tempStr);
                    mView.setRobotChatText(tempStrs[0]);
                    mView.speakText(tempStrs[1]);
                } else {
                    mView.setRobotChatText(greetContent);
                    mView.speakText(greetContent);
                }

            } else if (TextUtils.equals("1", greetContentBean.getGreetType())) {//音频
                mAuidoUtil.play(greetContent, null);
                String tempStr = mContext.getString(R.string.hello) + "，" + mContext.getString(R.string.welcome) + "！";
//                tempStrs = Constants.createWelcomeText(mSceneKey, tempStr);
//                mView.setRobotChatText(tempStrs[0]);
                mView.setRobotChatText(tempStr);

            }else {
                mView.setRobotChatText(greetContent);
                mView.speakText(greetContent);
            }
        }
    }

    /**
     * 跳转导航页
     */
    private void goNaviAct() {
        clickNaviType = Constants.NaviType.Guide;
        if (!isLoadMapSuccess) {
            mView.restoreMapDialog();
        } else {
            ARouter.getInstance().build(BRouterPath.NAVI_MAIN).navigation();
        }
    }

    /**
     * 跳转到大厅巡视
     * 优先级 恢复地图 密码页面 大厅巡视页面
     */
    private void goPatrolAct() {
        clickNaviType = Constants.NaviType.Patrol;
        if (!isLoadMapSuccess) {
            mView.restoreMapDialog();
        } else {
            BRouter.jumpActivity(BRouterPath.PATROL_PWD);
        }
    }

    private void goNaviActAI(NaviBean current, boolean isPointKnow) {
        clickNaviType = Constants.NaviType.Guide;
        if (!isLoadMapSuccess) {
            mView.restoreMapDialog();
        } else {
            NaviAction.getInstance().setCurrent(current);
            ARouter.getInstance()
                    .build(BRouterPath.NAVI_MAIN)
                    .withBoolean(BRouterKey.FROM_AI_GUIDE, true)
                    .withBoolean(BRouterKey.IS_POINT_KNOW, isPointKnow)
                    .navigation();
        }
    }


}
