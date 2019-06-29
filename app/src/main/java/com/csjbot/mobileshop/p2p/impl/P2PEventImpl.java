package com.csjbot.mobileshop.p2p.impl;

import android.text.TextUtils;

import com.csjbot.coshandler.log.CsjlogProxy;
import com.peergine.android.livemulti.pgLibLiveMultiCapture;

/**
 * @author ShenBen
 * @date 2019/1/29 11:09
 * @email 714081644@qq.com
 */
public class P2PEventImpl implements pgLibLiveMultiCapture.OnEventListener {
    private static final String TAG = P2PEventImpl.class.getSimpleName() + "--->";

    private static final String LOGIN = "Login";//登入
    private static final String LOGOUT = "Logout";//登出
    private static final String KICKOUT = "KickOut";//剔出

    @Override
    public void event(String action, String data, String renderId) {
        //处理事件的代码
        CsjlogProxy.getInstance().warn(TAG + "action : " + action + " ,data : " + data + " ,renderId: " + renderId);
        if (TextUtils.isEmpty(action)) {
            return;
        }
        switch (action) {
            case LOGIN:

                break;
            case LOGOUT:

                break;
            case KICKOUT:

                break;
        }
    }
}
