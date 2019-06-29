package com.csjbot.mobileshop.advertisement.adapter;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.mobileshop.GlideApp;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.model.http.bean.rsp.QrCodeBean;

/**
 * @author ShenBen
 * @date 2019/1/21 10:03
 * @email 714081644@qq.com
 */

public class FloatQrCodeAdapter extends BaseQuickAdapter<QrCodeBean, BaseViewHolder> {

    public FloatQrCodeAdapter() {
        super(R.layout.item_float_qr_view, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, QrCodeBean item) {
        ImageView iv = helper.getView(R.id.iv_qr_code);
        GlideApp.with(iv)
                .load(item.getQrFile().get(0).getUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(iv);
        helper.setText(R.id.tv_qr_code_name, item.getQrName());
    }
}
