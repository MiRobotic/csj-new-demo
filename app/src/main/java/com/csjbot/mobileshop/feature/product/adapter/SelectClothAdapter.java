package com.csjbot.mobileshop.feature.product.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.product.bean.SelectClothBean;

import java.util.List;

/**
 * @author ShenBen
 * @date 2018/11/12 18:55
 * @email 714081644@qq.com
 */

public class SelectClothAdapter extends BaseQuickAdapter<SelectClothBean, BaseViewHolder> {

    public SelectClothAdapter(@LayoutRes int layoutResId, @Nullable List<SelectClothBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectClothBean item) {
        helper.setText(R.id.tv_item_type, item.getType())
                .setTextColor(R.id.tv_item_type, item.isChecked() ? Color.parseColor("#d09b7f") : Color.parseColor("#393939"));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        SelectClothBean bean = getItem(position);
        assert bean != null;
        if (payloads.isEmpty()) {
            convert(holder, bean);
        } else {
            holder.setTextColor(R.id.tv_item_type, bean.isChecked() ? Color.parseColor("#d09b7f") : Color.parseColor("#393939"));
        }
    }
}
