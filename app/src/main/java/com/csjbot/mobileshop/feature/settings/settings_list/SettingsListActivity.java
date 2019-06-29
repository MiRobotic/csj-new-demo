package com.csjbot.mobileshop.feature.settings.settings_list;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.dialog.LoadMapFileDialog;
import com.csjbot.mobileshop.feature.settings.SettingVirtualKey;
import com.csjbot.mobileshop.feature.settings.SettingsAboutActivity;
import com.csjbot.mobileshop.feature.settings.SettingsActivity;
import com.csjbot.mobileshop.feature.settings.SettingsEvaluateActivity;
import com.csjbot.mobileshop.feature.settings.SettingsManualPositioningActivity;
import com.csjbot.mobileshop.feature.settings.SettingsNaviActivity;
import com.csjbot.mobileshop.feature.settings.SettingsNaviSoundControlActivity;
import com.csjbot.mobileshop.feature.settings.SettingsOtherActivity;
import com.csjbot.mobileshop.feature.settings.SettingsResetActivity;
import com.csjbot.mobileshop.feature.settings.SettingsRobotStateActivity;
import com.csjbot.mobileshop.feature.settings.SettingsSpeedActivity;
import com.csjbot.mobileshop.feature.settings.SettingsVolumeActivity;
import com.csjbot.mobileshop.feature.settings.change_skin.ChangeSkinActivity;
import com.csjbot.mobileshop.feature.settings.charge_setting.ChargeSettingActivity;
import com.csjbot.mobileshop.feature.settings.checked_update.SettingsCheckUpdateActivity;
import com.csjbot.mobileshop.feature.settings.network.SettingsNetworkActivity;
import com.csjbot.mobileshop.feature.settings.pwd_setting.PWDManagementActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.router.BRouter;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.mobileshop.util.ShellUtils;
import com.csjbot.mobileshop.widget.NewRetailDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingwc on 2017/10/20.
 */

public class SettingsListActivity extends BaseModuleActivity implements SettingsListContract.View {

    SettingsListContract.Presenter mPresenter;
    boolean isFinish = false;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_settings_list, R.layout.vertical_activity_settings_list);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        Button bt_set_about = (Button) findViewById(R.id.bt_set_about);
        bt_set_about.setOnLongClickListener(v -> {
            Intent mIntent = new Intent();
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings");
            mIntent.setComponent(comp);
            mIntent.setAction("android.intent.action.VIEW");
            startActivity(mIntent);
            return false;
        });
    }

    @Override
    public void init() {
        super.init();
        mPresenter = new SettingsListPresenter();
        mPresenter.initView(this);

        getTitleView().setSettingLayoutGone();
    }

    @OnClick(R.id.bt_set_network)
    public void setNetwork() {
        jumpActivity(SettingsNetworkActivity.class);
    }

    @OnClick(R.id.charge_setting)
    public void chargeSetting() {
        jumpActivity(ChargeSettingActivity.class);
    }

    @OnClick(R.id.pwd_setting)
    public void pwdSetting() {
        jumpActivity(PWDManagementActivity.class);
    }

//    @OnClick(R.id.syn_data_setting)
//    public void synDataSetting() {
//        jumpActivity(SynchronousDataActivity.class);
//    }

    @OnClick(R.id.bt_check_update)
    public void checkUpdate() {
        jumpActivity(SettingsCheckUpdateActivity.class);
    }

    @OnClick(R.id.bt_reset)
    public void reset() {
        jumpActivity(SettingsResetActivity.class);
    }

    @OnClick(R.id.bt_set_volume)
    public void setVolume() {
        jumpActivity(SettingsVolumeActivity.class);
    }

    @OnClick(R.id.bt_set_speed)
    public void setSpeed() {
        jumpActivity(SettingsSpeedActivity.class);
    }


    @OnClick(R.id.bt_set_about)
    public void setAbout() {
        jumpActivity(SettingsAboutActivity.class);
    }

    @OnClick(R.id.bt_shutdown)
    public void shutdown() {

        showNewRetailDialog(getString(R.string.shutdown_prompt), getString(R.string.shutdown_text), new NewRetailDialog.OnDialogClickListener() {
            @Override
            public void yes() {
                ServerFactory.getRobotState().shutdown();
                ShellUtils.execCommand("reboot -p", true, false);
                dismissNewRetailDialog();
            }

            @Override
            public void no() {
                dismissNewRetailDialog();
            }
        });


    }

    @OnClick(R.id.bt_save_map)
    public void saveMap() {
        saveMapDialog();
    }

    @BindView(R.id.bt_save_map)
    Button btnSaveMap;

    @OnClick(R.id.bt_restore_map)
    public void restoreMap() {
        restoreMapDialog();
    }

    @OnClick(R.id.btn_change_skin)
    public void changeSkin() {
        jumpActivity(ChangeSkinActivity.class);
    }

    @OnClick(R.id.bt_manual_positioning)
    public void bt_manual_positioning() {
        jumpActivity(SettingsManualPositioningActivity.class);
    }

    @OnClick(R.id.bt_evaluation)
    public void bt_evaluation() {
        jumpActivity(SettingsEvaluateActivity.class);
    }

    @OnClick(R.id.bt_navi_sound_control)
    public void bt_navi_sound_control() {
        jumpActivity(SettingsNaviSoundControlActivity.class);
    }

    @OnClick(R.id.bt_virtual_key)
    public void bt_virtual_key() {
        jumpActivity(SettingVirtualKey.class);
    }

    @OnClick(R.id.bt_robot_state)
    public void bt_robot_state() {
        jumpActivity(SettingsRobotStateActivity.class);
    }

//    @OnClick(R.id.bt_semantics)
//    public void bt_semantics() {
//        jumpActivity(SettingsSemanticsActivity.class);
//    }

    @OnClick(R.id.bt_navi_auto_exit)
    public void bt_navi_auto_exit() {
        jumpActivity(SettingsNaviActivity.class);
    }


    @OnClick(R.id.bt_other)
    public void bt_other() {
        jumpActivity(SettingsOtherActivity.class);
    }


    int clickCount = 0;

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW));
    }

    @Override
    protected void onPause() {
        super.onPause();
        clickCount = 0;
    }

    @OnClick(R.id.rl_root)
    public void rlRoot() {
        clickCount++;
        if (clickCount == 6) {
            BRouter.getInstance().build(BRouterPath.UPBODY_TEST).navigation();
        }
    }

    /* 恢复地图对话框 */
    private void restoreMapDialog() {
        if (RobotManager.getInstance().getConnectState()) {
            showNewRetailDialog(getString(R.string.map_manager), getString(R.string.is_restore_map), new NewRetailDialog.OnDialogClickListener() {
                @Override
                public void yes() {
                    restoremap();
                    dismissNewRetailDialog();
                }

                @Override
                public void no() {
                    dismissNewRetailDialog();
                }
            });
        } else {
            Toast.makeText(context, R.string.not_connect_slam, Toast.LENGTH_SHORT).show();
        }
    }

    /* 保存地图对话框 */
    private void saveMapDialog() {
        if (RobotManager.getInstance().getConnectState()) {
            showNewRetailDialog(getString(R.string.map_manager), getString(R.string.is_save_map), new NewRetailDialog.OnDialogClickListener() {
                @Override
                public void yes() {
                    savemap();
                    dismissNewRetailDialog();
                }

                @Override
                public void no() {
                    dismissNewRetailDialog();
                }
            });
        } else {
            Toast.makeText(context, R.string.not_connect_slam, Toast.LENGTH_SHORT).show();
        }
    }

    private void savemap() {
        mPresenter.saveMap();
        isFinish = false;
        checkConnect();
    }

    private void restoremap() {
        mPresenter.restoreMap();
        isFinish = false;
        checkConnect();
    }

    private void checkConnect() {
        btnSaveMap.postDelayed(() -> {
            runOnUiThread(() -> {
                if (!isFinish) {
                    Toast.makeText(SettingsListActivity.this, R.string.check_linux, Toast.LENGTH_SHORT).show();
                }
            });
        }, Constants.internalCheckLinux);
    }

    @Override
    public void saveMapResult(boolean isSuccess) {
        runOnUiThread(() -> {
            isFinish = true;
            if (isSuccess) {
                SharedPreUtil.putBoolean(SharedKey.ISLOADMAP, SharedKey.ISLOADMAP, true);
                Toast.makeText(SettingsListActivity.this, R.string.save_map_success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(SettingsListActivity.this, R.string.save_map_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void restoreMapResult(boolean isSuccess) {
        runOnUiThread(() -> {
            isFinish = true;
            if (isSuccess) {
                SharedPreUtil.putBoolean(SharedKey.ISLOADMAP, SharedKey.ISLOADMAP, true);
                Toast.makeText(SettingsListActivity.this, R.string.restore_map_success, Toast.LENGTH_LONG).show();
            } else {
                CsjlogProxy.getInstance().info("恢复地图失败 Settings");
                restoreMapException();
            }
        });
    }

    public void restoreMapException() {
        new Handler(Looper.getMainLooper()).post(()->{
            LoadMapFileDialog dialog = new LoadMapFileDialog(SettingsListActivity.this);
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW));
    }
}
