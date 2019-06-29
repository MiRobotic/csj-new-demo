package com.csjbot.mobileshop.feature.payment.contract;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.feature.payment.adapter.PaymentGuideAdapter;
import com.csjbot.mobileshop.feature.payment.bean.PaymentGuideBean;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.bean.rsp.QrCodeBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridLayoutManager;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridSnapHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author ShenBen
 * @date 2019/1/22 10:55
 * @email 714081644@qq.com
 */
public class PaymentGuidePresenterImpl implements PaymentGuideContract.Presenter {
    private Context mContext;
    private PaymentGuideContract.View mView;
    private PaymentGuideAdapter mAdapter;
    private List<PaymentGuideBean> mList;
    private long mLastTime;

    public PaymentGuidePresenterImpl(Context mContext, PaymentGuideContract.View mView) {
        this.mContext = mContext;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void init(@Nullable String key) {
        mList = new ArrayList<>();
        mAdapter = new PaymentGuideAdapter();
        mAdapter.bindToRecyclerView(mView.getRecycler());
        PagerGridLayoutManager manager = new PagerGridLayoutManager(2, 2, PagerGridLayoutManager.HORIZONTAL);
        mView.getRecycler().setLayoutManager(manager);
        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(mView.getRecycler());
        mView.getRecycler().setHasFixedSize(true);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastTime < 1000) {//1.5秒内只能点击一次
                return;
            }
            mLastTime = currentTime;
            mAdapter.overTurn(position);
            PaymentGuideBean bean = mList.get(position);
            if ((!bean.isCanOverTurn() && bean.isOverTurn()) //不能翻转，并且现在是已经翻转的状态
                    || (bean.isCanOverTurn() && !bean.isOverTurn())//或者是能翻转，并且现在不是翻转状态
                    ) {
                mView.showSpeakWord(mList.get(position).getWords());
            }
        });

        getQrCodeData(key);
    }

    @Override
    public boolean speakToIntent(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return false;
        }
        PaymentGuideBean bean;
        int size = mList.size();
        for (int i = 0; i < size; i++) {
            bean = mList.get(i);
            if (!bean.isOverTurn() && msg.contains(bean.getKey())) {
                mAdapter.overTurn(i);
                mView.showSpeakWord(bean.getWords());
                return true;
            }
        }
        return false;
    }

    private void getQrCodeData(@Nullable String key) {
        mList.clear();
        PaymentGuideBean bean;
        QrCodeBean qrCodeBean;
        int defaultCheckedPoistion = 0;
        int size = Constants.QrCode.sPaymentQrCodes.size();
        for (int i = 0; i < size; i++) {
            qrCodeBean = Constants.QrCode.sPaymentQrCodes.get(i);
            bean = new PaymentGuideBean();
            bean.setName(qrCodeBean.getQrName());
            bean.setWords(qrCodeBean.getQrWords());
            bean.setKey(qrCodeBean.getQrCodeKey());
            if (TextUtils.equals(key, qrCodeBean.getQrName())) {
                defaultCheckedPoistion = i;
            }
            if (qrCodeBean.getQrFile() != null && !qrCodeBean.getQrFile().isEmpty()) {
                bean.setQrCode(qrCodeBean.getQrFile().get(0).getUrl());
            }
            if (qrCodeBean.getIcon() != null && !qrCodeBean.getIcon().isEmpty()) {
                bean.setIcon(qrCodeBean.getIcon().get(0).getUrl());
            } else {//没有icon 的话，默认不能翻转，且直接为翻转状态
                bean.setOverTurn(true);
                bean.setCanOverTurn(false);
            }
            mList.add(bean);
        }

        if (!mList.isEmpty()) {//不为空再显示
            mView.isNoData(false, mList.get(defaultCheckedPoistion).getWords());
            mAdapter.setNewData(mList);
            int finalDefaultCheckedPoistion = defaultCheckedPoistion;
            mLastTime = System.currentTimeMillis();//禁止刚进入就点击
            new Handler().postDelayed(() -> mAdapter.overTurn(finalDefaultCheckedPoistion), 800);//必须加延迟
        } else {
            mView.isNoData(true, null);
        }
    }
}
