package com.csjbot.mobileshop.widget.language;


import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.global.Constants;


/**
 * @author ShenBen
 * @date 2018/12/13 17:21
 * @email 714081644@qq.com
 */

public class LanguageAdapter extends BaseQuickAdapter<LanguageBean, BaseViewHolder> {

    public LanguageAdapter() {
        super(R.layout.item_popup_language, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, LanguageBean item) {
        helper.setText(R.id.tv_language, item.getLanguage())
                .setTextColor(R.id.tv_language, item.isChecked() ?
                        Color.parseColor(Constants.SceneColor.sLanguagePopupItemTextSelectedColor) :
                        Color.parseColor(Constants.SceneColor.sLanguagePopupItemTextUnSelectedColor))
                .setBackgroundColor(R.id.ll_language, item.isChecked() ?
                        Color.parseColor(Constants.SceneColor.sLanguagePopupItemSelectedBg) :
                        Color.parseColor(Constants.SceneColor.sLanguagePopupItemUnSelectedBg));
    }
}
