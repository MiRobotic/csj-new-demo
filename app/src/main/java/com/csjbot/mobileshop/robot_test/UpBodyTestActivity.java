package com.csjbot.mobileshop.robot_test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.coshandler.service.CsjlogService;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.TestActivity;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.dialog.NewRetailEdittextDialog;
import com.csjbot.mobileshop.feature.navigation.ArmLooper;
import com.csjbot.mobileshop.jpush.manager.CsjJPushManager;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.service.HomeService;
import com.csjbot.mobileshop.util.BlackgagaLogger;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.mobileshop.util.ShellUtils;
import com.csjbot.printer.CsjPrinter;
import com.csjbot.printer.bean.BluetoothBean;
import com.csjbot.printer.callback.BlueSearchCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingwc on 2017/11/3.
 */

@Route(path = BRouterPath.UPBODY_TEST)
public class UpBodyTestActivity extends BaseModuleActivity {

    @BindView(R.id.tb_setBaidu_wifi)
    Button tb_setBaidu_wifi;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_upbody_test;
    }

    @BindView(R.id.tv_connect_state)
    TextView tv_connect_state;

    private volatile boolean canClick = true;

    @Override
    public void init() {
        super.init();

        if (RobotManager.getInstance().getConnectState()) {
            tv_connect_state.setText(R.string.connection_linux_success);
        } else {
            tv_connect_state.setText(R.string.connect_linux_failed);
        }

        csjPrinter = CsjPrinter.getInstance(this.getApplicationContext());
        csjPrinter.setBlueSearchCallback(new BlueSearchCallback() {
            @Override
            public void complete(ArrayList<BluetoothBean> bluetoothBeans) {
                mBluetoothList = bluetoothBeans;
                showBluetoothPop();
            }

            @Override
            public void empty() {
                Toast.makeText(context, "未搜索到蓝牙设备", Toast.LENGTH_SHORT).show();
                pdSearch.dismiss();
            }
        });
        csjPrinter.setPrinterConnectCallback(() -> {
            pdConnect.dismiss();
            showSuccessDialog();
        });

        // 如果不是百度的包，则不显示这个按钮
        if (!BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_BAIDU)) {
            tb_setBaidu_wifi.setVisibility(View.GONE);
        }

        int number = SharedPreUtil.getInt(SharedKey.AUTO_SPEECH_RECOGNITION_CLOSE_TIME, SharedKey.AUTO_SPEECH_RECOGNITION_CLOSE_TIME, 30);
        et_speech_time.setText(String.valueOf(number));

    }

    @OnClick(R.id.bt_open_bug_window)
    public void bt_open_bug_window() {
        startService(new Intent(this, CsjlogService.class));
    }

    @OnClick(R.id.bt_close_bug_window)
    public void bt_close_bug_window() {
        stopService(new Intent(this, CsjlogService.class));
    }

    @OnClick(R.id.bt_deny)
    public void denyAction() {
        if (canClick) {
            new Thread(() -> {
                canClick = false;
                Robot.getInstance().AliceHeadLeft();

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Robot.getInstance().AliceHeadRight();

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Robot.getInstance().AliceHeadRight();

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Robot.getInstance().AliceHeadLeft();
                canClick = true;
            }).start();
        }
    }

    @OnClick(R.id.bt_nod)
    public void nodAction() {
        if (canClick) {
            new Thread(() -> {
                canClick = false;
                Robot.getInstance().AliceHeadDown();
                CsjlogProxy.getInstance().debug("低头");


                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Robot.getInstance().AliceHeadUp();
                CsjlogProxy.getInstance().debug("抬头");
                canClick = true;
            }).start();
        }
    }

    @OnClick(R.id.bt_left_large_arm_up)
    public void bt_left_large_arm_up() {
        ServerFactory.getActionInstance().leftLargeArmUp();
    }

    @OnClick(R.id.bt_left_large_arm_down)
    public void bt_left_large_arm_down() {
        ServerFactory.getActionInstance().leftLargeArmDown();
    }

    @OnClick(R.id.bt_left_small_arm_up)
    public void bt_left_small_arm_up() {
        ServerFactory.getActionInstance().leftSmallArmUp();
    }

    @OnClick(R.id.bt_left_small_arm_down)
    public void bt_left_small_arm_down() {
        ServerFactory.getActionInstance().leftSmallArmDown();
    }

    @OnClick(R.id.bt_right_large_arm_up)
    public void bt_right_large_arm_up() {
        ServerFactory.getActionInstance().righLargeArmUp();
    }

    @OnClick(R.id.bt_right_large_arm_down)
    public void bt_right_large_arm_down() {
        ServerFactory.getActionInstance().rightLargeArmDown();
    }

    @OnClick(R.id.bt_right_small_arm_up)
    public void bt_right_small_arm_up() {
        ServerFactory.getActionInstance().rightSmallArmUp();
    }

    @OnClick(R.id.bt_right_small_arm_down)
    public void bt_right_small_arm_down() {
        ServerFactory.getActionInstance().rightSmallArmDown();
    }

    @OnClick(R.id.bt_test_print)
    public void bt_test_print() {
        RobotManager robotManager = RobotManager.getInstance();
        robotManager.robot.reqProxy.openPrinter();
        robotManager.robot.reqProxy.printQRCode("www.example.com");
    }

    @OnClick(R.id.bt_set_alias)
    public void setAlias() {
        CsjJPushManager.getInstance().setAlias((int) System.currentTimeMillis(), Robot.SN);
    }

    @OnClick(R.id.bt_push)
    public void push() {
    }

    ArmLooper looper = new ArmLooper();

    @OnClick(R.id.bt_start_loop)
    public void startLoop() {
        looper.startLooper();
    }

    @OnClick(R.id.bt_stop_loop)
    public void stopLoop() {
        looper.stopLooper();
    }

    @OnClick(R.id.bt_micro_volume)
    public void bt_micro_volume() {
        jumpActivity(TestMicroVolumeActivity.class);
    }

    @OnClick(R.id.bt_ifly)
    public void bt_ifly() {
        RobotManager.getInstance().transparentTransmission("{\"msg_id\":\"SET_SPEECH_TRAGEDY_CMD\",\"selection\":1}");
    }

    @OnClick(R.id.bt_microsoft)
    public void bt_microsoft() {
        RobotManager.getInstance().transparentTransmission("{\"msg_id\":\"SET_SPEECH_TRAGEDY_CMD\",\"selection\":2}");
    }


    @OnClick(R.id.bt_test_search)
    public void bt_test_search() {
        Robot.getInstance().setOnNaviSearchListener(json -> BlackgagaLogger.debug("收到查询结果：" + json));

        new Thread(() -> {
//            while (true) {
            Robot.getInstance().reqProxy.search();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            }
        }).start();
    }

    @OnClick(R.id.bt_setSNNull)
    public void setSNNull() {
        NewRetailEdittextDialog dialog = new NewRetailEdittextDialog(this);
        dialog.setTitle(getString(R.string.please_input_pw));
        dialog.setHintText(getString(R.string.please_input_pw));
        dialog.setListener(new NewRetailEdittextDialog.OnDialogClickListener() {
            @Override
            public void yes(String pwd) {
                if ("1314csjbot".equals(pwd)) {
//                    String path = Environment.getExternalStorageDirectory().getPath();
//                    path += File.separator + ".robot_info";
                    ShellUtils.execCommand("rm -rf /sdcard/.robot_info", true, true);
                    RobotManager.getInstance().robot.reqProxy.setSN("");
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void no() {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @OnClick(R.id.btn_config_hotword)
    void configHotword() {
        startActivity(new Intent(this, ConfigHotwordActivity.class));
    }


    @OnClick(R.id.btn_av_test)
    void btn_av_test() {
        startActivity(new Intent(this, AVTestActivity.class));
    }

    @OnClick(R.id.btn_wakeup_test)
    void btn_wakeup_test(){
        startActivity(new Intent(this, WakeupTestActivity.class));
    }


    @BindView(R.id.et_speech_time)
    EditText et_speech_time;

    @OnClick(R.id.bt_speech_time)
    public void bt_speech_time() {
        String time = et_speech_time.getText().toString().trim();
        if (!TextUtils.isEmpty(time)) {
            int number = Integer.parseInt(time);
            SharedPreUtil.putInt(SharedKey.AUTO_SPEECH_RECOGNITION_CLOSE_TIME, SharedKey.AUTO_SPEECH_RECOGNITION_CLOSE_TIME, number);
            HomeService.autoCloseSpeechTime = number;
            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        }

    }

    @BindView(R.id.et_expression)
    EditText et_expression;

    @OnClick(R.id.bt_expression_switch)
    public void bt_expression_switch() {
        String text = et_expression.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        int expression = Integer.valueOf(text);
        RobotManager.getInstance()
                .robot
                .reqProxy
                .setExpression(expression
                        , 0
                        , 0);
    }

    @OnClick(R.id.tb_setBaidu_wifi)
    public void setBaiduWifi() {
        if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_BAIDU)) {
        }
    }

    /////////////////////// 蓝牙打印功能测试↓↓↓↓↓↓↓↓↓↓↓↓↓

    ProgressDialog pdSearch;

    ProgressDialog pdConnect;

    ArrayList<BluetoothBean> mBluetoothList;

    PopupWindow pw;

    CsjPrinter csjPrinter;

    @OnClick(R.id.bt_connect_bluetooth)
    public void bt_connect_bluetooth() {
        pdSearch = ProgressDialog.show(this, "", "搜索中", true, true);
        pdSearch.setCanceledOnTouchOutside(false);
        pdSearch.setOnKeyListener((dialog, keyCode, event) -> true);
        pdSearch.show();
        csjPrinter.getBluetoothList();
    }

    @OnClick(R.id.bt_print_number)
    public void bt_print_number() {
        String title = "北京市长安公证处";
        String number = "2025";
        String content = "您将要办理的市:国内使用,共有5人在等待,请您在位置上放心等候,我们会广播通知您!（过号请重新取号,谢谢您的合作,欢迎您光临）排队开始时间:2018-05-08 10:30";
        csjPrinter.printer(title, number, content);
    }

    @OnClick(R.id.bt_print_product)
    public void bt_print_product() {
        csjPrinter.printerProduct();

    }

    @OnClick(R.id.bt_start_face_follow)
    public void bt_start_face_follow() {
        Robot.getInstance().startFaceFollow();
    }

    @OnClick(R.id.bt_stop_face_follow)
    public void bt_stop_face_follow() {
        Robot.getInstance().stopFaceFollow();
    }


    private void showBluetoothPop() {
        pdSearch.dismiss();
        View view = LayoutInflater.from(UpBodyTestActivity.this).inflate(R.layout.layout_bluetooth, null);
        ListView mListView = view.findViewById(R.id.lv_bluetooth);
        MyBluetoothAdapter myBluetoothAdapter = new MyBluetoothAdapter();
        mListView.setAdapter(myBluetoothAdapter);
        mListView.setOnItemClickListener((adapterView, view1, position, l) -> {
            if (0 != mBluetoothList.size()) {
                closePopupWindow();
                pdConnect = ProgressDialog.show(UpBodyTestActivity.this, "", "开始连接", true, true);
                pdConnect.setCanceledOnTouchOutside(false);
                pdConnect.show();
                csjPrinter.connect(mBluetoothList.get(position));
            }
        });
        pw = new PopupWindow(view, (int) (getScreenWidth() * 0.8), -2);
        closePopupWindow();
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        pw.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = UpBodyTestActivity.this.getWindow().getAttributes();
            lp1.alpha = 1f;
            getWindow().setAttributes(lp1);
        });
        //显示
        pw.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void closePopupWindow() {
        if (pw != null && pw.isShowing()) {
            pw.dismiss();
            pw = null;
        }
    }

    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }


    private class MyBluetoothAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBluetoothList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBluetoothList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(UpBodyTestActivity.this).inflate(R.layout.item_bluetooth, parent, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.item_text = convertView.findViewById(R.id.item_text);
            holder.item_text_address = convertView.findViewById(R.id.item_text_address);
            holder.item_text.setText(mBluetoothList.get(position).mBluetoothName);
            holder.item_text_address.setText(mBluetoothList.get(position).mBluetoothAddress);
            return convertView;
        }

        class ViewHolder {
            TextView item_text;
            TextView item_text_address;
        }
    }


    private void showSuccessDialog() {
        pdSearch.dismiss();
        Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
    }
}
