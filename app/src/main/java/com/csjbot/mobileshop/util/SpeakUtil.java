package com.csjbot.mobileshop.util;

import android.text.TextUtils;

import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.localbean.NaviBean;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2019/1/10.
 * 话术
 */

public class SpeakUtil {

    /**
     * 获取带路兜底话术
     */
    public static String getNaviNoMatchSpeak() {
        String robotname = "";//机器人名称
        if (Constants.greetContentBean != null) {
            robotname = Constants.greetContentBean.getRobotName();
        }
        String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
        List<NaviBean> naviBeans;
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(json)) {
            naviBeans = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
            }.getType());
            if (naviBeans != null && naviBeans.size() > 0) {
                StringBuilder nameBuilder = new StringBuilder();
                nameBuilder.append(naviBeans.get(0).getName());
                if (naviBeans.size() > 1) {
                    nameBuilder.append("}{").append(naviBeans.get(1).getName());
                }
                builder.append("这个地方").append(robotname).append("还不能到达，您可以让我带路到{").append(nameBuilder.toString()).append("}");
            } else {
                builder.append("这个地方").append(robotname).append("还不能到达，您可以和我说点别的");
            }
        } else {
            builder.append("这个地方").append(robotname).append("还不能到达，您可以和我说点别的");
        }

        return builder.toString();
    }

    public static String getRobotName() {
        String robotName = "";//机器人名称
        if (Constants.greetContentBean != null) {
            robotName = Constants.greetContentBean.getRobotName();
        }
        return robotName;
    }
}
