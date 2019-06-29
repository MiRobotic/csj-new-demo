package com.csjbot.mobileshop.feature.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.feature.product.mvp.ProductTypeContract;
import com.csjbot.mobileshop.feature.product.mvp.ProductTypePresenter;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.router.BRouterPath;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品种类界面
 *
 * @author ShenBen
 * @date 2018/11/13 9:48
 * @email 714081644@qq.com
 */
@Route(path = BRouterPath.PRODUCT_CLOTHING_TYPE)
public class ProductTypeActivity extends BaseModuleActivity implements ProductTypeContract.View {

    @BindView(R.id.rv_clothing)
    RecyclerView rvClothing;
    @BindView(R.id.ll_dots)
    LinearLayout llDots;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.fl_no_data)
    FrameLayout flNoData;
    @BindView(R.id.tv_null_name)
    TextView tvNullName;


    private ProductTypeContract.Presenter mPresenter;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_product_type, R.layout.vertical_activity_product_type);
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
        new ProductTypePresenter(this, this);
        mPresenter.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
    }


    @Override
    public void setPresenter(ProductTypeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView getRvCloth() {
        return rvClothing;
    }

    @Override
    public LinearLayout llDots() {
        return llDots;
    }

    @Override
    public void speakMessage(String message) {
        prattle(message);
    }

    @Override
    public void isNoData(boolean isNodata) {
        if (isNodata) {
            llData.setVisibility(View.GONE);
            flNoData.setVisibility(View.VISIBLE);
            if (!tvNullName.isShown()) tvNullName.setVisibility(View.VISIBLE);
            prattle(getString(R.string.no_data));
        } else {
            if (tvNullName.isShown()) tvNullName.setVisibility(View.GONE);
            flNoData.setVisibility(View.GONE);
            if (CsjLanguage.CURRENT_LANGUAGE == CsjLanguage.CHINESE) {
                if (mPresenter.getProductTypes().isEmpty()) {
                    prattle("您可以点击查看商品列表");
                } else {
                    int size = mPresenter.getProductTypes().size();
                    StringBuilder speak = new StringBuilder();
                    StringBuilder show = new StringBuilder();
                    show.append("您可以点击屏幕上的按钮，或者语音通过语音对我说:\n");
                    speak.append("您可以通过语音对我说:\n");
                    for (int i = 0; i < size; i++) {
                        if (i <= 2) {
                            speak.append(mPresenter.getProductTypes().get(i).getGoodsName()).append("、");
                            show.append(i + 1).append("、").append(mPresenter.getProductTypes().get(i).getGoodsName()).append("\n");
                        } else {
                            break;
                        }
                    }
                    speak.deleteCharAt(speak.length() - 1);
                    speak(speak.toString());
                    setRobotChatMsg(show.toString());
                }
            }
            llData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        if (mPresenter.isSpeakType(text)) {
            return true;
        }
        prattle(answerText);
        return true;
    }


}
