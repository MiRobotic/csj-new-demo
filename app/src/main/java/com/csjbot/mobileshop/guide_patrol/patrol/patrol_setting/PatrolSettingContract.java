package com.csjbot.mobileshop.guide_patrol.patrol.patrol_setting;

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

public class PatrolSettingContract {

    interface Presenter extends BasePresenter<View> {
        void getPosition();//获取当前位置
        void getPatrolData();//获取巡视点数据
        void addPatrolPoint();//新增巡视点
        int getIndexByData(PatrolBean patrolBean);//获取巡视点的索引
        void savePatrolPointByPosition(PatrolBean patrolBean, int position);//保存巡视点
        void showEdittextDialog(String title, String content, EditText editText, int length);//添加和修改编辑框对话框
        void showRecyclerViewDialog(String title, Button editText, Constants.ResourceSuffix.suffix suffix);//选择文件对话框
        void removePatrolPointByPosition(int position);//移除巡视点
        boolean getPatrolPlan();//获取巡视计划
        void savePatrolPlan(boolean isCircle);//获取巡视计划  true 循环巡视   false  单次巡视
        void getGuideData();//获取巡视路线的数据
        void saveGuideData(List<PatrolBean> patrolBeans);//保存巡视路线数据
        void removeGuideData();
        boolean isGuideData(int position);//该巡视点是否在巡视路线中
        String getMapPatrolName(String name);
        void removeNoValidData();//移除不符合规则的数据
    }

    interface View extends BaseView {
        void showPatrolPointMap(List<PatrolBean> patrolBeans);//在地图上显示巡视点
        void addPatrolPoint(PatrolBean patrolBean);//地图上新增巡视点
        void setPatrolPosition(Position position);
        void saveSuccess();//巡视点保存成功
        void removePatrolItem(int position);//移除巡视点
        void addNotPatrolPoint();
        void showGuideData(List<PatrolBean> patrolBeans);
    }

}

