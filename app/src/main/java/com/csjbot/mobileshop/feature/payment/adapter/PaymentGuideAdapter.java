package com.csjbot.mobileshop.feature.payment.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.GlideApp;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.payment.bean.PaymentGuideBean;

/**
 * @author ShenBen
 * @date 2019/1/22 11:12
 * @email 714081644@qq.com
 */
public class PaymentGuideAdapter extends BaseQuickAdapter<PaymentGuideBean, BaseViewHolder> {
    /**
     * 上一次点击的位置
     */
    private int mLastPosition = -1;

    public PaymentGuideAdapter() {
        super(R.layout.item_payment_guide);
    }

    @Override
    protected void convert(BaseViewHolder helper, PaymentGuideBean item) {
        if (!TextUtils.isEmpty(item.getQrCode())) {//二维码链接不为空
            ImageView ivIcon = helper.getView(R.id.iv_icon);
            ImageView ivQrCode = helper.getView(R.id.iv_qr_code);

            GlideApp.with(ivQrCode)
                    .load(item.getQrCode())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivQrCode);
            if (!TextUtils.isEmpty(item.getIcon())) {//icon不为空
                GlideApp.with(ivIcon)
                        .load(item.getIcon())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(ivIcon);
            }
            if (item.isCanOverTurn()) {//是否可以翻转
                ivIcon.setVisibility(item.isOverTurn() ? View.INVISIBLE : View.VISIBLE);
                ivQrCode.setVisibility(item.isOverTurn() ? View.VISIBLE : View.INVISIBLE);
            } else {//不可以
                ivIcon.setVisibility(View.INVISIBLE);
                ivQrCode.setVisibility(View.VISIBLE);
            }
        }
        helper.setText(R.id.tv_payment_name, item.getName());
    }

    public void overTurn(int position) {
        CsjlogProxy.getInstance().error("缴费： 当前点击： " + position + "，上一次点击： " + mLastPosition);
        if (mLastPosition != -1) {//如果不是第一次点击
            turn(mLastPosition);
            if (mLastPosition == position) {//上一次点击的位置和当前点击的位置不一致
                mLastPosition = -1;
                return;
            }
        }
        turn(position);
        mLastPosition = position;
    }

    private void turn(int position) {
        PaymentGuideBean bean = getItem(position);
        if (bean == null || !bean.isCanOverTurn()) {//null或不可以翻转，返回
            return;
        }
        FrameLayout flRoot = (FrameLayout) getViewByPosition(position, R.id.fl_root);
        ImageView rivIcon = (ImageView) getViewByPosition(position, R.id.iv_icon);
        ImageView rivQrCode = (ImageView) getViewByPosition(position, R.id.iv_qr_code);

        if (flRoot == null || rivIcon == null || rivQrCode == null) {
            return;
        }
        int direction = 1;
        if (rivQrCode.isShown()) {
            direction = -1;
        }
        int duration = 500;
        flip(flRoot, duration, direction);
        flRoot.postDelayed(() -> switchViewVisibility(rivQrCode, rivIcon, bean), duration);
    }

    /**
     * 旋转
     *
     * @param view      view
     * @param duration  时长
     * @param direction 方向 1或-1
     */
    private void flip(View view, int duration, int direction) {
        if (view == null) {
            return;
        }
        if (direction != 1 && direction != -1) direction = 1;
        view.setCameraDistance(16000 * view.getResources().getDisplayMetrics().density);
        AnimatorSet animSet = new AnimatorSet();
        ObjectAnimator rotationY = new ObjectAnimator();
        rotationY.setDuration(duration).setPropertyName("rotationY");
        rotationY.setFloatValues(0, 90 * direction);
        ObjectAnimator _rotationY = new ObjectAnimator();
        _rotationY.setDuration(duration).setPropertyName("rotationY");
        _rotationY.setFloatValues(-90 * direction, 0);
        _rotationY.setStartDelay(duration);
        ObjectAnimator scale = new ObjectAnimator();
        scale.setDuration(duration).setPropertyName("scaleY");
        scale.setFloatValues(1f, 1f);
        ObjectAnimator _scale = new ObjectAnimator();
        _scale.setDuration(duration).setPropertyName("scaleY");
        _scale.setFloatValues(1f, 1f);
        _scale.setStartDelay(duration);
        animSet.setTarget(view);
        rotationY.setTarget(view);
        _rotationY.setTarget(view);
        scale.setTarget(view);
        _scale.setTarget(view);
        animSet.playTogether(rotationY, _rotationY, scale, _scale);
        animSet.start();
    }

    /**
     * 控制控件是否可见
     *
     * @param ivBack
     * @param ivFront
     * @param bean
     */
    private void switchViewVisibility(ImageView ivBack, ImageView ivFront, PaymentGuideBean bean) {
        if (ivBack == null || ivFront == null) {
            return;
        }
        if (ivBack.isShown()) {
            ivBack.setVisibility(View.INVISIBLE);
            ivFront.setVisibility(View.VISIBLE);
            bean.setOverTurn(false);
        } else {
            bean.setOverTurn(true);
            ivBack.setVisibility(View.VISIBLE);
            ivFront.setVisibility(View.INVISIBLE);
        }
    }
}
