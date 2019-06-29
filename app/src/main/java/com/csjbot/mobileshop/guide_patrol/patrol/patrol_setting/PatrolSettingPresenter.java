package com.csjbot.mobileshop.guide_patrol.patrol.patrol_setting;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.csjbot.coshandler.listener.OnPositionListener;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.guide_patrol.patrol.bean.PatrolBean;
import com.csjbot.mobileshop.guide_patrol.patrol.dialog.EditDialog;
import com.csjbot.mobileshop.guide_patrol.patrol.dialog.RecyclerViewDialog;
import com.csjbot.mobileshop.model.tcp.bean.Position;
import com.csjbot.mobileshop.model.tcp.chassis.IChassis;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.util.BlackgagaLogger;
import com.csjbot.mobileshop.util.FileUtil;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.MD5;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.mobileshop.util.StrUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class PatrolSettingPresenter implements PatrolSettingContract.Presenter{

    private PatrolSettingContract.View view;
    private List<PatrolBean> navis;
    private IChassis chassis;
    private Context mContext;
    private RecyclerViewDialog recyclerViewDialog;//文本列表对话框
    private EditDialog editDialog;//文本编辑框对话框

    public PatrolSettingPresenter(Context context) {
        mContext = context;
        chassis = ServerFactory.getChassisInstance();
        navis = new ArrayList<>();
        /* 设置位置回调事件 */
        RobotManager.getInstance().addListener(new MyOnPositionListener(this));
        recyclerViewDialog = new RecyclerViewDialog(mContext);
        editDialog = new EditDialog(mContext);
    }

    @Override
    public PatrolSettingContract.View getView() {
        return view;
    }

    @Override
    public void initView(PatrolSettingContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if (view != null) {
            view = null;
        }
    }

    @Override
    public void getPosition() {
        if (RobotManager.getInstance().getConnectState()) {
            chassis.getPosition();
        } else {
            Toast.makeText(mContext, "底层未连接", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void addPatrolPoint() {
        if (navis == null) {
            navis = new ArrayList<PatrolBean>();
        }
        int index = -1;
        if (navis.size() > 0) {
            for (int i = 0; i < navis.size(); i++) {
                if (StrUtil.isBlank(navis.get(i).getName())) {
                    index = i;
                    break;
                }
            }
        }
        if (index != -1) {
            view.addNotPatrolPoint();
            return;
        }
        SharedPreUtil.putString(SharedKey.PATROL_POINT_NAME, SharedKey.PATROL_POINT_KEY, GsonUtils.objectToJson(navis));
        PatrolBean patrolBean = new PatrolBean();
        patrolBean.setId(MD5.getRandomString(10));
        navis.add(patrolBean);
        view.addPatrolPoint(patrolBean);
    }

    @Override
    public int getIndexByData(PatrolBean naviBean) {
        int position = 0;
        String json = SharedPreUtil.getString(SharedKey.PATROL_POINT_NAME, SharedKey.PATROL_POINT_KEY);
        List<PatrolBean> naviList = GsonUtils.jsonToObject(json, new TypeToken<List<PatrolBean>>() {
        }.getType());
        for (int i = 0; i < naviList.size(); i++) {
            if (naviList != null && naviList.size() > 0) {
                PatrolBean bean = naviList.get(i);
                if (StrUtil.isNotBlank(naviBean.getName()) && naviBean.getName().equals(bean.getName()) &&
                        bean.getTranslationX() == naviBean.getTranslationX() && bean.getTranslationY() == naviBean.getTranslationY()) {
                    position = i;
                }
            }
        }
        return position;
    }

    /** 根据索引值保存导航点*/
    @Override
    public void savePatrolPointByPosition(PatrolBean naviBean, int position) {
        try {
            navis.set(position, naviBean);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreUtil.putString(SharedKey.PATROL_POINT_NAME, SharedKey.PATROL_POINT_KEY, GsonUtils.objectToJson(navis));
        BlackgagaLogger.debug("保存成功:savePosition" + GsonUtils.objectToJson(navis));
        view.saveSuccess();
        updateGuideData(naviBean);
        addPatrolPoint();
    }

    @Override
    public void showEdittextDialog(String title, String content, EditText editText, int length) {
        editDialog.setTitle(title);
        editDialog.setContent(content);
        editDialog.setLength(length);
        editDialog.setStart();
        editDialog.setListener(new EditDialog.OnDialogClickListener() {
            @Override
            public void yes(String content) {
                editText.setText(content);
                editDialog.dismiss();
            }

            @Override
            public void no() {
                editDialog.dismiss();
            }
        });
        editDialog.show();
    }

    @Override
    public void showRecyclerViewDialog(String title, Button editText, Constants.ResourceSuffix.suffix suffix) {
        recyclerViewDialog.setSuffix(suffix);
        recyclerViewDialog.setTitle(title);
        recyclerViewDialog.startSearch();
        recyclerViewDialog.setListener(new RecyclerViewDialog.OnDialogClickListener() {
            @Override
            public void yes(String path) {
                if (StrUtil.isNotBlank(path) && !FileUtil.isEmptyFile(Constants.NAVI_PATH + path)) {
                    editText.setText(path);
                    recyclerViewDialog.dismiss();
                } else {
                    Toast.makeText(mContext, "请选择要使用的文件", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void no() {
                recyclerViewDialog.dismiss();
            }
        });
        recyclerViewDialog.show();
    }

    @Override
    public void removePatrolPointByPosition(int position) {
        if (isGuideData(position)) {
            removeGuideData(navis.get(position));
        }
        if (navis != null && navis.size() > position) {
            navis.remove(position);
            SharedPreUtil.putString(SharedKey.PATROL_POINT_NAME, SharedKey.PATROL_POINT_KEY, GsonUtils.objectToJson(navis));
            view.removePatrolItem(position);
        }
    }

    @Override
    public boolean getPatrolPlan() {
        return SharedPreUtil.getBoolean(SharedKey.PATROL_PLAN_NAME, SharedKey.PATROL_PLAN_KEY, false);
    }

    @Override
    public void savePatrolPlan(boolean isCircle) {
        SharedPreUtil.putBoolean(SharedKey.PATROL_PLAN_NAME, SharedKey.PATROL_PLAN_KEY, isCircle);
        view.saveSuccess();
    }

    private void removeGuideData(PatrolBean naviBean) {
        String json = SharedPreUtil.getString(SharedKey.PATROL_LINE_NAME, SharedKey.PATROL_LINE_KEY);
        List<PatrolBean> naviBeanList = GsonUtils.jsonToObject(json, new TypeToken<List<PatrolBean>>() {
        }.getType());
        if (naviBeanList != null && naviBeanList.size() > 0) {
            for (int i = naviBeanList.size() - 1; i >= 0; i--) {
                if (naviBeanList.get(i).getId().equals(naviBean.getId())) {
                    naviBeanList.remove(i);
                }
            }
        }
        SharedPreUtil.putString(SharedKey.PATROL_LINE_NAME, SharedKey.PATROL_LINE_KEY, GsonUtils.objectToJson(naviBeanList));
    }

    @Override
    public void getGuideData() {
        String json = SharedPreUtil.getString(SharedKey.PATROL_LINE_NAME, SharedKey.PATROL_LINE_KEY);
        List<PatrolBean> naviBeanList = GsonUtils.jsonToObject(json, new TypeToken<List<PatrolBean>>() {
        }.getType());
        view.showGuideData(naviBeanList);
    }

    @Override
    public void saveGuideData(List<PatrolBean> naviBeanList) {
        SharedPreUtil.putString(SharedKey.PATROL_LINE_NAME, SharedKey.PATROL_LINE_KEY, GsonUtils.objectToJson(naviBeanList));
        view.saveSuccess();
        BlackgagaLogger.debug("保存成功:" + naviBeanList.size());
    }

    @Override
    public void removeGuideData() {
        String json = SharedPreUtil.getString(SharedKey.PATROL_LINE_NAME, SharedKey.PATROL_LINE_KEY);
        List<PatrolBean> naviBeanList = GsonUtils.jsonToObject(json, new TypeToken<List<PatrolBean>>() {
        }.getType());
        if (naviBeanList != null && naviBeanList.size() > 0) {
            naviBeanList.clear();
        } else {
            naviBeanList = new ArrayList<>();
        }
        view.showGuideData(naviBeanList);
        SharedPreUtil.putString(SharedKey.PATROL_LINE_NAME, SharedKey.PATROL_LINE_KEY, GsonUtils.objectToJson(naviBeanList));
    }

    @Override
    public boolean isGuideData(int position) {
        String json = SharedPreUtil.getString(SharedKey.PATROL_LINE_NAME, SharedKey.PATROL_LINE_KEY);
        List<PatrolBean> naviBeanList = GsonUtils.jsonToObject(json, new TypeToken<List<PatrolBean>>() {
        }.getType());
        boolean isExist = false;
        if (naviBeanList != null && naviBeanList.size() > 0 && navis != null && navis.size() > position) {
            PatrolBean naviBean = navis.get(position);
            for (int i = 0; i < naviBeanList.size(); i++) {
                if (naviBeanList.get(i).getId().equals(naviBean.getId())) {
                    isExist = true;
                }
            }
        }
        BlackgagaLogger.debug("sunxy isGuideData isExist" + isExist);
        return isExist;
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
    public void removeNoValidData() {
        int index = -1;
        if (navis.size() > 0) {
            for (int i = 0; i < navis.size(); i++) {
                if (StrUtil.isBlank(navis.get(i).getName())) {
                    index = i;
                    break;
                }
            }
        }
        if (index != -1) {
            navis.remove(index);
            SharedPreUtil.putString(SharedKey.PATROL_POINT_NAME, SharedKey.PATROL_POINT_KEY, GsonUtils.objectToJson(navis));
            return;
        }
    }

    /**
     * 当保存导航数据的时候，需要更新一键导引数据
     */
    private void updateGuideData(PatrolBean patrolBean) {
        String json = SharedPreUtil.getString(SharedKey.PATROL_LINE_NAME, SharedKey.PATROL_LINE_KEY);
        List<PatrolBean> patrolBeanList = GsonUtils.jsonToObject(json, new TypeToken<List<PatrolBean>>() {
        }.getType());
        boolean isUpdate = false;
        if (patrolBeanList != null && patrolBeanList.size() > 0) {
            for (int i = 0; i < patrolBeanList.size(); i++) {
                if (patrolBeanList.get(i).getId().equals(patrolBean.getId())) {
                    patrolBeanList.set(i, patrolBean);
                    isUpdate = true;
                }
            }
            if (isUpdate) {
                SharedPreUtil.putString(SharedKey.PATROL_LINE_NAME, SharedKey.PATROL_LINE_KEY, GsonUtils.objectToJson(patrolBeanList));
            }
        }
    }

    Handler mHandle = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            /* 显示地图 */
            view.showPatrolPointMap(navis);
        }
    };

    /**
     * 此事件为获取到当前位置而触发
     * 由于此事件需要设置到RobotManager,而RobotManager是静态对象,为避免释放不掉Presenter造成内存泄露
     * 所以将Presenter对象以弱引用的方式传入
     */
    private static final class MyOnPositionListener implements OnPositionListener {

        WeakReference<PatrolSettingPresenter> reference;

        public MyOnPositionListener(PatrolSettingPresenter value) {
            this.reference = new WeakReference<>(value);
        }

        @Override
        public void positionInfo(String json) {
            BlackgagaLogger.debug("sunxy"+json);
            reference.get().view.setPatrolPosition(GsonUtils.jsonToObject(json, Position.class));
        }

        @Override
        public void moveResult(String json) {

        }

        @Override
        public void moveToResult(String json) {

        }

        @Override
        public void cancelResult(String json) {

        }
    }
}
