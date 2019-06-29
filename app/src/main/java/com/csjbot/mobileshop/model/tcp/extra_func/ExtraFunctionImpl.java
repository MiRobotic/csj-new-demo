package com.csjbot.mobileshop.model.tcp.extra_func;

import com.csjbot.mobileshop.model.tcp.base.BaseImpl;
import com.csjbot.coshandler.core.interfaces.IExtraFunction;

import java.util.List;

/**
 * Copyright (c) 2018, SuZhou CsjBot. All Rights Reserved.
 * www.example.com
 * <p>
 * Created by 浦耀宗 at 2018/05/23 0023-15:15.
 * Email: puyz@example.com
 */

public class ExtraFunctionImpl extends BaseImpl implements IExtraFunction {
    @Override
    public void getHotWords() {
        robotManager.robot.reqProxy.getHotWords();
    }

    @Override
    public void setHotWords(List<String> hotwords) {
        robotManager.robot.reqProxy.setHotWords(hotwords);
    }

    /**
     * 开始人脸跟随
     */
    @Override
    public void startFaceFollow() {
        robotManager.robot.reqProxy.startFaceFollow();
    }

    /**
     * 关闭人脸跟随
     */
    @Override
    public void stopFaceFollow() {
        robotManager.robot.reqProxy.stopFaceFollow();
    }

    @Override
    public void sendMessageToLinux(String message, String language) {
//        robotManager.robot.reqProxy.sendMessageToLinux();

    }
}
