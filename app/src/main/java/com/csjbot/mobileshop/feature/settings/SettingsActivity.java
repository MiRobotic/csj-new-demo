package com.csjbot.mobileshop.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.feature.settings.settings_list.SettingsListActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.router.BRouter;
import com.csjbot.mobileshop.router.BRouterKey;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.util.CheckOutUtil;
import com.csjbot.mobileshop.util.SharePreferenceTools;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingwc on 2017/10/20.
 */

@Route(path = BRouterPath.SETTING_CONFIRM)
public class SettingsActivity extends BaseModuleActivity implements SettingsContract.View {

    SettingsContract.Presenter mPresenter;
    @BindView(R.id.et_password)
    EditText mEtPassword;

    @BindView(R.id.tv_navi_setting)
    TextView tvNaviSetting;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @OnClick(R.id.btn_cancel)
    public void viewOnClick(View view) {
        finish();
    }

    private String activityName;
    private boolean guideAll;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_settings, R.layout.vertical_activity_settings);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setSettingLayoutGone();
        activityName = getIntent().getStringExtra(Constants.JUMP_ACTIVITY_NAME);
        guideAll = getIntent().getBooleanExtra(Constants.GUIDE_ALL, false);

        if (!TextUtils.isEmpty(activityName)) {
            if ((Constants.Scene.CurrentScene.equals(Constants.Scene.XingZheng)
                    || Constants.Scene.CurrentScene.equals(Constants.Scene.CheGuanSuo))) {
                tvNaviSetting.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            }
        }

//        mEtPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});//最多只能输入10个字符
        mEtPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                login();
                return true;
            }
            return false;
        });

        CheckOutUtil.getInstance().verifyUseName(mEtPassword, this);

        if (!TextUtils.isEmpty(activityName)) {
            speak(R.string.input_pwd_first, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, true));
    }

    @Override
    public void init() {
        super.init();
        mPresenter = new SettingsPresenter();
        mPresenter.initView(this);
    }

    @OnClick(R.id.bt_login)
    public void login() {
        SharePreferenceTools sharePreferenceTools = new SharePreferenceTools(this);
        if (sharePreferenceTools.getString("pwd_word") != null) {
            if (sharePreferenceTools.getString("pwd_word").equals(mEtPassword.getText().toString().trim())) {
                if (TextUtils.isEmpty(activityName)) {
                    jumpActivity(SettingsListActivity.class);
                } else {
                    BRouter.getInstance()
                            .build(activityName)
                            .withBoolean(BRouterKey.GUIDE_ALL, guideAll)
                            .navigation();
                }
                this.finish();
            } else {
                Toast.makeText(this, getString(R.string.passWord_error), Toast.LENGTH_SHORT).show();
                mEtPassword.setText("");
            }
        } else {
            if (mEtPassword.getText().toString().trim().equals("csjbot")) {
                if (TextUtils.isEmpty(activityName)) {
                    jumpActivity(SettingsListActivity.class);
                } else {
                    BRouter.getInstance()
                            .build(activityName)
                            .withBoolean(BRouterKey.GUIDE_ALL, guideAll)
                            .navigation();
                }

                this.finish();
            } else {
                Toast.makeText(this, getString(R.string.passWord_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
