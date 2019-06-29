package com.csjbot.mobileshop.base.test;

import android.text.TextUtils;

import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/7/18.
 */

public abstract class BaseMainPageActivity extends BaseFullScreenActivity {

    @Override
    public void init() {
        super.init();
        SharedPreUtil.putInt(SharedKey.STARTMODE, SharedKey.STARTMODE, 1);//热启动
        Constants.Charging.initCharging();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTimezone();
    }

    private void setTimezone() {
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        int timeOffSet = cal.get(Calendar.ZONE_OFFSET);
        ServerFactory.getRobotState().setTimezone(String.valueOf(timeOffSet), CsjLanguage.getLanguageStrForTimeZone());
    }

    @Override
    protected boolean dealIntent(String intentJson, String source) {
        if (super.dealIntent(intentJson, source)) {
            return true;
        }
        boolean isNotEmptyIntent = true;
        getLog().warn("BaseMainPageActivity:intentJson:" + intentJson);
        String intent = "";
        try {
            intent = new JSONObject(intentJson).getString("intent");
            getLog().warn("BaseMainPageActivity:intent:" + intent);
        } catch (JSONException e) {
            getLog().error("BaseMainPageActivity intent json 解析失败:" + e.toString());
        }
        switch (intent) {
//            case Intents.PRODUCT_BUY:
//                product(intentJson,source);
//                break;
            default:
                isNotEmptyIntent = false;
                break;
        }
        if (!isNotEmptyIntent) {
            chat(source);
        }
        return isNotEmptyIntent;
    }

    @Override
    protected String getIntentType(String json) {
        String intentJson = super.getIntentType(json);
        if (!TextUtils.isEmpty(intentJson)) {
            return intentJson;
        }
        String empty = "";
        JSONObject result = null;
        try {
            result = new JSONObject(json).getJSONObject("result");
            String text = result.getString("text");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return empty;
    }

}
