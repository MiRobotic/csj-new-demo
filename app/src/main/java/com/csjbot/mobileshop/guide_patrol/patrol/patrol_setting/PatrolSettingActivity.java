package com.csjbot.mobileshop.guide_patrol.patrol.patrol_setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.guide_patrol.patrol.adapter.PatrolLineListAdapter;
import com.csjbot.mobileshop.guide_patrol.patrol.bean.PatrolBean;
import com.csjbot.mobileshop.model.tcp.bean.Position;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.util.CheckOutUtil;
import com.csjbot.mobileshop.util.MD5;
import com.csjbot.mobileshop.util.MaxLengthWatcher;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.csjbot.mobileshop.util.StrUtil;
import com.csjbot.mobileshop.widget.OnRecyclerItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 孙秀艳 on 2019/1/19.
 */
@Route(path = BRouterPath.PATROL_SETTING)
public class PatrolSettingActivity extends BaseModuleActivity implements PatrolSettingContract.View{

    //巡视点
    @BindView(R.id.btn_get_position)
    Button btnGetPosition;//获取坐标点
    @BindView(R.id.et_navi_xpoint)
    EditText etNaviXPoint;//X坐标
    @BindView(R.id.et_navi_ypoint)
    EditText etNaviYPoint;//Y坐标
    @BindView(R.id.et_navi_zpoint)
    EditText etNaviZPoint;//Z坐标
    @BindView(R.id.et_navi_name)
    EditText etPatrolName;//导航点名称
    //巡视计划
    @BindView(R.id.rb_single)
    RadioButton rbSingle;//单次巡航
    @BindView(R.id.rb_circle)
    RadioButton rbCircle;//循环巡航
    //巡视路线
    @BindView(R.id.recyclerView_guide)
    RecyclerView recyclerView;//一键导览数据列表
    //地图
    @BindView(R.id.iv_map)
    ImageView ivMap;//地图图片
    @BindView(R.id.rl_map)
    RelativeLayout rl_map;
    //tab切换
    @BindView(R.id.btn_add_patrol)
    Button btnAddPatrol;//新增巡视点
    @BindView(R.id.btn_patrol_plan)
    Button btnPatrolPlan;//巡视计划
    @BindView(R.id.btn_patrol_line)
    Button btnPatrolLine;//巡视路线
    @BindView(R.id.btn_add_patrol_selected)
    Button btnAddPatrolSelected;//新增巡视点
    @BindView(R.id.btn_patrol_plan_selected)
    Button btnPatrolPlanSelected;//巡视计划
    @BindView(R.id.btn_patrol_line_selected)
    Button btnPatrolLineSelected;//巡视路线
    @BindView(R.id.patrol_tab_point)
    LinearLayout llPatrolPoint;//巡视点数据布局
    @BindView(R.id.patrol_tab_plan)
    LinearLayout llPatrolPlan;//巡视计划数据布局
    @BindView(R.id.patrol_tab_line)
    LinearLayout llPatrolLine;//巡视数据布局
    @BindView(R.id.ll_save_btn)
    LinearLayout llSaveBtn;//保存按钮布局
    @BindView(R.id.ll_save_delete_btn)
    LinearLayout llSaveDeleteBtn;//保存和删除按钮布局

    /*中文、英文、数字但不包括下划线等符号*/
    private String REGEX_USERNAME = "^[\\u4E00-\\u9FA5A-Za-z0-9]+$";
    private PatrolSettingContract.Presenter mPresenter;
    private List<KeyPointView> keyPointViews;//地图上所有点的集合
    private int index = -1;//地图上当前选中导航点的下标
    private PatrolLineListAdapter patrolLineListAdapter;//巡视路线列表适配器
    private List<PatrolBean> patrolBeans = new ArrayList<>();//一键导览列表
    private ItemTouchHelper mItemTouchHelper;//一键导览拖动
    private PatrolType patrolType = PatrolType.PATROLPOINT;//当前在哪个tab项

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        if (Constants.isPlus()) {
            return R.layout.vertical_activity_patrol_setting;
        } else {
            return R.layout.activity_patrol_setting;
        }
    }

    @Override
    public void init() {
        super.init();
        mTitleView.setCustomerLayoutGone();
        mTitleView.setSettingLayoutGone();
        CheckOutUtil.getInstance().verifyUseName(etPatrolName, this, REGEX_USERNAME);
        etPatrolName.addTextChangedListener(new MaxLengthWatcher(this, 8, etPatrolName, getString(R.string.patrol_name_limit_length)));
        mPresenter = new PatrolSettingPresenter(this);
        mPresenter.initView(this);
        keyPointViews = new ArrayList<>();
        mPresenter.getPatrolData();
        initRecycleView();
        initMap();
        initCheckBox();
        speak(getString(R.string.patrol_tip));
        setEditEnable(etPatrolName, true);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, true));
    }

    private void initPatrolPlan() {
        if (mPresenter.getPatrolPlan()) {
            rbSingle.setButtonDrawable(R.drawable.radio_btn_unselected);
            rbCircle.setButtonDrawable(R.drawable.radio_btn_selected);
        } else {
            rbSingle.setButtonDrawable(R.drawable.radio_btn_selected);
            rbCircle.setButtonDrawable(R.drawable.radio_btn_unselected);
        }
    }

    private void initCheckBox() {
        initPatrolPlan();
        rbSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSingle.setButtonDrawable(R.drawable.radio_btn_selected);
                rbCircle.setButtonDrawable(R.drawable.radio_btn_unselected);
            }
        });
        rbCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSingle.setButtonDrawable(R.drawable.radio_btn_unselected);
                rbCircle.setButtonDrawable(R.drawable.radio_btn_selected);
            }
        });
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        patrolLineListAdapter = new PatrolLineListAdapter(this);
        recyclerView.setAdapter(patrolLineListAdapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {}

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                mItemTouchHelper.startDrag(vh);
            }
        });
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            /**
             * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，如果是网格类RecyclerView则还应该多有LEFT和RIGHT
             * @param recyclerView
             * @param viewHolder
             * @return
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//                    final int swipeFlags = 0;
                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(patrolBeans, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(patrolBeans, i, i - 1);
                    }
                }
                patrolLineListAdapter.notifyItemMoved(fromPosition, toPosition);
//                naviGuideListAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                patrolBeans.remove(position);
                patrolLineListAdapter.setGuideList(patrolBeans);
                patrolLineListAdapter.notifyItemRemoved(position);
            }

            /**
             * 重写拖拽可用
             * @return
             */
            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            /**
             * 长按选中Item的时候开始调用
             *
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开的时候还原
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(0);
            }
        });

        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void initMap() {
        String mapPath = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.MAP_PATH);
        if (!TextUtils.isEmpty(mapPath)) {// 获取不到地图，提示用户添加地图
            Glide.with(this).load(mapPath).into(ivMap);
        }
    }

    public void setEditEnable(View view, boolean isEnable) {
        view.setFocusable(isEnable);
        view.setFocusableInTouchMode(isEnable);
        view.requestFocus();
    }

    @OnClick({R.id.btn_get_position, R.id.btn_save, R.id.btn_delete, R.id.btn_save_patrol_plan,
            R.id.btn_add_patrol, R.id.btn_patrol_plan, R.id.btn_patrol_line})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_position://获取当前位置
                getCurPosition();
                break;
//            case R.id.et_navi_name://添加和更新导航点名称
//                mPresenter.showEdittextDialog(getString(R.string.patrol_name), etPatrolName.getText().toString(), etPatrolName, 30);
//                break;
            case R.id.btn_save://导航点保存
                if (patrolType == PatrolType.PATROLPOINT) {
                    savePatrolData();
                } else if (patrolType == PatrolType.PATROLLINE) {
                    saveGuideAllData();
                }
                break;
            case R.id.btn_save_patrol_plan:
                if (patrolType == PatrolType.PATROLPLAN) {
                    savePatrolPlan();
                }
                break;
            case R.id.btn_delete://导航点删除
                if (patrolType == PatrolType.PATROLPOINT) {
                    removeNaviData();
                } else if (patrolType == PatrolType.PATROLLINE) {
                    removeGuideAllData();
                }
                break;
            case R.id.btn_add_patrol://切换设置导览点
                setTabTextAndBG(R.id.btn_add_patrol);
                break;
            case R.id.btn_patrol_plan://切换设置迎宾点
                setTabTextAndBG(R.id.btn_patrol_plan);
                initPatrolPlan();
                break;
            case R.id.btn_patrol_line://切换设置一键导览
                patrolBeans.clear();
                setTabTextAndBG(R.id.btn_patrol_line);
                mPresenter.getGuideData();
                break;
            default:
                break;
        }
    }

    private void savePatrolPlan() {
        boolean isCircle = rbCircle.isChecked();
        mPresenter.savePatrolPlan(isCircle);
    }

    /**
     * 获取当前位置
     */
    private void getCurPosition() {
        mPresenter.getPosition();
    }

    /**
     * 保存导航点数据
     */
    private void savePatrolData() {
        String msg = naviValidate();
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            return;
        }
        if (index == -1) {
            Toast.makeText(this, R.string.select_patrol_point, Toast.LENGTH_LONG).show();
            return;
        }
        if (keyPointViews != null && keyPointViews.size() > 0) {
            KeyPointView keyPointView = keyPointViews.get(index);
            PatrolBean patrolBean = null;
            if (keyPointView.getTag() == null) {
                patrolBean = new PatrolBean();
                patrolBean.setId(MD5.getRandomString(10));
            } else {
                patrolBean = (PatrolBean) keyPointView.getTag();
            }
            Position position = new Position();
            position.setX(etNaviXPoint.getText().toString().replace("X:", ""));
            position.setY(etNaviYPoint.getText().toString().replace("Y:", ""));
            position.setRotation(etNaviZPoint.getText().toString().replace("YAW:", ""));
            patrolBean.setPos(position);
            patrolBean.setName(etPatrolName.getText().toString());
            patrolBean.setTranslationX(keyPointView.getTranslationX());
            patrolBean.setTranslationY(keyPointView.getTranslationY());
            patrolBean.left = keyPointView.getLeft();
            patrolBean.right = keyPointView.getRight();
            patrolBean.top = keyPointView.getTop();
            patrolBean.bottom = keyPointView.getBottom();
            keyPointView.setTag(patrolBean);
            keyPointView.setName(mPresenter.getMapPatrolName(etPatrolName.getText().toString()));
            keyPointView.setIsTouch(true);
            keyPointView.setIsBound(true);
            keyPointView.setBound(900, 570);
            keyPointView.setIv_maker(R.drawable.map_2maker);
            keyPointView.layout(patrolBean.left, patrolBean.top, patrolBean.right, patrolBean.bottom);
            final PatrolBean patrolBean1 = patrolBean;
            keyPointView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (patrolType == PatrolType.PATROLPOINT) {
                        index = mPresenter.getIndexByData(patrolBean1);
                        showPatrolData(patrolBean1);
                    } else if (patrolType == PatrolType.PATROLLINE) {
                        savePatrolLineData(patrolBean1);
                    }
                }
            });
            mPresenter.savePatrolPointByPosition(patrolBean1, index);
        }
    }

    /**
     * 移除导航数据
     */
    private void removeNaviData() {
        if (keyPointViews == null || keyPointViews.size() <= 0) {
            Toast.makeText(this, R.string.no_patrol_data, Toast.LENGTH_LONG).show();
            return;
        }
        if (index == -1) {
            Toast.makeText(this, R.string.select_patrol_data, Toast.LENGTH_LONG).show();
            return;
        } else {
            PatrolBean patrolBean = (PatrolBean) keyPointViews.get(index).getTag();
            if (StrUtil.isBlank(patrolBean.getName())) {
                Toast.makeText(this, R.string.select_patrol_data, Toast.LENGTH_LONG).show();
                return;
            }
        }
        mPresenter.removePatrolPointByPosition(index);
    }

    /**
     * 导航点有效性检测
     */
    private String naviValidate() {
        String msg = null;
        if (StrUtil.isBlank(etPatrolName.getText().toString())) {
            msg = getString(R.string.input_patrol_name);
        } else if (StrUtil.isBlank(etNaviXPoint.getText().toString()) || StrUtil.isBlank(etNaviZPoint.getText().toString())
                || StrUtil.isBlank(etNaviZPoint.getText().toString())) {
            msg = getString(R.string.plz_get_position);
        }
        return msg;
    }

    private void saveGuideAllData() {
        if (patrolBeans == null || patrolBeans.size() <= 0) {
            Toast.makeText(PatrolSettingActivity.this, R.string.plz_add_patrol_point, Toast.LENGTH_SHORT).show();
            return;
        } else if (patrolBeans != null && patrolBeans.size() < 2) {
            Toast.makeText(PatrolSettingActivity.this, R.string.patrol_line_limit, Toast.LENGTH_SHORT).show();
        } else if (invalidatePatrolLine()) {//true 无效
            Toast.makeText(PatrolSettingActivity.this, R.string.same_point_continue, Toast.LENGTH_SHORT).show();
            return;
        } else {
            mPresenter.saveGuideData(patrolBeans);
        }
    }

    private boolean invalidatePatrolLine() {
        boolean isValidate = false;
        if (patrolBeans.size() == 1) {
            isValidate = false;
        } else {
            for (int i=1; i<patrolBeans.size(); i++) {
                PatrolBean prePoint = patrolBeans.get(i-1);
                PatrolBean curPoint = patrolBeans.get(i);
                if (prePoint.getName().equals(curPoint.getName()) && prePoint.getPos().getX().equals(curPoint.getPos().getX())
                        && prePoint.getPos().getY().equals(curPoint.getPos().getY()) && prePoint.getPos().getRotation().equals(curPoint.getPos().getRotation())
                        && prePoint.getTop() == curPoint.getTop() && prePoint.getBottom() == curPoint.getBottom()
                        && prePoint.getLeft() == curPoint.getLeft() && prePoint.getRight() == curPoint.getRight()
                        && prePoint.getTranslationX() == curPoint.getTranslationX() && prePoint.getTranslationY() == curPoint.getTranslationY()) {
                    isValidate = true;
                    break;
                }
            }
        }
        return isValidate;
    }

    private void removeGuideAllData() {
        mPresenter.removeGuideData();
    }

    @Override
    public void showPatrolPointMap(List<PatrolBean> patrolBeans) {
        if (patrolBeans == null) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                mPresenter.addPatrolPoint();
            }, 500);
            return;
        }
        for (int i = 0; i < patrolBeans.size(); i++) {
            PatrolBean patrolBean = patrolBeans.get(i);
            KeyPointView keyPointView = new KeyPointView(this);
            keyPointView.setIsTouch(true);//标注点可以触摸
            keyPointView.setIsBound(true);//标注点需要设置边界
            keyPointView.setBound(900, 570);
            keyPointView.layout(patrolBean.left, patrolBean.top, patrolBean.right, patrolBean.bottom);
            keyPointView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (patrolType == PatrolType.PATROLPOINT) {
                        index = mPresenter.getIndexByData(patrolBean);
                        showPatrolData(patrolBean);
                    } else if (patrolType == PatrolType.PATROLLINE) {
                        savePatrolLineData(patrolBean);
                    }
                }
            });
            keyPointView.setName(mPresenter.getMapPatrolName(patrolBean.getName()));
            keyPointView.setTag(patrolBean);
            /* 设置平移坐标 */
            keyPointView.setTranslationX(patrolBean.getTranslationX());
            keyPointView.setTranslationY(patrolBean.getTranslationY());
            keyPointView.setIv_maker(R.drawable.map_2maker);
            rl_map.addView(keyPointView);
            keyPointViews.add(keyPointView);
        }
        mPresenter.addPatrolPoint();
    }

    /**
     * 展示巡视数据
     */
    private void showPatrolData(PatrolBean patrolBean) {
        if (StrUtil.isBlank(patrolBean.getName()) || patrolBean.getPos() == null || StrUtil.isBlank(patrolBean.getPos().getX())
                || StrUtil.isBlank(patrolBean.getPos().getY()) || StrUtil.isBlank(patrolBean.getPos().getRotation())) {
            clearNaviData();
            return;
        }
        etNaviXPoint.setText(patrolBean.getPos().getX());
        etNaviYPoint.setText(patrolBean.getPos().getY());
        etNaviZPoint.setText(patrolBean.getPos().getRotation());
        etPatrolName.setText(patrolBean.getName());
    }

    /**
     * 保存一键导览数据
     */
    private void savePatrolLineData(PatrolBean patrolBean) {
        if (StrUtil.isNotBlank(patrolBean.getName())) {
            if (patrolBeans != null && patrolBeans.size() > 0) {
                if (patrolBean.getName().equals(patrolBeans.get(patrolBeans.size()-1).getName())) {
                    Toast.makeText(PatrolSettingActivity.this, R.string.same_point_continue, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            patrolBeans.add(patrolBean);
            patrolLineListAdapter.setGuideList(patrolBeans);
        } else {
            Toast.makeText(PatrolSettingActivity.this, R.string.no_patrol_point_setting, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 清除巡视点界面数据
     */
    private void clearNaviData() {
        etNaviXPoint.setText("");
        etNaviYPoint.setText("");
        etNaviZPoint.setText("");
        etPatrolName.setText("");
    }

    @Override
    public void addPatrolPoint(PatrolBean patrolBean) {
        KeyPointView keyPointView = new KeyPointView(this);
        keyPointView.setTag(patrolBean);
        keyPointView.setName("");
        keyPointView.setIv_maker(R.drawable.map_2maker);
        rl_map.addView(keyPointView);
        keyPointViews.add(keyPointView);
        keyPointView.setIsTouch(true);
        keyPointView.setIsBound(true);
        keyPointView.setBound(900, 570);
        index = keyPointViews.size() - 1;
        keyPointView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = keyPointViews.size() - 1;
                clearNaviData();
            }
        });
        clearNaviData();
    }

    @Override
    public void setPatrolPosition(Position position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (position != null && patrolType == PatrolType.PATROLPOINT) {
                    etNaviXPoint.setText(position.getX());
                    etNaviYPoint.setText(position.getY());
                    etNaviZPoint.setText(position.getRotation());
                }
            }
        });
    }

    @Override
    public void saveSuccess() {
        Toast.makeText(this, R.string.save_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void removePatrolItem(int position) {
        Toast.makeText(PatrolSettingActivity.this, R.string.delete_success, Toast.LENGTH_LONG).show();
        rl_map.removeView(keyPointViews.get(position));
        keyPointViews.remove(position);
        if (keyPointViews != null && keyPointViews.size() > 0) {
            index = keyPointViews.size() - 1;
        } else {
            index = -1;
        }
        clearNaviData();
    }

    @Override
    public void addNotPatrolPoint() {
        index = keyPointViews.size() - 1;
        clearNaviData();
    }

    @Override
    public void showGuideData(List<PatrolBean> patrolBeanList) {
        patrolBeans.clear();
        if (patrolBeanList != null && patrolBeanList.size() > 0) {
            patrolBeans.addAll(patrolBeanList);
        }
        patrolLineListAdapter.setGuideList(patrolBeanList);
    }

    /**
     * 切换tab时，tab项字体和背景设置
     */
    private void setTabTextAndBG(int id) {
        if (id == R.id.btn_add_patrol) {//巡视点
            patrolType = PatrolType.PATROLPOINT;
            btnAddPatrol.setVisibility(View.GONE);
            btnAddPatrolSelected.setVisibility(View.VISIBLE);
            btnPatrolPlan.setVisibility(View.VISIBLE);
            btnPatrolPlanSelected.setVisibility(View.GONE);
            btnPatrolLine.setVisibility(View.VISIBLE);
            btnPatrolLineSelected.setVisibility(View.GONE);
            llPatrolPoint.setVisibility(View.VISIBLE);
            llPatrolPlan.setVisibility(View.GONE);
            llPatrolLine.setVisibility(View.GONE);
            llSaveBtn.setVisibility(View.GONE);
            llSaveDeleteBtn.setVisibility(View.VISIBLE);
        } else if (id == R.id.btn_patrol_plan) {//巡视计划
            patrolType = PatrolType.PATROLPLAN;
            btnAddPatrol.setVisibility(View.VISIBLE);
            btnAddPatrolSelected.setVisibility(View.GONE);
            btnPatrolPlan.setVisibility(View.GONE);
            btnPatrolPlanSelected.setVisibility(View.VISIBLE);
            btnPatrolLine.setVisibility(View.VISIBLE);
            btnPatrolLineSelected.setVisibility(View.GONE);
            llPatrolPoint.setVisibility(View.GONE);
            llPatrolPlan.setVisibility(View.VISIBLE);
            llPatrolLine.setVisibility(View.GONE);
            llSaveBtn.setVisibility(View.VISIBLE);
            llSaveDeleteBtn.setVisibility(View.GONE);
        } else if (id == R.id.btn_patrol_line) {//巡视路线
            patrolType = PatrolType.PATROLLINE;
            btnAddPatrol.setVisibility(View.VISIBLE);
            btnAddPatrolSelected.setVisibility(View.GONE);
            btnPatrolPlan.setVisibility(View.VISIBLE);
            btnPatrolPlanSelected.setVisibility(View.GONE);
            btnPatrolLine.setVisibility(View.GONE);
            btnPatrolLineSelected.setVisibility(View.VISIBLE);
            llPatrolPoint.setVisibility(View.GONE);
            llPatrolPlan.setVisibility(View.GONE);
            llPatrolLine.setVisibility(View.VISIBLE);
            llSaveBtn.setVisibility(View.GONE);
            llSaveDeleteBtn.setVisibility(View.VISIBLE);
        }
    }
}
