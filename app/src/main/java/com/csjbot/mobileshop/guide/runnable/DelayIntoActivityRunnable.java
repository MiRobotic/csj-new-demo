package com.csjbot.mobileshop.guide.runnable;

import com.csjbot.mobileshop.guide.splash_thread_model.base.BaseRunnable;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/12/19.
 * 等待的时间
 */

public class DelayIntoActivityRunnable extends BaseRunnable {
    @Override
    public void getRun() {
        sendMsg(this.getClass().getSimpleName());
    }
}
