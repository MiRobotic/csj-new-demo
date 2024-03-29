package com.csjbot.mobileshop.feature.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.Toast;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.util.ClearUtil;
import com.csjbot.mobileshop.util.SharePreferenceTools;
import com.csjbot.mobileshop.widget.NewRetailDialog;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by xiasuhuei321 on 2017/10/20.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class SettingsResetActivity extends BaseModuleActivity {
    @BindView(R.id.et_pwd)
    EditText mEtPwd;

    private SharePreferenceTools sp;
    private String pwd;
    private String password;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_reset, R.layout.vertical_activity_reset);

    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setSettingLayoutGone();

        mEtPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});//最多只能输入10个字符
//        CheckOutUtil.getInstance().verifyUseName(mEtPwd, this);

        sp = new SharePreferenceTools(context);
        if (sp.getString("pwd_word") != null) {
            pwd = sp.getString("pwd_word");
        } else {
            pwd = "csjbot";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
    }

    @OnClick(R.id.bt_reset)
    public void reset() {
        password = mEtPwd.getText().toString();
        // 校验输入的密码
        if (!pwd.equals(password)) {
            Toast.makeText(context, R.string.check_pwd, Toast.LENGTH_SHORT).show();
            return;
        }

        showNewRetailDialog(getString(R.string.restart_hint), getString(R.string.restore_factory_settings), new NewRetailDialog.OnDialogClickListener() {
            @Override
            public void yes() {
                ProgressDialog dialog = new ProgressDialog(SettingsResetActivity.this);
                dialog.setTitle(getString(R.string.recovering));
                dialog.setMessage(getString(R.string.reseting));
                dialog.show();
                // 清除用户数据，目前只清除产品图片文件下的所有文件
                ClearUtil.clearUserData(new Observer() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

                dismissNewRetailDialog();
            }

            @Override
            public void no() {
                dismissNewRetailDialog();
            }
        });
    }
}
