package com.csjbot.mobileshop.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.util.CSJToast;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;

import butterknife.BindView;

public class SettingSYingbinActivity extends BaseModuleActivity {
    @BindView(R.id.charging_pile_switch)
    CheckBox charging_pile_switch;

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_yingbin_setting, R.layout.vertical_activity_yingbin_setting);

    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        charging_pile_switch.setChecked(SharedPreUtil.getBoolean(SharedKey.YINGBINGSETTING, SharedKey.ISACTIVE, false));
        charging_pile_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            activeYingbin(isChecked);
        });
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setSettingLayoutGone();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void activeYingbin(boolean ye) {
        if (SharedPreUtil.putBoolean(SharedKey.YINGBINGSETTING, SharedKey.ISACTIVE, ye)) {
            CSJToast.showToast(this, getString(R.string.save_success), 1000);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}
