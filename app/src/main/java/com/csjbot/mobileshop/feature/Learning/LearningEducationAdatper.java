package com.csjbot.mobileshop.feature.Learning;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.mobileshop.R;

import java.util.List;

/**
 * Created by  Wql , 2018/3/7 11:49
 */
public class LearningEducationAdatper extends BaseQuickAdapter<LearnBean, BaseViewHolder> {


    public LearningEducationAdatper(@LayoutRes int layoutResId, @Nullable List<LearnBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LearnBean item) {
        ImageView iv = helper.getView(R.id.iv_learning_picture);
        iv.setBackground(item.getIcon());
        helper.setText(R.id.iv_learning_text, item.getAppName());
    }
}
