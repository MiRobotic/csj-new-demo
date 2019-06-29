package com.csjbot.mobileshop.feature.product.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.product.bean.ProductAttributeBean;

import java.util.List;

/**
 * @author ShenBen
 * @date 2019/1/9 16:07
 * @email 714081644@qq.com
 */

public class ProductAttributeAdapter extends BaseQuickAdapter<ProductAttributeBean, BaseViewHolder> {

    public ProductAttributeAdapter(@Nullable List<ProductAttributeBean> data) {
        super(R.layout.item_product_attribute, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductAttributeBean item) {
        helper.setText(R.id.tv_attribute, item.getAttribute() + ": ")
                .setText(R.id.tv_label, item.getLabel());
    }
}
