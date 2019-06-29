package com.csjbot.mobileshop.widget;

import android.content.Context;
import android.widget.ImageView;

import com.csjbot.mobileshop.GlideApp;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by 孙秀艳 on 2017/10/16.
 * 本地轮播图片
 */

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        GlideApp.with(context).load(path).into(imageView);
    }
}
