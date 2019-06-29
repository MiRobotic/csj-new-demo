package com.csjbot.mobileshop.feature.consult;

import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.BaseView;

/**
 * Created by jingwc on 2017/9/19.
 */

public class ConsultContract {

    interface Presenter extends BasePresenter<ConsultContract.View> {
        void getConsult();
    }

    interface View extends BaseView {
        void showConsult();
    }
}
