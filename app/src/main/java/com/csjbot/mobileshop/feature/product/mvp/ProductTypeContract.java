package com.csjbot.mobileshop.feature.product.mvp;

import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.csjbot.mobileshop.feature.product.bean.ProductTypeBean;

import java.util.List;

/**
 * @author ShenBen
 * @date 2018/11/13 9:50
 * @email 714081644@qq.com
 */

public interface ProductTypeContract {
    interface View {
        void setPresenter(Presenter presenter);

        RecyclerView getRvCloth();

        LinearLayout llDots();

        void speakMessage(String message);

        void isNoData(boolean isNodata);
    }

    interface Presenter {

        void initData();

        void previousPage();

        void nextPage();

        List<ProductTypeBean> getProductTypes();

        boolean isSpeakType(String message);
    }
}
