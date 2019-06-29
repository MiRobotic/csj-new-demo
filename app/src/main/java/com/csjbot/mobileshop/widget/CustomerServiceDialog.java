package com.csjbot.mobileshop.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.customer.FloatWindowCustomerEvent;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.router.BRouter;
import com.csjbot.mobileshop.router.BRouterPath;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ShenBen
 * @date 2019/4/3 11:44
 * @email 714081644@qq.com
 */
public class CustomerServiceDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Button mConnectBtn;
    private TextView mTip;
    private ImageView ivClose;
    private OnCustomerDialogClickListener mListener;
    private int DialogStatus;
    private Timer timer;


    protected Handler delayHandler = new Handler(Looper.getMainLooper());

    public CustomerServiceDialog(Context context) {
        super(context, R.style.NewRetailDialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diaglog_customer);
        setCofig();
        initView();
        InitData();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (DialogStatus == Constants.CONNECT_FAILED || DialogStatus == Constants.CONNECT_INTERRUPT) {
            CloseTimeOut();
        }
    }

    private void setCofig() {
        Window dialogWindow = getWindow();
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (!Constants.isPlus()) {
//            lp.width = (int) (d.widthPixels * 0.4);
//            lp.height = (int) (d.heightPixels * 0.5);
        } else {
//            lp.width = (int) (d.widthPixels * 0.8);
//            lp.height = (int) (d.heightPixels * 0.3);
        }
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(false);
    }


    private void initView() {
        mConnectBtn = (Button) findViewById(R.id.tv_btn);
        mTip = (TextView) findViewById(R.id.content_tip);
        ivClose = (ImageView) findViewById(R.id.iv_close);
    }

    private void InitData() {
        mConnectBtn.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivClose.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_btn:
                if (Constants.sIsCustomerService) {      //已进入人工客服
                    mListener.disconnectCustomer();
                } else {                                          //未进入人工客服
                    if (DialogStatus == Constants.CONNECTING) {
                        //未接通挂断
                        mListener.disconnectCustomer();
                        CloseTimeOut();
                        dismiss();
                    } else if (DialogStatus == Constants.CONNECT_FAILED || DialogStatus == Constants.CONNECT_INTERRUPT) {
                        //连接失败关闭
                        dismiss();
                    }
                }

                break;

            case R.id.iv_close: //隐藏
                mListener.hideDialog();
                break;
        }
    }

    public void setListener(OnCustomerDialogClickListener listener) {
        mListener = listener;
    }

    public void setConnnectStatus(int status) {
        DialogStatus = status;
    }

    public void setTip(String strTip) {
        mTip.setText(strTip);
    }

    public void setBtnEnable(boolean enable) {  //TODO 隐藏后下次打开有用
        mConnectBtn.setEnabled(enable);
    }

    public void reqConnect() {
        DialogStatus = Constants.CONNECTING;
        changeDialogStatus();
        mListener.connectCustomer();
        startTimeOut(45, false);
        //TODO 正在连接客服，请等待”，每10秒播放一次
        mListener.speakTip(mContext.getString(R.string.call_customer));
    }


    //mConnectBtn.setBackground(mContext.getResources().getDrawable(R.drawable.hang_up_btn_disabled));
    //弹窗状态
    public void changeDialogStatus() {

        switch (DialogStatus) {

            case Constants.CONNECTING:    //连接中

                setTip(mContext.getString(R.string.connecting));
                mConnectBtn.setText(mContext.getString(R.string.hang_up));
                if (ivClose.isShown()) {
                    ivClose.setVisibility(View.GONE);
                }
                if (!mConnectBtn.isEnabled()) {
                    mConnectBtn.setEnabled(true);
                }
                break;

            case Constants.CONNECT_FAILED:   //连接失败
                setTip(mContext.getString(R.string.connection_failed));
                mConnectBtn.setText(mContext.getString(R.string.dialog_close));
                if (!mConnectBtn.isEnabled()) {
                    mConnectBtn.setEnabled(true);
                }
                break;

            case Constants.CONNECT_INTERRUPT:   //通信中断

                setTip(mContext.getString(R.string.the_connection_is_broken));
                mConnectBtn.setText(mContext.getString(R.string.dialog_close));
                if (!mConnectBtn.isEnabled()) {
                    mConnectBtn.setEnabled(true);
                }
                if (ivClose.isShown()) {
                    ivClose.setVisibility(View.GONE);
                }
                break;

            case Constants.ISCONNECTED:    //连接成功
                setTip(mContext.getString(R.string.connected));
                mConnectBtn.setText(mContext.getString(R.string.hang_up));
                if (mConnectBtn.isEnabled()) {
                    mConnectBtn.setEnabled(false);
                }
                //2秒后隐藏
                delayHide(false);
                break;

            case Constants.ALREADY_CONNECTED:  //已连接状态
                setTip(mContext.getString(R.string.connected));
                mConnectBtn.setText(mContext.getString(R.string.hang_up));
                if (!mConnectBtn.isEnabled()) {
                    mConnectBtn.setEnabled(true);
                }
                ivClose.setVisibility(View.VISIBLE);
//                DialogStatus = Constants.ISCONNECTED;
                break;

            case Constants.SERVER_DISCONNCTED:    //主动断开连接
            case Constants.DISCONNCTED:
                setTip(mContext.getString(R.string.disconnecting));
                if (ivClose.isShown()) {
                    ivClose.setVisibility(View.GONE);
                }
                if (mConnectBtn.isEnabled()) {
                    mConnectBtn.setEnabled(false);
                }
                //2秒后关闭
                delayHide(true);
                break;


        }
    }


    public interface OnCustomerDialogClickListener {

        void connectCustomer();   //连接人工客服

        void disconnectCustomer(); //断开人工客服

        void hideDialog(); //隐藏人工客户

        void speakTip(String content);
    }


    public void startTimeOut(int sec, final boolean isHeatBeat) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isHeatBeat) {  // time out
                    //TODO 语音播报  “抱歉，未能给您联系到客服”
                    mListener.speakTip(mContext.getString(R.string.can_not_contact_tip));
                    delayHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setConnnectStatus(Constants.CONNECT_FAILED);
                            changeDialogStatus();
                            delayHide(true);
                        }
                    });
                } else {   //heatBeat lost
                    EventBus.getDefault().post(new FloatWindowCustomerEvent(false));
                    delayHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!isShowing()) {
                                show();
                            }
                            mListener.speakTip(mContext.getString(R.string.connect_failed_tip));
                            Constants.sIsCustomerService = false;
                            setConnnectStatus(Constants.CONNECT_INTERRUPT);
                            changeDialogStatus();
                        }
                    });
                }

            }
        }, sec * 1000L);
    }


    public void CloseTimeOut() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }


    //连接成功后/断开连接，2s隐藏dialog
    private void delayHide(final boolean isHungUp) {
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isHungUp) { // 挂断效果
                    dismiss();
                    if (Constants.isNeedComment) {
                        Constants.isNeedComment = false;
                        BRouter.getInstance()
                                .build(BRouterPath.NAVI_GUIDE_COMMENT)
                                .withBoolean("isFromNaviAI", true)
                                .navigation();
                    }
                } else {   // 连接成功效果
                    ivClose.setVisibility(View.VISIBLE);
                    EventBus.getDefault().post(new FloatWindowCustomerEvent(true));
                    dismiss();
                }

            }
        }, 2000L);
    }


}
