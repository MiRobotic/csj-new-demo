package com.csjbot.mobileshop.feature.dance;

import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.BaseView;

/**
 * Created by jingwc on 2017/10/12.
 */

public class DanceContract {

    interface Presenter extends BasePresenter<DanceContract.View> {
        void startDance(long time,String musicPath);
        void stopDance();
    }

    interface View extends BaseView {

    }
}
