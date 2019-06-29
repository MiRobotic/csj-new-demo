package com.csjbot.mobileshop.widget.chat_view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.csjbot.mobileshop.GlideApp;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.widget.GlideImageLoader;
import com.csjbot.mobileshop.widget.MyBanner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.List;

/**
 * @author ShenBen
 * @date 2019/2/26 18:50
 * @email 714081644@qq.com
 */
public class PictureDialog extends Dialog {
    private ImageView ivDetail;
    private MyBanner mBanner;

    public PictureDialog(@NonNull Context context) {
        this(context, R.style.NewRetailDialog);
    }

    public PictureDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setCancelable(false);
        setContentView(R.layout.layout_chat_picture_detail);
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
        }
        ivDetail = findViewById(R.id.iv_detail);

        mBanner = findViewById(R.id.chat_view_banner);
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);

        mBanner.isAutoPlay(false);

        mBanner.setOnBannerListener(position -> mBanner.stopAutoPlay());

        ImageButton button = findViewById(R.id.ib_close);
        button.setOnClickListener(v -> cancel());
    }

    public void setAutoPlay() {
        mBanner.isAutoPlay(true);
        mBanner.startAutoPlay();
    }

    public void setPictures(List<String> pictures) {
        mBanner.update(pictures);
    }


    public void setImageUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        GlideApp.with(ivDetail)
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivDetail);
    }

}
