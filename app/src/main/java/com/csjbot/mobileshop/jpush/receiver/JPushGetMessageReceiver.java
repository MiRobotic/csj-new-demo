package com.csjbot.mobileshop.jpush.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.csjbot.mobileshop.jpush.manager.CsjJPushManager;

/**
 * Created by 孙秀艳 on 2018/12/18.
 */

public class JPushGetMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        CsjJPushManager.getInstance().getJPushCallBack(action, bundle);
    }
}
