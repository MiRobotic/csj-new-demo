package com.csjbot.mobileshop.guide_patrol.widget;

import android.util.Log;

import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.guide_patrol.patrol.bean.PatrolBean;
import com.csjbot.mobileshop.localbean.NaviBean;
import com.csjbot.mobileshop.model.tcp.bean.Position;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class NaviTaskUtils {
    public volatile static NaviBean naviBean;//单点导览任务
    public volatile static Position position;//迎宾点位置信息
    public volatile static int curCircleIndex;//一键导览当前正在前往第几个点
    public volatile static boolean isNaviSingle = true;//当前导览模式  true 单次导航 false 循环导航
    public volatile static int naviStatus = -1;//单点导览 0途中 1到点 2返回
    public volatile static int circleStatus1 = -1;//一键导览 0途中 1到点 2返回
    public volatile static List<PatrolBean> naviBeans;//正在使用的一键导览点

    public static int getCircleStatus() {
        return circleStatus1;
    }

    public static void setCircleStatus(int circleStatus) {
        circleStatus1 = circleStatus;
    }

    /**
     * 设置当前单点导览任务
     */
    public static void setTaskBean(NaviBean bean) {
        naviBean = bean;
    }

    /**
     * 获取当前单点导览任务
     */
    public static NaviBean getTaskBean() {
        return naviBean;
    }

    /** 是否存在迎宾点*/
    public static boolean isExistYingbinPoint() {
        String j = SharedPreUtil.getString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY);
        List<Position> positionList = GsonUtils.jsonToObject(j, new TypeToken<List<Position>>() {
        }.getType());
        if (positionList != null && positionList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /** 获取迎宾点位置*/
    public static Position getWelcomePos() {
        String j = SharedPreUtil.getString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY);
        List<Position> positionList = GsonUtils.jsonToObject(j, new TypeToken<List<Position>>() {
        }.getType());
        Position position = null;
        if (positionList != null && positionList.size() > 0) {
            position = positionList.get(0);
        }
        return position;
    }

    /** 获取正在导览第几个导览点*/
    public static int getCurCircleIndex() {
        return curCircleIndex;
    }

    /** 设置正在导览第几个导览点*/
    public static void setCurCircleIndex(int curCircleIndex) {
        NaviTaskUtils.curCircleIndex = curCircleIndex;
    }

    /** 是否是单点导航*/
    public static boolean isNaviSingle() {
        boolean isCircle = SharedPreUtil.getBoolean(SharedKey.PATROL_PLAN_NAME, SharedKey.PATROL_PLAN_KEY, false);
        return !isCircle;
    }

    /** 设置导航方式*/
    public static void setNaviSingle(boolean isNaviSingle) {
        NaviTaskUtils.isNaviSingle = isNaviSingle;
    }

    /** 获取一键导览*/
    public static List<PatrolBean> getNaviList() {
        String json = SharedPreUtil.getString(SharedKey.PATROL_LINE_NAME, SharedKey.PATROL_LINE_KEY);
        naviBeans = GsonUtils.jsonToObject(json, new TypeToken<List<PatrolBean>>() {
        }.getType());
        return naviBeans;
    }

}

