package com.csjbot.mobileshop.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 荆为成 on 2018/8/7.
 */

public class SettingsChatViewActivity extends BaseModuleActivity {


    @BindView(R.id.chatview_switch)
    CheckBox chatview_switch;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);

    }

    @Override
    public void init() {
        super.init();
        getTitleView().setSettingLayoutGone();

        chatview_switch.setChecked(SharedPreUtil.getBoolean(SharedKey.CHAT_VIEW,SharedKey.CHAT_VIEW_IS_OPEN,true));
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return Constants.isPlus()
                ? R.layout.vertical_activity_settings_chat_view : R.layout.activity_settings_chat_view;

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


    @OnClick(R.id.bt_save)
    public void bt_save(){
        boolean isChecked = chatview_switch.isChecked();
        SharedPreUtil.putBoolean(SharedKey.CHAT_VIEW,SharedKey.CHAT_VIEW_IS_OPEN,isChecked);
        Constants.isOpenChatView = isChecked;
        Toast.makeText(context, getString(R.string.save_success), Toast.LENGTH_SHORT).show();

    }

}
