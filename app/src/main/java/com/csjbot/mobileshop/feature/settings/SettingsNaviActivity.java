package com.csjbot.mobileshop.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingwc on 2018/7/11.
 */
public class SettingsNaviActivity extends BaseModuleActivity {


    @BindView(R.id.cb_navi_auto_exit)
    CheckBox cb_navi_auto_exit;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_settings_navi, R.layout.vertical_activity_settings_navi);
    }

    @Override
    public void init() {
        super.init();
        boolean isChecked = SharedPreUtil.getBoolean(SharedKey.NAVI_AUTO_EXIT, SharedKey.NAVI_AUTO_EXIT, cb_navi_auto_exit.isChecked());
        cb_navi_auto_exit.setChecked(isChecked);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setSettingLayoutGone();
    }


    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
    }

    @OnClick(R.id.bt_ok)
    public void ok() {
        SharedPreUtil.putBoolean(SharedKey.NAVI_AUTO_EXIT, SharedKey.NAVI_AUTO_EXIT, cb_navi_auto_exit.isChecked());
        Toast.makeText(context, R.string.save_success, Toast.LENGTH_SHORT).show();
        speak(R.string.save_success);
    }

}
