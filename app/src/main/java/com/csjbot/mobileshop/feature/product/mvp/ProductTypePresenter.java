package com.csjbot.mobileshop.feature.product.mvp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.product.ProductListActivity;
import com.csjbot.mobileshop.feature.product.adapter.ProductTypeAdapter;
import com.csjbot.mobileshop.feature.product.bean.ProductTypeBean;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.bean.rsp.GoodsDetailBean;
import com.csjbot.mobileshop.model.http.bean.rsp.GoodsTypeBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridLayoutManager;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridSnapHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import skin.support.widget.SkinCompatImageView;

/**
 * @author ShenBen
 * @date 2018/11/13 9:51
 * @email 714081644@qq.com
 */

public class ProductTypePresenter implements ProductTypeContract.Presenter {
    private Context mContext;
    private ProductTypeContract.View mView;
    private PagerGridLayoutManager mManager;
    private ProductTypeAdapter mAdapter;
    private List<ProductTypeBean> mList;


    public ProductTypePresenter(Context context, ProductTypeContract.View mView) {
        mContext = context;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        RecyclerView mRvCloth = mView.getRvCloth();

        //可以根据大小屏动态设置不同的布局
        if (Constants.isPlus()) {
            mAdapter = new ProductTypeAdapter(R.layout.item_cloth_type_vertical, mList);
        } else {
            mAdapter = new ProductTypeAdapter(R.layout.item_cloth_type, mList);
        }

        mManager = new PagerGridLayoutManager(2, 3, PagerGridLayoutManager.HORIZONTAL);
        mRvCloth.setLayoutManager(mManager);
        mManager.setPageListener(new PagerGridLayoutManager.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {

            }

            @Override
            public void onPageSelect(int pageIndex) {
                SkinCompatImageView imageView;
                for (int i = 0; i < mView.llDots().getChildCount(); i++) {
                    imageView = (SkinCompatImageView) mView.llDots().getChildAt(i);
                    if (pageIndex == i) {
                        imageView.setImageResource(R.drawable.iv_point_selected);
                    } else {
                        imageView.setImageResource(R.drawable.iv_point_unselected);
                    }
                }
            }
        });
        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(mRvCloth);
        mRvCloth.setHasFixedSize(true);
        mRvCloth.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            //跳转至商品列表界面
            ProductTypeBean bean = mList.get(position);
            ArrayList<GoodsDetailBean> list = Constants.Product.sProductDetails.get(bean.getId());
            if (list == null) {
                list = new ArrayList<>();
            }
            ARouter.getInstance()
                    .build(BRouterPath.PRODUCT_CLOTHING_LIST)
                    .withParcelableArrayList(ProductListActivity.PRODUCT_DATA, list)
                    .navigation();
        });

        //现获取商品种类，在获取全部商品，再归类处理
        ServerFactory.createApi().getGoodsType(Robot.SN, "", new Observer<ResponseBean<List<GoodsTypeBean>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ResponseBean<List<GoodsTypeBean>> listResponseBean) {
                if (listResponseBean != null && listResponseBean.getCode() == 200
                        && listResponseBean.getRows() != null && !listResponseBean.getRows().isEmpty()) {
                    Constants.Product.sProductTypeLists.clear();
                    Constants.Product.sProductTypeLists.addAll(listResponseBean.getRows());//缓存种类

                    Collections.sort(listResponseBean.getRows());
                    ProductTypeBean typeBean;
                    for (GoodsTypeBean bean : listResponseBean.getRows()) {
                        typeBean = new ProductTypeBean();
                        typeBean.setId(bean.getId());
                        typeBean.setGoodsName(bean.getName());
                        mList.add(typeBean);
                    }
                    getGoodsDetail();
                } else {
                    mView.isNoData(true);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mView.isNoData(true);
                CsjlogProxy.getInstance().error("获取服装种类列表失败: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

    }

    private void getGoodsDetail() {
        ServerFactory.createApi().getGoodsDetail(Robot.SN, "{}", new Observer<ResponseBean<List<GoodsDetailBean>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ResponseBean<List<GoodsDetailBean>> listResponseBean) {
                if (listResponseBean != null && listResponseBean.getCode() == 200) {
                    loadData(listResponseBean.getRows());
                } else {
                    loadData(null);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                loadData(null);
                CsjlogProxy.getInstance().error("获取服装种类列表失败: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void loadData(List<GoodsDetailBean> rows) {
        Constants.Product.sProductDetails.clear();
        if (rows != null && !rows.isEmpty()) {
            ArrayList<GoodsDetailBean> list;
            //遍历所有商品数据，因为获取商品的获取种类的接口无法拿到商品的图片，所以要自己判断，取种类下第一个商品的图片
            //归类，某个种类下的所有商品
            for (GoodsDetailBean detailBean : rows) {
                list = Constants.Product.sProductDetails.get(detailBean.getTypeId());
                if (list == null) {
                    list = new ArrayList<>();
                    Constants.Product.sProductDetails.put(detailBean.getTypeId(), list);
                }
                list.add(detailBean);
            }
            List<GoodsDetailBean> beanList;
            GoodsDetailBean detailBean;
            for (ProductTypeBean typeBean : mList) {
                beanList = Constants.Product.sProductDetails.get(typeBean.getId());
                if (beanList != null && !beanList.isEmpty()) {
                    detailBean = beanList.get(0);//第一个商品作为商品类型
                    typeBean.setImageUrl(detailBean.getCoverUrl());
                    typeBean.setOriginalPrice(detailBean.getCostPrice());
                    typeBean.setPresentPrice(detailBean.getNowPrice());
                }
            }
        }
        if (!mList.isEmpty()) {
            mAdapter.setNewData(mList);
            updateDots();
            mView.isNoData(false);
        }
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
    public List<ProductTypeBean> getProductTypes() {
        return mList;
    }

    @Override
    public boolean isSpeakType(String message) {
        CsjlogProxy.getInstance().info("语音查找： " + message);
        if (TextUtils.isEmpty(message)) {
            return false;
        }
        for (ProductTypeBean bean : mList) {
            if (message.contains(bean.getGoodsName())) {
                ArrayList<GoodsDetailBean> list = Constants.Product.sProductDetails.get(bean.getId());
                if (list == null) {
                    list = new ArrayList<>();
                }
                ARouter.getInstance()
                        .build(BRouterPath.PRODUCT_CLOTHING_LIST)
                        .withParcelableArrayList(ProductListActivity.PRODUCT_DATA, list)
                        .navigation();
                return true;
            }
        }
        return false;
    }

    private void updateDots() {
        double a = (double) mList.size() / (double) (2 * 3);
        int pageSize = (int) Math.ceil(a);
        mView.llDots().removeAllViews();
        if (pageSize > 1) {
            SkinCompatImageView dot;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;
            for (int i = 0; i < pageSize; i++) {
                //小圆点
                dot = new SkinCompatImageView(mContext);
                dot.setScaleType(ImageView.ScaleType.CENTER_CROP);
                dot.setLayoutParams(params);
                if (i == 0) {
                    dot.setImageResource(R.drawable.iv_point_selected);
                } else {
                    dot.setImageResource(R.drawable.iv_point_unselected);
                }
                mView.llDots().addView(dot);
            }
        }
    }
}
