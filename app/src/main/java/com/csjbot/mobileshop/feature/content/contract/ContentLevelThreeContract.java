package com.csjbot.mobileshop.feature.content.contract;

import android.support.v7.widget.RecyclerView;

/**
 * @author ShenBen
 * @date 2018/10/19 16:43
 * @email 714081644@qq.com
 */
public interface ContentLevelThreeContract {

    interface View {
        void setPresenter(Presenter presenter);

        RecyclerView getRvTitle();

        void loadUrl(String word, String data);

    }

    interface Presenter {

        void loadData(String data);

        boolean voiceControl(String text);
    }
}
