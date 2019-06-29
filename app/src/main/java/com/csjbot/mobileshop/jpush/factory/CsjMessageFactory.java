package com.csjbot.mobileshop.jpush.factory;

import android.content.Context;

import com.csjbot.mobileshop.jpush.impl.JPushListenerImpl;
import com.csjbot.mobileshop.jpush.listener.JPushListener;


/**
 * @author Ben
 * @date 2018/2/8
 */

public class CsjMessageFactory {

    public static JPushListener newInstance() {
        return initJPush();
    }

    private static JPushListener initJPush() {
        return JPushListenerImpl.newInstance();
    }
}
