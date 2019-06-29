package com.csjbot.mobileshop.feature.media.contract;

import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

/**
 * @author ShenBen
 * @date 2019/1/21 16:30
 * @email 714081644@qq.com
 */
public interface MediaPublicizeContract {
    interface View {
        void setPresenter(Presenter presenter);

        RecyclerView getRecycler();

        LinearLayout getDots();

        void isNoData(boolean isNoData);
    }

    interface Presenter {
        void init();
    }
}
