package com.csjbot.mobileshop.guide_patrol.patrol.patrol_activity;

import android.widget.Button;
import android.widget.EditText;

import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.BaseView;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.guide_patrol.patrol.bean.PatrolBean;
import com.csjbot.mobileshop.model.tcp.bean.Position;

import java.util.List;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class PatrolContract {

    interface Presenter extends BasePresenter<View> {
        void getPatrolData();//获取巡视点数据
        String getMapPatrolName(String name);
    }

    interface View extends BaseView {
        void showPatrolPointMap(List<PatrolBean> patrolBeans);//在地图上显示巡视点
    }

}

