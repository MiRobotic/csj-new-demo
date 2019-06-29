package com.csjbot.mobileshop.feature.settings.checked_update;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.csjbot.coshandler.listener.OnGetVersionListener;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.core.RobotManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiasuhuei321 on 2017/10/20.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class SettingsCheckUpdateActivity extends BaseModuleActivity implements SettingUpdateAppContract.View {
    @BindView(R.id.tv_linux_version)
    TextView tvLinuxVersion;
    @BindView(R.id.tv_android_version)
    TextView tvAndroidVersion;
    @BindView(R.id.btn_update_linux)
    Button btnUpdateLinux;
    @BindView(R.id.btn_update_android)
    Button btnUpdateAndroid;

    @OnClick({R.id.btn_update_linux, R.id.btn_update_android})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_linux:
                mPresenter.updateLinux();
                break;
            case R.id.btn_update_android:
                mPresenter.updateAndroid();
                break;
        }
    }

    private SettingUpdateAppContract.Presenter mPresenter;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_check_update, R.layout.vertical_activity_check_update);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        tvAndroidVersion.setText(BuildConfig.VERSION_NAME);
        new SettingUpdateAppPresenterImpl(this, this);
        mPresenter.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setPresenter(SettingUpdateAppContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLinuxVersion(String version) {
        runOnUiThread(() -> tvLinuxVersion.setText(version));
    }
}
