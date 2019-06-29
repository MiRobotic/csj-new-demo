package com.csjbot.mobileshop.feature.content.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.content.bean.ChildContentLevelThreeBean;
import com.csjbot.mobileshop.global.Constants;

/**
 * @author ShenBen
 * @date 2018/10/20 16:20
 * @email 714081644@qq.com
 */
public class ContentThreePopupAdapter extends BaseQuickAdapter<ChildContentLevelThreeBean.ChildrenBean, BaseViewHolder> {

    /**
     * 字符串编码格式
     */
    private static final String ENCODE = "UTF-8";
    private static final int K = 0x80;

    public ContentThreePopupAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChildContentLevelThreeBean.ChildrenBean item) {
        TextView view = helper.getView(R.id.tv_item_popup_title);
        view.setText(getSubString(item.getTitle(), 7));
        view.setTextColor(item.isChecked() ?
                Color.parseColor(Constants.SceneColor.sContentPopupItemTextSelectedColor) :
                Color.parseColor(Constants.SceneColor.sContentPopupItemTextUnSelectedColor));
        view.setBackgroundColor(item.isChecked() ?
                Color.parseColor(Constants.SceneColor.sContentPopupItemSelectedBg) :
                Color.parseColor(Constants.SceneColor.sContentPopupItemUnSelectedBg));
    }

    /**
     * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
     *
     * @param c
     * @return boolean
     */
    private boolean isLetter(char c) {
        return (c / K) == 0;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
     *
     * @param s 需要得到长度的字符串
     * @return 得到的字符串长度
     */
    private double length(String s) {
        if (s == null) {
            return 0;
        }
        char[] c = s.toCharArray();
        double len = 0;
        for (char aC : c) {
            if (isLetter(aC)) {
                len += 0.5;
            } else {
                len += 1;
            }
        }
        return Math.ceil(len);
    }

    /**
     * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
     *
     * @param origin 原始字符串
     * @param len    截取长度(一个汉字长度按1算的)
     * @return String, 返回的字符串
     */
    private String getSubString(String origin, int len) {
        try {
            // 字符串为空
            if (TextUtils.isEmpty(origin)) {
                return "";
            }
            // 截取长度大于字符串长度
            if (len > length(origin)) {
                return origin;
            }

            StringBuilder buffer = new StringBuilder();
            char[] array = origin.toCharArray();
            double currentLength = 0;
            for (char c : array) {
                // 字符长度
                int charlen = String.valueOf(c).getBytes(ENCODE).length;
                // 汉字按一个长度，字母数字按半个长度
                if (charlen == 3) {
                    currentLength += 1;
                } else {
                    currentLength += 0.5;
                }
                if (currentLength <= len) {
                    buffer.append(c);
                } else {
                    break;
                }
            }
            buffer.append("…");
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
