package com.csjbot.mobileshop.feature.settings.checked_update;

/**
 * @author ShenBen
 * @date 2018/12/25 16:35
 * @email 714081644@qq.com
 */

public interface SettingUpdateAppContract {

    interface View {
        void setPresenter(Presenter presenter);

        void setLinuxVersion(String version);
    }

    interface Presenter {

        void initData();

        void updateLinux();

        void updateAndroid();

        void onDestroy();
    }
}
