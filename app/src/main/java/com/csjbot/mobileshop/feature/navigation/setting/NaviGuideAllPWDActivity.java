package com.csjbot.mobileshop.feature.navigation.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.util.CheckOutUtil;
import com.csjbot.mobileshop.util.SharePreferenceTools;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 孙秀艳 on 2019/1/21.
 */
public class NaviGuideAllPWDActivity extends BaseModuleActivity {

    @BindView(R.id.et_password)
    EditText mEtPassword;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        if (isPlus()) {
            return R.layout.vertical_activity_settings;
        } else {
            return R.layout.activity_settings;
        }
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setSettingLayoutGone();

        mEtPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                login();
                return true;
            }
            return false;
        });

        CheckOutUtil.getInstance().verifyUseName(mEtPassword, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, true));
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    public void init() {
        super.init();
    }

    @OnClick(R.id.bt_login)
    public void login() {
        SharePreferenceTools sharePreferenceTools = new SharePreferenceTools(this);
        if (sharePreferenceTools.getString("pwd_word") != null) {
            if (sharePreferenceTools.getString("pwd_word").equals(mEtPassword.getText().toString().trim())) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                this.finish();
            } else {
                Toast.makeText(this, getString(R.string.passWord_error), Toast.LENGTH_SHORT).show();
                mEtPassword.setText("");
            }
        } else {
            if (mEtPassword.getText().toString().trim().equals("csjbot")) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                this.finish();
            } else {
                Toast.makeText(this, getString(R.string.passWord_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
