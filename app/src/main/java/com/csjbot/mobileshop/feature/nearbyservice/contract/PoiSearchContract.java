package com.csjbot.mobileshop.feature.nearbyservice.contract;

import android.support.v7.widget.RecyclerView;

import com.baidu.mapapi.map.TextureMapView;

/**
 * @author ShenBen
 * @date 2019/1/23 17:19
 * @email 714081644@qq.com
 */
public interface PoiSearchContract {
    interface View {

        void setPresenter(Presenter presenter);

        TextureMapView getMapView();

        RecyclerView getRecycler();

    }

    interface Presenter {

        void init();

        void searchPlace(String key);

        void reset();

        void destroy();

    }
}
