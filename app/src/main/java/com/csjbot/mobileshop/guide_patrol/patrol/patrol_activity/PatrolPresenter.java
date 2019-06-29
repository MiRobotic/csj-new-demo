package com.csjbot.mobileshop.guide_patrol.patrol.patrol_activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.csjbot.mobileshop.guide_patrol.patrol.bean.PatrolBean;
import com.csjbot.mobileshop.util.BlackgagaLogger;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.mobileshop.util.StrUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2019/1/21.
 */

public class PatrolPresenter implements PatrolContract.Presenter{

    private PatrolContract.View view;
    private Context mContext;
    private List<PatrolBean> navis;

    public PatrolPresenter(Context context) {
        mContext = context;
        navis = new ArrayList<>();
    }

    @Override
    public PatrolContract.View getView() {
        return view;
    }

    @Override
    public void initView(PatrolContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if (view != null) {
            view = null;
        }
    }

    @Override
    public String getMapPatrolName(String name) {
        String naviName = "";
        if (StrUtil.isNotBlank(name)) {
            if (name.length() > 5) {
                naviName = name.substring(0, 4) + "...";
            } else {
                naviName = name;
            }
        }
        return naviName;
    }

    @Override
    public void getPatrolData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = SharedPreUtil.getString(SharedKey.PATROL_POINT_NAME, SharedKey.PATROL_POINT_KEY);
                navis = GsonUtils.jsonToObject(json, new TypeToken<List<PatrolBean>>() {
                }.getType());
                /* 读取完毕,发送handler处理 */
                if (navis != null && navis.size() > 0) {
                    BlackgagaLogger.debug("navis:" + navis.size());
                    BlackgagaLogger.debug("json:----->" + json);
                }
                mHandle.obtainMessage(0).sendToTarget();
            }
        }).start();
    }

    Handler mHandle = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            /* 显示地图 */
            view.showPatrolPointMap(navis);
        }
    };
}
