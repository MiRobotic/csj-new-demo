package com.csjbot.mobileshop.feature.Learning;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.feature.Learning.event.FloatWindowBackEvent;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.util.ShellUtils;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridLayoutManager;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridSnapHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 学习百科
 * Created by  Wql , 2018/3/7 9:58
 */
@Route(path = BRouterPath.LEARN_EDUCATION)
public class LearningEducationActivity extends BaseModuleActivity {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    @BindView(R.id.index_left)
    ImageView mIvLeft;
    @BindView(R.id.index_right)
    ImageView mIvRight;

    @OnClick({R.id.index_left, R.id.index_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.index_left:
                mManager.smoothPrePage();
                break;
            case R.id.index_right:
                mManager.smoothNextPage();
                break;
            default:
                break;
        }
    }

    private PagerGridLayoutManager mManager;
    private LearningEducationAdatper mAdapter;
    private List<LearnBean> mLists;

    private PackageManager pManager;

    private boolean isStartService = false;

    private boolean isResume = false;

    public String currentStartApp = "";

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void init() {
        super.init();
        initRecy();
        getAllAlowedApps();
    }

    private void initRecy() {
        mLists = new ArrayList<>();
        if (isPlus()) {
            mAdapter = new LearningEducationAdatper(R.layout.item_learningeducation_vertical, mLists);
        } else {
            mAdapter = new LearningEducationAdatper(R.layout.item_learningeducation, mLists);
        }

        mManager = new PagerGridLayoutManager(2, 3, PagerGridLayoutManager.HORIZONTAL);
        mRecyclerview.setLayoutManager(mManager);
        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(mRecyclerview);
        mRecyclerview.setNestedScrollingEnabled(false);
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_HIDE));
            currentStartApp = mLists.get(position).getPakageName();
            Intent i = pManager.getLaunchIntentForPackage(mLists.get(position).getPakageName());
            startActivity(i);
            EventBus.getDefault().post(new FloatWindowBackEvent(true));
            isStartService = true;
            Constants.sIsIntoOtherApp = true;
        });
    }

    private void getAllAlowedApps() {
        List<PackageInfo> appList = getAllApps();
        pManager = getPackageManager();
        for (int i = 0; i < appList.size(); i++) {
            PackageInfo pinfo = appList.get(i);
            String appName = pManager.getApplicationLabel(pinfo.applicationInfo).toString().trim();
//            if (!TextUtils.equals(appName, "搜狗输入法") &&
//                    !TextUtils.equals(appName, "应用宝") &&
//                    !TextUtils.equals(appName, "讯飞语记") &&
//                    !TextUtils.equals(appName, "新零售_d") &&
//                    !TextUtils.equals(appName, "ADB WiFi") &&
//                    !TextUtils.equals(appName, "Serial Port API sample")) {
//                LearnBean shareItem = new LearnBean();
//                // 设置应用程序名字
//                shareItem.setAppName(pManager.getApplicationLabel(pinfo.applicationInfo).toString());
//                // 设置应用图片
//                shareItem.setIcon(pManager.getApplicationIcon(pinfo.applicationInfo));
//                //设置应用包名
//                shareItem.setPakageName(pinfo.applicationInfo.packageName);
//                Log.e("程序名", pManager.getApplicationLabel(pinfo.applicationInfo).toString());
//                mLists.add(shareItem);
//            }
            LearnBean shareItem = new LearnBean();
            if (appName.contains("喜马拉雅FM")) {
                shareItem.setAppName("喜马拉雅FM");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_01));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            } else if (appName.equals("巴巴熊成语故事动画")) {
                shareItem.setAppName("成语故事");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_02));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            } else if (appName.equals("儿童游戏弹钢琴")) {
                shareItem.setAppName("儿童钢琴");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_03));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            } else if (appName.equals("儿童教育学数学")) {
                shareItem.setAppName("儿童数学");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_04));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            } else if (appName.equals("看图识字")) {
                shareItem.setAppName("看图识字");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_05));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            } else if (appName.equals("儿童学拼音游戏")) {
                shareItem.setAppName("拼音游戏");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_06));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            } else if (appName.equals("小学英语助手")) {
                shareItem.setAppName("小学英语");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_07));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            } else if (appName.equals("FingerPen Coloring Book")) {
                shareItem.setAppName("颜色屋");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_08));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            } else if (appName.equals("Princess Pea")) {
                shareItem.setAppName("童话书屋");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_10));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            }
        }

        if (!mLists.isEmpty()) {
            if (mLists.size() > 6) {//一页显示不下，则显示左右按钮图标
                mIvLeft.setVisibility(View.VISIBLE);
                mIvRight.setVisibility(View.VISIBLE);
            } else {
                mIvLeft.setVisibility(View.INVISIBLE);
                mIvRight.setVisibility(View.INVISIBLE);
            }
            mAdapter.setNewData(mLists);
            if (CsjLanguage.CURRENT_LANGUAGE == CsjLanguage.CHINESE && mLists.size() > 2) {
                setRobotChatMsg("你可以点击屏幕上的按钮，或者通过对我说：\n" +
                        "1、" + mLists.get(0).getAppName() + "\n" +
                        "2、" + mLists.get(1).getAppName() + "\n");
                speak("您可以对我说：" + mLists.get(0).getAppName() + "、" + mLists.get(1).getAppName());
            }
        }


    }

    private Drawable resIdToDrawable(@DrawableRes int resId) {
        return getBaseContext().getResources().getDrawable(resId);
    }

    private List<PackageInfo> getAllApps() {
        List<PackageInfo> apps = new ArrayList<>();
        PackageManager pManager = getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> packlist = pManager.getInstalledPackages(0);
        for (int i = 0; i < packlist.size(); i++) {
            PackageInfo pak = packlist.get(i);
            // if()里的值如果<=0则为自己装的程序，否则为系统工程自带
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                // 添加自己已经安装的应用程序
                apps.add(pak);
            }
        }
        return apps;
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (isResume) {
            if (super.onSpeechMessage(text, answerText)) {
                return false;
            }
        }
        if (mLists.isEmpty()) {
            return false;
        }

        if (text.contains(getString(R.string.back)) ||
                text.contains(getString(R.string.quit)) ||
                text.contains(getString(R.string.colse))) {
            if (isStartService) {
                ShellUtils.execCommand("am force-stop " + currentStartApp, true, false);
                return true;
            }
        }


        for (int i = 0; i < mLists.size(); i++) {
            if (text.contains(mLists.get(i).getAppName())) {
                currentStartApp = mLists.get(i).getPakageName();
                sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_HIDE));
                Intent intent = pManager.getLaunchIntentForPackage(mLists.get(i).getPakageName());
                startActivity(intent);

                EventBus.getDefault().post(new FloatWindowBackEvent(true));
                isStartService = true;
            }
        }
        prattle(answerText);
        return true;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_learningeducation, R.layout.vertical_activity_learningeducation);
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
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW));
        EventBus.getDefault().post(new FloatWindowBackEvent(false));
        isStartService = false;
        isResume = true;
        Constants.sIsIntoOtherApp = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
    }
}
