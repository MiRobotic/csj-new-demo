package com.csjbot.mobileshop.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.csjbot.mobileshop.GlideApp;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.global.Constants;

import java.io.File;

/**
 * @author ShenBen
 * @date 2018/12/10 8:41
 * @email 714081644@qq.com
 */

public class TitleView extends FrameLayout implements View.OnClickListener {
    private Context mContext;
    /**
     * wifi信号图标控件
     */
    private ImageView mIvWifi;
    /**
     * 切换语言按钮,默认-->不显示
     */
    private ImageButton mIbLanguage;

    /**
     * 切换语言按钮,默认-->不显示
     * 代替 mIbLanguage
     */
    private TextView mTvLanguage;

    /**
     * 返回按钮,默认-->显示
     */
    private ImageButton mIbBack;
    /**
     * 主页按钮,默认-->显示
     */
    private ImageButton mIbHome;
    /**
     * Logo按钮,默认-->显示
     */
    private ImageView mIvLogo;
    /**
     * 购物车父布局,默认-->不显示
     */
    private FrameLayout mFlShopCart;
    /**
     * 购物车按钮
     */
    private ImageButton mIbShopCart;
    /**
     * 购物车数量
     */
    private TextView mTvShopCount;
    /**
     * 客服按钮,默认-->显示
     */
    private ImageButton mIbCustomer;
    /**
     * 设置按钮,默认-->显示
     */
    private ImageButton mIbSetting;

    private OnViewClickListener mListener;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        if (isPlus()) {
            LayoutInflater.from(mContext).inflate(R.layout.layout_title_vertical, this, true);
        } else {
            LayoutInflater.from(mContext).inflate(R.layout.layout_title, this, true);
        }
        mIvWifi = findViewById(R.id.iv_wifi);
        mIbLanguage = findViewById(R.id.ib_language);
        mTvLanguage = findViewById(R.id.tv_mainpage_language);
        mIbBack = findViewById(R.id.ib_back);
        mIbHome = findViewById(R.id.ib_home);
        mIvLogo = findViewById(R.id.iv_logo);
        mFlShopCart = findViewById(R.id.fl_shop_cart);
        mIbShopCart = findViewById(R.id.ib_shop_cart);
        mTvShopCount = findViewById(R.id.tv_shop_count);
        mIbCustomer = findViewById(R.id.ib_customer);
        mIbSetting = findViewById(R.id.ib_setting);
        mIbLanguage.setOnClickListener(this);
        mTvLanguage.setOnClickListener(this);
        mIbBack.setOnClickListener(this);
        mIbHome.setOnClickListener(this);
        mIbShopCart.setOnClickListener(this);
        mIbCustomer.setOnClickListener(this);
        mIbSetting.setOnClickListener(this);
        Glide.with(mContext).load(R.drawable.gif_btn).into(mIbCustomer);

        if (TextUtils.equals(Constants.Scene.CurrentScene, "qiche")
                || TextUtils.equals(Constants.Scene.CurrentScene, "shouji")
                || TextUtils.equals(Constants.Scene.CurrentScene, "canting")
                || TextUtils.equals(Constants.Scene.CurrentScene, "zhanguan")
                || TextUtils.equals(Constants.Scene.CurrentScene, "kejiguan")) {
            mTvLanguage.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    /**
     * 设置wifi强度
     * 0: 无wifi
     * 1: 信号1格
     * 2: 信号2格
     * 3: 信号3格
     *
     * @param magnitude wifi强度
     */
    public void setWifiMagnitude(@IntRange(from = 0, to = 3) int magnitude) {
        int resId;
        switch (magnitude) {
            case 0:
                resId = isPlus() ? R.drawable.wifi_no_vertical : R.drawable.wifi_no;
                break;
            case 1:
                resId = isPlus() ? R.drawable.wifi_1_vertical : R.drawable.wifi_1;
                break;
            case 2:
                resId = isPlus() ? R.drawable.wifi_2_vertical : R.drawable.wifi_2;
                break;
            case 3:
                resId = isPlus() ? R.drawable.wifi_3_vertical : R.drawable.wifi_3;
                break;
            default:
                resId = isPlus() ? R.drawable.wifi_no_vertical : R.drawable.wifi_no;
                break;
        }
        mIvWifi.setBackgroundResource(resId);
    }

    /**
     * 语言按钮显示
     * 同时会使返回、主页按钮隐藏
     */
    public void setLanguageLayoutVisible() {
        mIbBack.setVisibility(GONE);
        mIbHome.setVisibility(GONE);
//        mIbLanguage.setVisibility(VISIBLE);
        mTvLanguage.setVisibility(VISIBLE);
    }

    public void setBackLayoutVisibility(int visibility) {
        mIbBack.setVisibility(visibility);
    }

    public void setHomeLayoutGone() {
        mIbHome.setVisibility(GONE);
    }

    /**
     * 购物车布局显示
     */
    public void setShopCartLayoutVisible() {
        mFlShopCart.setVisibility(VISIBLE);
    }

    /**
     * 购物车布局隐藏
     */
    public void setShopCartLayoutGone() {
        mFlShopCart.setVisibility(GONE);
    }

    /**
     * 购物车数字显示
     */
    public void setShopCartCountLayoutVisible() {
        mTvShopCount.setVisibility(VISIBLE);
    }

    /**
     * 购物车数字显隐藏
     */
    public void setShopCartCountLayoutGone() {
        mTvShopCount.setVisibility(GONE);
    }

    public void setCustomerLayoutVisible() {
        mIbCustomer.setVisibility(VISIBLE);
    }

    public void setCustomerLayoutGone() {
        mIbCustomer.setVisibility(GONE);
    }

    public void setSettingLayoutVisible() {
        mIbSetting.setVisibility(VISIBLE);
    }

    public void setSettingLayoutGone() {
        mIbSetting.setVisibility(GONE);
    }

    public void setLanguageImage(@DrawableRes int resId) {
        mIbLanguage.setImageResource(resId);
    }

    public void SetLanguageText(String text) {
        mTvLanguage.setText(text);
    }

    public void setShopCount(String count) {
        mTvShopCount.setText(count);
    }

    public void setLogoRes(@DrawableRes int resId) {
        mIvLogo.setBackgroundResource(resId);
    }

    public void setLogoByUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);
            GlideApp.with(this)
                    .load(url)
                    .apply(options)
                    .into(mIvLogo);
        }
    }

    public void setLogoBySd(String filePath) {
        if (mIvLogo != null) {
            mIvLogo.setImageDrawable(null);
            if (new File(filePath).exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(filePath);
                mIvLogo.setImageBitmap(bmp);
            } else {
                mIvLogo.setImageResource(R.drawable.small_logo);
            }
        }
    }


    public FrameLayout getShoppingCart() {
        return mFlShopCart;
    }


    /**
     * 设置点击事件监听
     *
     * @param listener
     */
    public void setOnViewClickListener(OnViewClickListener listener) {
        mListener = listener;
    }

    /**
     * 是否是大屏
     *
     * @return
     */
    private boolean isPlus() {
        return Constants.isPlus();
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onClick(v);
        }
    }

    public interface OnViewClickListener {
        void onClick(View v);
    }
}
