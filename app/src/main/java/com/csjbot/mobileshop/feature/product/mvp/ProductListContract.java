package com.csjbot.mobileshop.feature.product.mvp;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.csjbot.mobileshop.model.http.bean.rsp.GoodsDetailBean;

import java.util.ArrayList;

/**
 * @author ShenBen
 * @date 2018/11/12 11:09
 * @email 714081644@qq.com
 */

public interface ProductListContract {

    interface View {
        void setPresenter(Presenter presenter);

        RecyclerView getRvCloth();

        TextView getSelectCloth();

        void isNoData(boolean isNodata);

        /**
         * 上一页、下一页隐藏
         */
        void setIvGone();
    }

    interface Presenter {
        /**
         * 初始化操作
         */
        void init();

        /**
         * 加载数据
         *
         * @param list 数据
         */
        void loadData(ArrayList<GoodsDetailBean> list);

        /**
         * 上一页
         */
        void previousPage();

        /**
         * 下一页
         */
        void nextPage();

        /**
         * 显示筛选popup
         */
        void showSelectPopup();
    }

}
