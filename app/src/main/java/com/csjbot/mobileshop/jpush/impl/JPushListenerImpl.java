package com.csjbot.mobileshop.jpush.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.csjbot.mobileshop.jpush.listener.JPushListener;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 孙秀艳 on 2018/12/18.
 */

public class JPushListenerImpl implements JPushListener {

    private static final String TAG = "JPushListenerImpl";
    private Context mContext = null;

    /**
     * 初始化JPush
     */
    private JPushListenerImpl() {
    }

    public static JPushListenerImpl newInstance() {
        return new JPushListenerImpl();
    }

    /**
     * 初始化
     *
     * @param context 需要传入getApplicationContext();
     * @param debug   开启debug
     */
    @Override
    public void init(Context context, boolean debug) {
        this.mContext = context;
        JPushInterface.setDebugMode(debug);
        JPushInterface.init(mContext);
    }

    @Override
    public void stopPush() {
        if (mContext != null) {
            JPushInterface.stopPush(mContext);
        } else {
            Log.e(TAG, "JPushInterface.stopPush(mContext),失败");
        }
    }

    @Override
    public void resumePush() {
        if (mContext != null) {
            JPushInterface.resumePush(mContext);
        } else {
            Log.e(TAG, "JPushInterface.resumePush(mContext),失败");
        }
    }

    @Override
    public boolean isPushStopped() {
        return mContext != null && JPushInterface.isPushStopped(mContext);
    }

    @Override
    public String getRegistrationId() {
        if (mContext != null) {
            if (!TextUtils.isEmpty(JPushInterface.getRegistrationID(mContext))) {
                return JPushInterface.getRegistrationID(mContext);
            } else {
                return "";
            }
        }
        return "";
    }

    @Override
    public void setTags(int sequence, Set<String> set) {
        if (mContext != null) {
            JPushInterface.setTags(mContext, sequence, set);
        } else {
            Log.e(TAG, "JPushInterface.setTags(mContext,sequence,set),失败");
        }
    }

    @Override
    public void getAllTags(int sequence) {
        if (mContext != null) {
            JPushInterface.getAllTags(mContext, sequence);
        } else {
            Log.e(TAG, "JPushInterface.getAllTags(mContext,sequence),失败");
        }
    }

    @Override
    public void setAlias(int sequence, String alias) {
        if (mContext != null) {
            Log.d(TAG, "JPushInterface.setAlias(mContext,sequence,alias) --> alias: " + alias);
            JPushInterface.setAlias(mContext, sequence, alias);
        } else {
            Log.e(TAG, "JPushInterface.setAlias(mContext,sequence,alias),失败");
        }
    }

    @Override
    public void getAlias(int sequence) {
        if (mContext != null) {
            JPushInterface.getAlias(mContext, sequence);
        } else {
            Log.e(TAG, "JPushInterface.getAlias(mContext,sequence),失败");
        }
    }
}

