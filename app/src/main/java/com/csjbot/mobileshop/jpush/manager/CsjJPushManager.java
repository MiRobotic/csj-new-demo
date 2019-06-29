package com.csjbot.mobileshop.jpush.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.jpush.diaptch.CsjPushDispatch;
import com.csjbot.mobileshop.jpush.factory.CsjMessageFactory;
import com.csjbot.mobileshop.jpush.listener.JPushListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;

/**
 * Created by 孙秀艳 on 2018/12/18.
 */

public class CsjJPushManager {

    private static final String TAG = "CsjJPushManager";
    private Context mContext;
    private JPushListener mListener;
    private List<String> mSendReqFailedList;
    private final Object lock = new Object();

    private CsjJPushManager() {
        mListener = CsjMessageFactory.newInstance();
        mSendReqFailedList = new ArrayList<>();
    }

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final CsjJPushManager mManager = new CsjJPushManager();
    }

    /**
     * 初始化CsjMessageManager
     *
     * @return
     */
    public static CsjJPushManager getInstance() {
        return Holder.mManager;
    }

    /**
     * 初始化JPush，建议在Application中进行初始化
     * 可以在需要调用的地方初始化
     */
    public void init(Context context) {
        init(context, true);
    }

    public void init(Context context, boolean debug) {
        mContext = context;
        mListener.init(context, debug);
    }

    /**
     * 停止接收推送服务
     */
    public void stopPush() {
        mListener.stopPush();
    }

    /**
     * 恢复接收推送服务
     * 重新恢复接收推送服务，调用init()方法无效，需调用resumePush()
     */
    public void resumePush() {
        mListener.resumePush();
    }

    /**
     * 推送服务的状态
     *
     * @return 是否停止，是，返回true，否，返回false
     */
    public boolean isPushStopped() {
        return mListener.isPushStopped();
    }

    /**
     * 获取getRegistrationId
     *
     * @return
     */
    public String getRegistrationId() {
        return mListener.getRegistrationId();
    }

    /**
     * 设置Tags,如果存在Tag,会把以前的Tag覆盖掉
     *
     * @param sequence
     * @param set
     */
    public void setTags(int sequence, Set<String> set) {
        mListener.setTags(sequence, set);
    }

    /**
     * 获取所有Tags
     *
     * @param sequence
     */
    public void getAllTags(int sequence) {
        mListener.getAllTags(sequence);
    }

    /**
     * 设置Alias，如果存在Alias,会把以前的Alias覆盖掉
     *
     * @param sequence
     * @param alias
     */
    public void setAlias(int sequence, String alias) {
        mListener.setAlias(sequence, alias);
    }

    public void setAliasSN() {
        String sn = Robot.SN;
        setAlias((int) System.currentTimeMillis(), sn);
    }


    /**
     * 获取Alias
     *
     * @param sequence
     */
    public void getAlias(int sequence) {
        mListener.getAlias(sequence);
    }

    /**
     * 添加发送失败的命令
     *
     * @param json
     */
    public void addSendFailedReq(String json) {
        synchronized (lock) {
            if (TextUtils.isEmpty(json)) {
                return;
            }
            if (!mSendReqFailedList.contains(json)) {
                CsjlogProxy.getInstance().error("JPush-> 添加发送失败命令: " + json);
                mSendReqFailedList.add(json);
            }
        }
    }

    /**
     * 重新发送
     */
    public void sendFailedReqAgain() {
        synchronized (lock) {
            if (mSendReqFailedList.isEmpty()) {
                return;
            }
            CsjlogProxy.getInstance().error("JPush-> 重发失败命令");
            for (String req : mSendReqFailedList) {
                CsjPushDispatch.getInstance().execute(req.trim());
            }
            mSendReqFailedList.clear();
        }
    }

    /**
     * tag 增删查改的操作会在此方法中回调结果。
     *
     * @param message 回调结果
     */
    public void onTagOperatorResult(JPushMessage message) {
        if (message == null) {
            return;
        }
        CsjlogProxy.getInstance().info("JPush-> onTagOperatorResult");
    }

    /**
     * 查询某个 tag 与当前用户的绑定状态的操作会在此方法中回调结果。
     *
     * @param message
     */
    public void onCheckTagOperatorResult(JPushMessage message) {
        if (message == null) {
            return;
        }
        CsjlogProxy.getInstance().info("JPush-> onCheckTagOperatorResult");
    }

    /**
     * alias 相关的操作会在此方法中回调结果。
     *
     * @param message
     */
    public void onAliasOperatorResult(JPushMessage message) {
        if (message == null) {
            return;
        }
        if (message.getErrorCode() != 0) {
            new Handler().postDelayed(() -> setAlias((int) System.currentTimeMillis(), Robot.SN), 5000);
        }
        CsjlogProxy.getInstance().info("JPush-> onAliasOperatorResult,ErrorCode: " + message.getErrorCode());
    }

    /**
     * 设置手机号码会在此方法中回调结果。
     *
     * @param message
     */
    public void onMobileNumberOperatorResult(JPushMessage message) {
        if (message == null) {
            return;
        }
        CsjlogProxy.getInstance().info("JPush-> onMobileNumberOperatorResult");
    }

    /**
     * 获取JPush 返回的数据
     *
     * @param action
     * @param bundle
     */
    public synchronized void getJPushCallBack(String action, Bundle bundle) {
        if (TextUtils.isEmpty(action) || bundle == null) {
            return;
        }
        switch (action) {
            case JPushInterface.ACTION_CONNECTION_CHANGE://网络连接状态
                CsjlogProxy.getInstance().info("JPush->网络连接状态: " + bundle.getBoolean(JPushInterface.EXTRA_CONNECTION_CHANGE));
                break;
            case JPushInterface.ACTION_REGISTRATION_ID://用户注册成功
                CsjlogProxy.getInstance().info("JPush->推送注册成功: " + bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID));
                break;
            case JPushInterface.ACTION_MESSAGE_RECEIVED://接收到推送下来的信息
                String title = bundle.getString(JPushInterface.EXTRA_TITLE);
                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                if (!TextUtils.isEmpty(message)) {
                    CsjlogProxy.getInstance().info("JPush->接收到推送下来的信息: " + message);
                    CsjPushDispatch.getInstance().execute(message.trim());
                }
                break;
            case JPushInterface.ACTION_NOTIFICATION_RECEIVED://接收到推送下来的通知
                CsjlogProxy.getInstance().info("JPush->接收到推送下来的通知: ");
                break;
            case JPushInterface.ACTION_NOTIFICATION_OPENED://用户点击了通知
                CsjlogProxy.getInstance().info("JPush->用户点击了通知: ");
                break;
        }
    }
}

