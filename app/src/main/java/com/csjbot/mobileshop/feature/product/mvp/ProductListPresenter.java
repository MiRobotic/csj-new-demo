package com.csjbot.mobileshop.feature.product.mvp;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.product.ProductDetailsActivity;
import com.csjbot.mobileshop.feature.product.adapter.ProductListAdapter;
import com.csjbot.mobileshop.feature.product.bean.SelectClothBean;
import com.csjbot.mobileshop.feature.product.popup.SelectClothPopup;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.bean.rsp.GoodsDetailBean;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridLayoutManager;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridSnapHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShenBen
 * @date 2018/11/12 11:13
 * @email 714081644@qq.com
 */

public class ProductListPresenter implements ProductListContract.Presenter, PagerGridLayoutManager.PageListener {
    private Activity mActivity;
    private ProductListContract.View mView;
    private ProductListAdapter mAdapter;
    private PagerGridLayoutManager mManager;
    private List<GoodsDetailBean> mList;
    private SelectClothPopup mPopup;

    private List<SelectClothBean> mColorList;
    private List<SelectClothBean> mSeasonList;

    private String mGoodsType;
    private String mUid;

    public ProductListPresenter(Activity mActivity, ProductListContract.View mView) {
        this.mActivity = mActivity;
        this.mView = mView;
        mView.setPresenter(this);

    }

    @Override
    public void init() {
        mList = new ArrayList<>();
        mColorList = new ArrayList<>();
        mSeasonList = new ArrayList<>();

        mSeasonList.add(new SelectClothBean("春"));
        mSeasonList.add(new SelectClothBean("夏"));
        mSeasonList.add(new SelectClothBean("秋"));
        mSeasonList.add(new SelectClothBean("冬"));

        //可以根据大小屏动态设置不同的布局
        if (Constants.isPlus()) {
            mAdapter = new ProductListAdapter(R.layout.item_product_list_vertical, mList);
        } else {
            mAdapter = new ProductListAdapter(R.layout.item_product_list, mList);
        }

        RecyclerView mRvCloth = mView.getRvCloth();
        mManager = new PagerGridLayoutManager(2, 3, PagerGridLayoutManager.HORIZONTAL);
        mManager.setPageListener(this);
        mRvCloth.setLayoutManager(mManager);
        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(mRvCloth);
        mRvCloth.setHasFixedSize(true);
        mRvCloth.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) ->
                ARouter.getInstance()
                        .build(BRouterPath.PRODUCT_CLOTHING_DETAIL)
                        .withParcelable(ProductDetailsActivity.PRODUCT_DETAIL, mList.get(position))
                        .navigation());
    }


    @Override
    public void loadData(ArrayList<GoodsDetailBean> list) {
        if (list.isEmpty()) {
            mView.isNoData(true);
            return;
        }
        mView.isNoData(false);
        mList.addAll(list);
        mAdapter.setNewData(mList);
    }

    @Override
    public void previousPage() {
        if (mManager != null) {
            mManager.smoothPrePage();
        }
    }

    @Override
    public void nextPage() {
        if (mManager != null) {
            mManager.smoothNextPage();
        }
    }

    @Override
    public void showSelectPopup() {
//        if (mPopup == null) {
//            mPopup = new SelectClothPopup(mActivity);
//            mPopup.setOnSelectClothDetailListener(this::loadData);
////            mPopup.setColorList(mColorList);
//            mPopup.setSeasonList(mSeasonList);
//        }
//
//        if (!mPopup.isShowing()) {
//            mPopup.showPopupWindow(mView.getSelectCloth());
//        }
    }

    @Override
    public void onPageSizeChanged(int pageSize) {

    }

    @Override
    public void onPageSelect(int pageIndex) {

    }
}
