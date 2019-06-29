package com.csjbot.mobileshop.guide_patrol.widget;

/**
 * Created by 孙秀艳 on 2019/1/18.
 * 导航状态 空闲 单点带路 一键导航 暂停导航 到点 返回迎宾点 取消
 */

public enum NaviTaskStatus {
    AWAIT,
    START,
    CIRCLE,
    SINGLECIRCLE,
    PAUSE,
    CONTINUE,
    STOP,
    ARRIVE,
    BACK,
    GOWAIT,
    BLOCK,
    UNBLOCK
}
