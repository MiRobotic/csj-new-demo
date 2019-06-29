package com.csjbot.mobileshop.feature.nearbyservice;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.mapapi.map.TextureMapView;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.feature.nearbyservice.contract.PoiSearchContract;
import com.csjbot.mobileshop.feature.nearbyservice.contract.PoiSearchPresenterImpl;
import com.csjbot.mobileshop.router.BRouterPath;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * poi搜索，进入时直接进行周边搜索
 */
@Route(path = BRouterPath.NEAR_BY_SEARCH)
public class PoiSearchActivity extends BaseModuleActivity implements PoiSearchContract.View {
    public static final String KEYWORD = "keyword";
    @BindView(R.id.mapView)
    TextureMapView mMapView;
    @BindView(R.id.rv_address_list)
    RecyclerView rvAddressList;
    private PoiSearchContract.Presenter mPresenter;

    @OnClick({R.id.ib_reset})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.ib_reset:
                mPresenter.reset();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_poi_search, R.layout.activity_poi_search_vertical);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected boolean isDisableEntertainment() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        getTitleView().setSettingLayoutGone();
        new PoiSearchPresenterImpl(this, this);
        mPresenter.init();
        Intent intent = getIntent();
        if (intent != null) {
            mPresenter.searchPlace(intent.getStringExtra(KEYWORD));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, true));
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        mMapView.onDestroy();
    }

    @Override
    public void setPresenter(PoiSearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public TextureMapView getMapView() {
        return mMapView;
    }

    @Override
    public RecyclerView getRecycler() {
        return rvAddressList;
    }
}
