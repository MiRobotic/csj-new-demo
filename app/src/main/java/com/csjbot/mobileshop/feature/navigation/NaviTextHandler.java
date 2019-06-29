package com.csjbot.mobileshop.feature.navigation;

import android.text.TextUtils;

import com.csjbot.mobileshop.localbean.NaviBean;
import com.csjbot.mobileshop.util.BlackgagaLogger;
import com.github.promeg.pinyinhelper.Pinyin;

import java.util.List;

/**
 * Created by xiasuhuei321 on 2017/12/15.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class NaviTextHandler {
    public static PointMatchListener listener;

    public static boolean handle(String text) {
        BlackgagaLogger.debug(text);
        String pinyin = strConvertPinyin(text);
        List<NaviBean> data = NaviAction.getInstance().getData();

        for (String s : NAVI) {
            if (pinyin.contains(s)) {
                // 必须要要有导航点才能进入下面的<不可到达点》
                if (data == null) {
                    return false;
                }

                if (data.size() == 0) {
                    return false;
                }

                for (NaviBean naviBean : data) {
                    String namePinyin = strConvertPinyin(naviBean.getName());
                    String nickPinyin = strConvertPinyin(naviBean.getNickName());
                    if (TextUtils.isEmpty(nickPinyin)) {
                        if ((pinyin.contains(namePinyin)) && listener != null) {
                            listener.match(naviBean, true);
                            return true;
                        }
                    } else {
                        if ((pinyin.contains(namePinyin) || pinyin.contains(nickPinyin)) && listener != null) {
                            listener.match(naviBean, true);
                            return true;
                        }
                    }

                }

                // 没有匹配上
                if (listener != null) listener.noMatch(pinyin);
                return true;
            }
        }

        for (String s : GO_NAVI) {
            if (pinyin.contains(s) && listener != null) {
                listener.goNavi();
                return true;
            }
        }

        return false;
    }

    /**
     * 汉字转拼音方法
     *
     * @param text
     * @return
     */
    public static String strConvertPinyin(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        StringBuilder sbPinyin = new StringBuilder();
        char[] texts = text.toCharArray();
        for (char text1 : texts) {
            sbPinyin.append(Pinyin.toPinyin(text1));
        }
        return sbPinyin.toString();
    }

    private static final String[] NAVI = {"DAIWOQU", "WOXIANGQU", "ZAINA", "WOYAOQU"};
    private static final String[] GO_NAVI = {"DAOHANG"};

    public interface PointMatchListener {
        void noMatch(String msg);

        void match(NaviBean naviBean, boolean flag);

        void goNavi();
    }
}
