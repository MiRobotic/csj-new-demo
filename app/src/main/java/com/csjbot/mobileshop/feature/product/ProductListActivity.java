package com.csjbot.mobileshop.feature.product;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.feature.product.mvp.ProductListContract;
import com.csjbot.mobileshop.feature.product.mvp.ProductListPresenter;
import com.csjbot.mobileshop.model.http.bean.rsp.GoodsDetailBean;
import com.csjbot.mobileshop.router.BRouterPath;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商品列表界面
 *
 * @author ShenBen
 * @date 2018/11/12 10:59
 * @email 714081644@qq.com
 */
@Route(path = BRouterPath.PRODUCT_CLOTHING_LIST)
public class ProductListActivity extends BaseModuleActivity implements ProductListContract.View {
    public static final String PRODUCT_DATA = "PRODUCT_DATA";

    @BindView(R.id.tv_select_clothing)
    TextView tvSelectClothing;
    @BindView(R.id.rv_clothing)
    RecyclerView rvClothing;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.fl_no_data)
    FrameLayout flNoData;

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;

    @OnClick({R.id.tv_select_clothing, R.id.iv_left, R.id.iv_right})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_clothing:
                mPresenter.showSelectPopup();
                break;
            case R.id.iv_left:
                mPresenter.previousPage();
                break;
            case R.id.iv_right:
                mPresenter.nextPage();
                break;
        }
    }

    private ProductListContract.Presenter mPresenter;
    private ArrayList<GoodsDetailBean> list;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_product_list, R.layout.vertical_activity_product_list);
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
        new ProductListPresenter(this, this);
        Intent intent = getIntent();
        if (intent != null) {
            list = intent.getParcelableArrayListExtra(PRODUCT_DATA);
            mPresenter.init();
            if (list.size() <= 6) {
                setIvGone();
            }
            mPresenter.loadData(list);
        } else {
            isNoData(true);
        }
    }

//    @Override
//    protected CharSequence initChineseSpeakText() {
//        return null;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    public void setPresenter(ProductListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView getRvCloth() {
        return rvClothing;
    }

    @Override
    public TextView getSelectCloth() {
        return tvSelectClothing;
    }

    @Override
    public void isNoData(boolean isNodata) {
        if (isNodata) {
            llData.setVisibility(View.GONE);
            flNoData.setVisibility(View.VISIBLE);
            prattle(getString(R.string.no_data));
        } else {
            flNoData.setVisibility(View.GONE);
            llData.setVisibility(View.VISIBLE);
            prattle(getString(R.string.product_tip));
        }
    }

    @Override
    public void setIvGone() {
        ivLeft.setVisibility(View.INVISIBLE);
        ivRight.setVisibility(View.INVISIBLE);
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
