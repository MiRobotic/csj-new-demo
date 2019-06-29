package com.csjbot.mobileshop.jpush.listener;

import android.content.Context;

import java.util.Set;

/**
 * Created by 孙秀艳 on 2018/12/18.
 */

public interface JPushListener {

    void init(Context context, boolean debug);

    void stopPush();

    void resumePush();

    boolean isPushStopped();

    String getRegistrationId();

    void setTags(int sequence, Set<String> set);

    void getAllTags(int sequence);

    void setAlias(int sequence, String alias);

    void getAlias(int sequence);

}

