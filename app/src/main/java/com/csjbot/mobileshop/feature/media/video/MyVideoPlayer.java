package com.csjbot.mobileshop.feature.media.video;

import android.content.Context;
import android.util.AttributeSet;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.guide_patrol.patrol.event.PatrolEvent;

import org.greenrobot.eventbus.EventBus;

import cn.jzvd.JzvdStd;

/**
 * @author ShenBen
 * @date 2019/1/23 9:55
 * @email 714081644@qq.com
 */
public class MyVideoPlayer extends JzvdStd {
    public MyVideoPlayer(Context context) {
        super(context);
    }

    public MyVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_full_screen;
    }

    @Override
    public void startVideo() {
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, true));
        EventBus.getDefault().post(new PatrolEvent(PatrolEvent.PLAYING));
        Constants.sIsIntoOtherApp = true;
        super.startVideo();
    }

    @Override
    public void onCompletion() {
        EventBus.getDefault().post(new PatrolEvent(PatrolEvent.PLAYEND));
        Constants.sIsIntoOtherApp = false;
        super.onCompletion();
    }
}
