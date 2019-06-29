package com.csjbot.mobileshop.feature.settings;

import android.content.Intent;
import android.os.Bundle;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;

import butterknife.OnClick;

/**
 * Created by 荆为成 on 2018/8/30.
 */

public class SettingsOtherActivity extends BaseModuleActivity {

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setSettingLayoutGone();
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_settings_other, R.layout.vertical_activity_settings_other);
    }

    @Override
    public boolean isOpenChat() {
        return false;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW));
    }

    @OnClick(R.id.bt_chatview)
    public void bt_chatview() {
        jumpActivity(SettingsChatViewActivity.class);
    }

    @OnClick(R.id.bt_face_sync)
    public void bt_face_sync() {
        jumpActivity(SettingsFaceSyncActivity.class);
    }

}
