package com.csjbot.mobileshop.feature.payment.contract;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

/**
 * @author ShenBen
 * @date 2019/1/22 10:52
 * @email 714081644@qq.com
 */
public interface PaymentGuideContract {
    interface View {
        void setPresenter(Presenter presenter);

        RecyclerView getRecycler();

        LinearLayout getDots();

        void isNoData(boolean isNoData, String msg);

        void showSpeakWord(String msg);
    }

    interface Presenter {
        /**
         * 是否要翻开指定二维码
         *
         * @param key
         */
        void init(@Nullable String key);

        boolean speakToIntent(String msg);

    }
}
