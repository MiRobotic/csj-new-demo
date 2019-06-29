package com.csjbot.mobileshop.feature.product;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.feature.product.adapter.ProductAttributeAdapter;
import com.csjbot.mobileshop.feature.product.bean.ProductAttributeBean;
import com.csjbot.mobileshop.feature.product.decoration.SpacesItemDecoration;
import com.csjbot.mobileshop.model.http.bean.rsp.GoodsDetailBean;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.widget.GlideImageLoader;
import com.csjbot.mobileshop.widget.MyBanner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 展示商品详情界面
 *
 * @author ShenBen
 * @date 2018/11/12 13:20
 * @email 714081644@qq.com
 */

@Route(path = BRouterPath.PRODUCT_CLOTHING_DETAIL)
public class ProductDetailsActivity extends BaseModuleActivity {

    public static final String PRODUCT_DETAIL = "PRODUCT_DETAIL";

    @BindView(R.id.banner)
    MyBanner mBanner;
    @BindView(R.id.tv_cloth_name)
    TextView tvClothName;
    @BindView(R.id.tv_original_price)
    TextView tvOriginalPrice;
    @BindView(R.id.tv_present_price)
    TextView tvPresentPrice;
    @BindView(R.id.rv_attribute)
    RecyclerView rvAttribute;
    @BindView(R.id.tv_cloth_introduce)
    TextView tvClothIntroduce;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_product_details, R.layout.vertical_activity_product_details);
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        tvClothIntroduce.setMovementMethod(ScrollingMovementMethod.getInstance());

        Intent intent = getIntent();
        if (intent != null) {
            GoodsDetailBean mBean = intent.getParcelableExtra(PRODUCT_DETAIL);
            List<String> list = new ArrayList<>();
            if (mBean != null) {
                list.add(mBean.getCoverUrl());
                if (mBean.getImageUrls() != null && !mBean.getImageUrls().isEmpty()) {
                    list.clear();
                    for (GoodsDetailBean.ImageUrlsBean bean : mBean.getImageUrls()) {
                        list.add(bean.getUrl());
                    }
                }
                mBanner.setImages(list);
                mBanner.start();
                if (mBean.getParamTypeInfoModels() != null && !mBean.getParamTypeInfoModels().isEmpty()) {
                    ProductAttributeBean attributeBean;
                    List<ProductAttributeBean> mList = new ArrayList<>();
                    ProductAttributeAdapter mAdapter = new ProductAttributeAdapter(mList);
                    LinearLayoutManager manager = new LinearLayoutManager(context) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    };
                    rvAttribute.setLayoutManager(manager);
                    rvAttribute.setHasFixedSize(true);
                    rvAttribute.setAdapter(mAdapter);
                    rvAttribute.addItemDecoration(new SpacesItemDecoration(10));
                    for (GoodsDetailBean.ParamTypeInfoModelsBean bean : mBean.getParamTypeInfoModels()) {
                        attributeBean = new ProductAttributeBean(bean.getAttName(), bean.getLabelName());
                        mList.add(attributeBean);
                    }
                    mAdapter.setNewData(mList);
                } else {
                    rvAttribute.setVisibility(View.GONE);
                }
                tvClothName.setText(mBean.getName());
                tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tvOriginalPrice.setText(TextUtils.isEmpty(mBean.getMoneyType()) ? "￥" : mBean.getMoneyType()
                        + mBean.getCostPrice());
                tvPresentPrice.setText(TextUtils.isEmpty(mBean.getMoneyType()) ? "￥" : mBean.getMoneyType()
                        + mBean.getNowPrice());
                tvClothIntroduce.setText(mBean.getRemarks());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBanner.stopAutoPlay();
        mBanner.releaseBanner();
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        prattle(answerText);
        return true;
    }
}
