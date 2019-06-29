package com.csjbot.mobileshop.feature.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.dialog.NewRetailEdittextDialog;
import com.csjbot.mobileshop.util.BlackgagaLogger;
import com.csjbot.mobileshop.util.ZxingUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnGetVersionListener;
import com.csjbot.coshandler.listener.OnSNListener;
import com.csjbot.coshandler.listener.OnSetSNListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by 孙秀艳 on 2017/10/20.
 * 系统设置关于界面
 */

public class SettingsAboutActivity extends BaseModuleActivity {

    @BindView(R.id.settings_about_info_version)
    TextView tvRobotInfoVersion;//机器人信息版本
    @BindView(R.id.settings_about_model)
    TextView tvRobotModel;//机器人型号
    @BindView(R.id.settings_about_memory)
    TextView tvRobotMemory;//机器人运行内存
    @BindView(R.id.settings_about_sn)
    TextView tvRobotSN;//机器人SN
    @BindView(R.id.settings_about_ui_version)
    TextView tvRobotUIVersion;//机器人UI版本
    @BindView(R.id.settings_about_service_version)
    TextView tvRobotServiceVersion;//机器人服务版本
    @BindView(R.id.settings_about_copyright)
    TextView tvRobotVersion;//机器人版权
    @BindView(R.id.image_qrcode)
    ImageView image_qrcode;

    private String code;
    private boolean canGetDeviceInfo = false;

    @Override
    public void init() {
        super.init();
        getTitleView().setSettingLayoutGone();
        tvRobotUIVersion.setText(BuildConfig.VERSION_NAME);
        tvRobotModel.setText(BuildConfig.robotType);
    }

    /**
     * 设置机器人信息版本
     */
    private void setRobotVersion() {
        RobotManager.getInstance().addListener((OnGetVersionListener) version -> runOnUiThread(() -> {
            tvRobotInfoVersion.setText(version);
        }));
        mainHandler.postDelayed(() -> RobotManager.getInstance().robot.reqProxy.getVersion(), 200);
    }

    /**
     * 设置机器人SN
     */
    private void setSN() {
        RobotManager.getInstance().addListener(snListener);
        RobotManager.getInstance().robot.reqProxy.getSN();
    }

    private void insertQrCodeInImage(String sn) {
//        String content = Robot.SN;
        String content = sn;
        Bitmap bitmap = ZxingUtil.createQRCode(content, image_qrcode.getWidth(), image_qrcode.getHeight());
        if (bitmap != null) {
            image_qrcode.setImageBitmap(bitmap);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (RobotManager.getInstance().getConnectState()) {
            setSN();
            setRobotVersion();
        } else {
            Toast.makeText(context, getString(R.string.not_connect_slam), Toast.LENGTH_SHORT).show();
            canGetDeviceInfo = true;
            BlackgagaLogger.debug("底层未连接");
        }
        tvRobotSN.setText(Robot.SN);
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_settings_about, R.layout.vertical_activity_settings_about);

    }

    @Override
    public boolean isOpenChat() {
        return false;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    int count = 0;
    ProgressDialog dialog = null;


    Handler h = new Handler();

    @OnClick(R.id.button_get)
    public void getRecognitionNumber() {
        Bean bean = new Bean();
        bean.setSn(Robot.SN);
        Random random = new Random();
        DecimalFormat df = new DecimalFormat("0000");
        code = df.format(random.nextInt(10000));
        bean.setDynamiccode(code);
        //下面发送动态码

    }

    public void showCode() {
        TextView textView = new TextView(getApplicationContext());
        textView.setTextSize(getResources().getDimension(R.dimen.btn_text_size));
        textView.setText(code);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(textView);
        builder.setNegativeButton(getString(R.string.sure), (dialog, which) -> {
            dialog.dismiss();
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @OnLongClick(R.id.ll_sn)
    public boolean setSn() {
        if (!RobotManager.getInstance().getConnectState()) {
            Toast.makeText(context, getString(R.string.not_connect_slam), Toast.LENGTH_SHORT).show();
            BlackgagaLogger.debug("底层未连接");
            return false;
        }

        if (!canGetDeviceInfo) {
            return false;
        }

        NewRetailEdittextDialog dialog = new NewRetailEdittextDialog(this);
        dialog.setTitle("SN");
        dialog.setListener(new NewRetailEdittextDialog.OnDialogClickListener() {
            @Override
            public void yes(String sn) {
                RobotManager.getInstance().robot.reqProxy.setSN(sn);
                RobotManager.getInstance().addListener((OnSetSNListener) BlackgagaLogger::debug);
                dialog.dismiss();
            }

            @Override
            public void no() {
                dialog.dismiss();
            }
        });
        dialog.show();
        return true;
    }

    @OnClick(R.id.ll_sn)
    public void getSn() {
        if (!canGetDeviceInfo) return;

        count++;
        if (count >= 3 && count < 6) {
            Toast.makeText(this, getString(R.string.click_again) + (6 - count) + getString(R.string.enter_into_sn_view), Toast.LENGTH_SHORT).show();
        }

        if (count >= 6) {
            if (!RobotManager.getInstance().getConnectState()) {
                Toast.makeText(context, getString(R.string.not_connect_slam), Toast.LENGTH_SHORT).show();
                BlackgagaLogger.debug("底层未连接");
                return;
            }

            dialog = new ProgressDialog(this);
            dialog.setTitle(getString(R.string.commution));
            dialog.setMessage(getString(R.string.get_device_info));
            dialog.show();
            dialog.setOnDismissListener(dialog1 -> h.removeCallbacksAndMessages(null));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RobotManager.getInstance().removeSnListener(snListener);
    }

    public class Bean {

        /**
         * sn :
         * dynamiccode  :
         */

        private String sn;
        private String dynamiccode;

        public Bean() {
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getDynamiccode() {
            return dynamiccode;
        }

        public void setDynamiccode(String dynamiccode) {
            this.dynamiccode = dynamiccode;
        }
    }

    public class Result {

        /**
         * message : ok
         * result : {"check_result":true}
         * status : 200
         */

        private String message;
        private ResultBean result;
        private String status;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public class ResultBean {
            /**
             * check_result : true
             */

            private boolean check_result;

            public boolean isCheck_result() {
                return check_result;
            }

            public void setCheck_result(boolean check_result) {
                this.check_result = check_result;
            }
        }
    }

    private OnSNListener snListener = new OnSNListener() {
        @Override
        public void response(String sn) {
            try {
                JSONObject jo = new JSONObject(sn);
                String str = jo.optString("sn");
                if (TextUtils.isEmpty(str) || str.contains("empty")) {
                    // sn 未能获取到
                    BlackgagaLogger.debug("未能获取到SN信息");
                    canGetDeviceInfo = true;
                } else {
                    runOnUiThread(() -> {
                        if (dialog != null) dialog.dismiss();
                        BlackgagaLogger.debug("获取到SN信息");
                        tvRobotSN.setText(str);
                        insertQrCodeInImage(str);
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
                BlackgagaLogger.debug("发生异常");
            }
        }
    };
}
