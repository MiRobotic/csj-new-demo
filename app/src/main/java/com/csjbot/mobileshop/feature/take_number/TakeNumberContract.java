package com.csjbot.mobileshop.feature.take_number;

import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.BaseView;

/**
 * Created by jingwc on 2017/9/26.
 */

public class TakeNumberContract {
    interface Presenter extends BasePresenter<View> {
        void takeNumber();
    }

    interface View extends BaseView {
    }
}
