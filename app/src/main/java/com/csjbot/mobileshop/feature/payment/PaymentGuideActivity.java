package com.csjbot.mobileshop.feature.payment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.feature.payment.contract.PaymentGuideContract;
import com.csjbot.mobileshop.feature.payment.contract.PaymentGuidePresenterImpl;
import com.csjbot.mobileshop.router.BRouterPath;

import butterknife.BindView;

@Route(path = BRouterPath.PAYMENT_GUIDE)
public class PaymentGuideActivity extends BaseModuleActivity implements PaymentGuideContract.View {
    public static final String KEY = "KEY";
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.ll_dots)
    LinearLayout llDots;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.fl_no_data)
    FrameLayout flNoData;
    private PaymentGuideContract.Presenter mPresenter;

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_payment_guide, R.layout.activity_plus_payment_guide);
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
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        new PaymentGuidePresenterImpl(this, this);
        Intent intent = getIntent();
        if (intent != null) {
            mPresenter.init(intent.getStringExtra(KEY));
        } else {
            mPresenter.init(null);
        }
    }

    @Override
    public void setPresenter(PaymentGuideContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView getRecycler() {
        return rv;
    }

    @Override
    public LinearLayout getDots() {
        return llDots;
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return true;
        }
        if (mPresenter.speakToIntent(text)) {
            return true;
        }
        prattle(answerText);
        return true;
    }

    @Override
    public void isNoData(boolean isNoData, String msg) {
        if (isNoData) {
            llData.setVisibility(View.GONE);
            flNoData.setVisibility(View.VISIBLE);
        } else {
            llData.setVisibility(View.VISIBLE);
            flNoData.setVisibility(View.GONE);
            showSpeakWord(msg);
        }
    }

    @Override
    public void showSpeakWord(String msg) {
        if (mSpeak.isSpeaking()) {
            mSpeak.stopSpeaking();
        }
        prattle(msg);
    }
}
