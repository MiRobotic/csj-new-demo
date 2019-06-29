package com.csjbot.mobileshop.guide_patrol.patrol.core.robotStateMachine;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public interface INaviCircleState {
    void robotStart(int curindex);//通知UI层，机器人开始一键导览
    void robotArrive(int curindex);//通知UI层，机器人到达第几个目标导航点
    void robotBack();//通知UI层，机器人正在返回迎宾点 到点返回
    void robotError(String msg);//通知UI层，导航出错
    void robotPause();//通知UI层，导航任务暂停
    void robotContinue();//通知UI层，导航任务继续
    void robotStop();//通知UI层，导航任务停止
    void robotGoWaiting();//通知UI层，机器人返回迎宾点  点击界面上的返回迎宾点
    void robotCircusee();//通知UI层，机器人围观
}