package com.csjbot.mobileshop.model.http.consult;

import com.csjbot.mobileshop.model.http.base.BaseImpl;

/**
 * Created by jingwc on 2017/9/19.
 */

public class ConsultImpl extends BaseImpl implements IConsult {

    @Override
    public ConsultService getRetrofit() {
        return getRetrofit(ConsultService.class);
    }
}
