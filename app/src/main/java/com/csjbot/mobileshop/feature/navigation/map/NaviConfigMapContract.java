package com.csjbot.mobileshop.feature.navigation.map;

import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.BaseView;

/**
 * Created by jingwc on 2017/9/23.
 */

public class NaviConfigMapContract {
    interface Presenter extends BasePresenter<View> {
    }

    interface View extends BaseView {
        void showNaviMap();
    }
}
