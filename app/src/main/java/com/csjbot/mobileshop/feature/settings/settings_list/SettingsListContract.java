package com.csjbot.mobileshop.feature.settings.settings_list;

import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.BaseView;

/**
 * Created by jingwc on 2017/10/20.
 */

public interface SettingsListContract {

    interface Presenter extends BasePresenter<SettingsListContract.View> {
        public void saveMap();
        public void restoreMap();
    }

    interface View extends BaseView {
        public void saveMapResult(boolean isSuccess);
        public void restoreMapResult(boolean isSuccess);
    }
}
