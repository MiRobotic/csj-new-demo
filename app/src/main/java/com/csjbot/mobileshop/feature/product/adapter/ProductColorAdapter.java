package com.csjbot.mobileshop.feature.product.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.product.bean.SelectClothColor;

import java.util.List;

/**
 * @author ShenBen
 * @date 2018/12/6 9:27
 * @email 714081644@qq.com
 */

public class ProductColorAdapter extends BaseQuickAdapter<SelectClothColor, BaseViewHolder> {


    public ProductColorAdapter(@LayoutRes int layoutResId, @Nullable List<SelectClothColor> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectClothColor item) {
        helper.setText(R.id.tv_color, item.getColor())
                .setTextColor(R.id.tv_color, item.isChecked() ? Color.parseColor("#d09b7f") : Color.parseColor("#999999"))
                .setBackgroundRes(R.id.fl_bg, item.isChecked() ? R.drawable.cloth_color_select_bg : R.drawable.cloth_color_unselect_bg);
    }
}
