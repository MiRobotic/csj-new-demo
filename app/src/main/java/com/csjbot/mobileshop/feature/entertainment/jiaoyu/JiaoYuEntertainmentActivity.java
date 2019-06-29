package com.csjbot.mobileshop.feature.entertainment.jiaoyu;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.feature.Learning.event.FloatWindowBackEvent;
import com.csjbot.mobileshop.feature.dance.DanceActivity;
import com.csjbot.mobileshop.feature.music.MusicActivity;
import com.csjbot.mobileshop.feature.story.StoryActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.util.CSJToast;
import com.github.promeg.pinyinhelper.Pinyin;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by jingwc on 2017/10/16.
 */
@Route(path = BRouterPath.JIAO_YU_ENTERTAINMENT)
public class JiaoYuEntertainmentActivity extends BaseModuleActivity {

    private String[] mMusicList = {"YINYUE", "CHANGSHOUGE", "CHANGGE", "LAISHOUGE", "CHANGSHOUGEBA"};
    private String[] mStoryList = {"GUSHI", "JIANGGUSHI", "JIANGGEGUSHI"};
    private String[] mDanceList = {"TIAOWU", "TIAOGEWU", "TIAOZHIWU"};
    private String[] mGameList = {"YOUXI", "WANYOUXI"};

    private List<PackageInfo> mPacklist;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_entertainment_jiaoyu, R.layout.activity_vertical_entertainment_jiaoyu);
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
        mPacklist = new ArrayList<>();

    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        if (CsjLanguage.CURRENT_LANGUAGE == CsjLanguage.CHINESE) {

            mainHandler.postDelayed(() -> setRobotChatMsg("你可以点击屏幕上的按钮，或者通过对我说：\n" +
                    "1.唱首歌吧\n" +
                    "2.讲个故事\n"),500);
            speak("您可以对我说：唱首歌吧");
        } else {
            speak(getString(R.string.entertainment_init_speak), true);
        }
        List<PackageInfo> list = new ArrayList<>(getPackageManager().getInstalledPackages(0));
        for (PackageInfo info : list) {
            // if()里的值如果<=0则为自己装的程序，否则为系统工程自带
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                // 添加自己已经安装的应用程序
                mPacklist.add(info);
            }
        }
    }

    @OnClick(R.id.ib_storytelling)
    public void storytelling() {
        jumpActivity(StoryActivity.class);
    }

    @OnClick(R.id.ib_dance)
    public void dance() {
        jumpActivity(DanceActivity.class);
    }

    @OnClick(R.id.ib_music)
    public void music() {
        jumpActivity(MusicActivity.class);
    }

    @OnClick(R.id.ib_game)
    public void game() {
        if (isPlus()) {
            boolean isExisted = false;//游戏软件是否存在
            for (PackageInfo info : mPacklist) {
                if (TextUtils.equals("com.sinyee.babybus.parkour", info.packageName.trim())) {
                    isExisted = true;
                    Constants.sIsIntoOtherApp = true;
                    EventBus.getDefault().post(new FloatWindowBackEvent(true));
                    startActivity(getPackageManager().getLaunchIntentForPackage(info.applicationInfo.packageName));
                    sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_HIDE));
                    break;
                }
            }
            if (!isExisted) {
                CSJToast.showToast(this, "未找到“宝宝爱跑步”软件");
            }
        } else {
            boolean isExisted = false;//游戏软件是否存在
            for (PackageInfo info : mPacklist) {
                if (TextUtils.equals("com.sinyee.babybus.animal", info.packageName.trim())) {
                    Constants.sIsIntoOtherApp = true;
                    isExisted = true;
                    EventBus.getDefault().post(new FloatWindowBackEvent(true));
                    startActivity(getPackageManager().getLaunchIntentForPackage(info.applicationInfo.packageName));
                    sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_HIDE));
                    break;
                }
            }
            if (!isExisted) {
                CSJToast.showToast(this, "未找到“宝宝乐园”软件");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.sIsIntoOtherApp = false;
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW));
        EventBus.getDefault().post(new FloatWindowBackEvent(false));

    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        String pinyin = strConvertPinyin(text);
        if (!TextUtils.isEmpty(pinyin)) {
            for (String s : mMusicList) {
                if (pinyin.contains(s)) {
                    music();
                    return true;
                }
            }
            for (String s : mStoryList) {
                if (pinyin.contains(s)) {
                    storytelling();
                    return true;
                }
            }
            for (String s : mDanceList) {
                if (pinyin.contains(s)) {
                    dance();
                    return true;
                }
            }
            for (String s : mGameList) {
                if (pinyin.contains(s)) {
                    game();
                    return true;
                }
            }
        }
        prattle(answerText);
        return true;
    }

    /**
     * 汉字转拼音方法
     *
     * @param text
     * @return
     */
    private String strConvertPinyin(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        StringBuilder sbPinyin = new StringBuilder();
        char[] texts = text.toCharArray();
        for (char str : texts) {
            sbPinyin.append(Pinyin.toPinyin(str));
        }
        return sbPinyin.toString();
    }
}
