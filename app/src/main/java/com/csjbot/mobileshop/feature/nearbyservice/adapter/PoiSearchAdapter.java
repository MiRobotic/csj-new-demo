package com.csjbot.mobileshop.feature.nearbyservice.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.nearbyservice.bean.PoiSearchBean;

import java.util.List;
import java.util.Objects;

/**
 * @author ShenBen
 * @date 2019/1/24 10:24
 * @email 714081644@qq.com
 */
public class PoiSearchAdapter extends BaseQuickAdapter<PoiSearchBean, BaseViewHolder> {

    private int mLastPosition = -1;

    public PoiSearchAdapter() {
        super(R.layout.item_poi_search, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoiSearchBean item) {
        helper.setText(R.id.tv_address, item.getName())
                .setText(R.id.tv_detail, item.getAddress())
                .setText(R.id.tv_distance, "距离：" + item.getDistance())
                .setVisible(R.id.iv_checked, item.isChecked());

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        PoiSearchBean bean = getItem(position);
        if (payloads.isEmpty()) {
            convert(holder, bean);
        } else {
            if (bean != null) {
                holder.setVisible(R.id.iv_checked, bean.isChecked());
            }
        }
    }

    public void checkAddress(int position) {
        if (position == mLastPosition) {
            return;
        }
        if (mLastPosition != -1) {
            Objects.requireNonNull(getItem(mLastPosition)).setChecked(false);
            notifyItemChanged(mLastPosition, "check");
        }
        Objects.requireNonNull(getItem(position)).setChecked(true);
        notifyItemChanged(position, "check");
        mLastPosition = position;
    }

    public int getLastPosition() {
        return mLastPosition;
    }
}
