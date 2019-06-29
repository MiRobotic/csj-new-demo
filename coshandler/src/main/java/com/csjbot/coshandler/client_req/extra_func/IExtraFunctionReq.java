package com.csjbot.coshandler.client_req.extra_func;

import java.util.List;

/**
 * Copyright (c) 2018, SuZhou CsjBot. All Rights Reserved.
 * www.example.com
 * <p>
 * Created by 浦耀宗 at 2018/05/23 0023-15:22.
 * Email: puyz@example.com
 */

public interface IExtraFunctionReq {
    void getHotWords();

    void setHotWords(List<String> hotwords);


    void startFaceFollow();

    void stopFaceFollow();

    /**
     * 恢复出厂设置
     */
    void resetRobot();
}
