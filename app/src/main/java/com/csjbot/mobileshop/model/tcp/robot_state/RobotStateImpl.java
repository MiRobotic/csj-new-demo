package com.csjbot.mobileshop.model.tcp.robot_state;

import com.csjbot.mobileshop.model.tcp.base.BaseImpl;

/**
 * Created by jingwc on 2017/10/25.
 */

public class RobotStateImpl extends BaseImpl implements IRobotState {
    @Override
    public void shutdown() {
        robotManager.robot.reqProxy.shutdown();
    }

    @Override
    public void reboot() {
        robotManager.robot.reqProxy.reboot();
    }

    @Override
    public void getBattery() {
        robotManager.robot.reqProxy.getBattery();
    }

    @Override
    public void checkSelf() {
        robotManager.robot.reqProxy.checkSelf();
    }

    /**
     * 获取版本
     */
    @Override
    public void getRobotHWVersion() {
        robotManager.robot.reqProxy.getRobotHWVersion();
    }

    /**
     * 获取Linux储存的机器人硬件类型，默认是迎宾
     */
    @Override
    public void getLinuxRobotType() {
        robotManager.robot.reqProxy.getLinuxRobotType();
    }

    /**
     * 设置Linux储存的机器人硬件类型，默认是迎宾
     *
     * @param type
     */
    @Override
    public void setLinuxRobotType(String type) {
        robotManager.robot.reqProxy.setLinuxRobotType(type);
    }

    @Override
    public void setTimezone(String timeOffset, String region) {
        robotManager.robot.reqProxy.setTimezone(timeOffset, region);
    }

    @Override
    public void makeSessionId() {
        robotManager.robot.reqProxy.makeSessionId();
    }

    @Override
    public void getPerson() {

    }

    @Override
    public void setLinuxServerAddr(String addr, String port) {
        robotManager.robot.reqProxy.setLinuxServerAddr(addr, port);
    }
}
