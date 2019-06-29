package com.csjbot.mobileshop.feature.product.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.product.adapter.SelectClothAdapter;
import com.csjbot.mobileshop.feature.product.bean.SelectClothBean;
import com.csjbot.mobileshop.global.Constants;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author ShenBen
 * @date 2018/11/12 18:20
 * @email 714081644@qq.com
 */

public class SelectClothPopup extends BasePopupWindow implements View.OnClickListener {
    private EditText etGoodsCode;
    private RecyclerView rvSeason;
    private RecyclerView rvColor;
    private EditText etMinPrice;
    private EditText etMaxPrice;
    private Button btnReset;
    private Button btnSure;

    private List<SelectClothBean> mColorList;
    private List<SelectClothBean> mSeasonList;
    private SelectClothAdapter mColorAdapter;
    private SelectClothAdapter mSeasonAdapter;

    private Context mContext;
    private OnSelectClothDetailListener mListener;
    private String mGoodsCode = "";                  //商品编号
    private String mColor = "";                      //商品颜色
    private String mSeason = "";                     //季节
    private double mMinPrice = 0.0;                  //最低价
    private double mMaxPrice = Integer.MAX_VALUE;    //最高价

    private int mCurrentColorPosition = -1;          //当前颜色列表选中的位置
    private int mCurrentSeasonPosition = -1;         //当前季节列表选中的位置

    public SelectClothPopup(Context context) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setAlignBackground(true);
        mContext = context;
        etGoodsCode = findViewById(R.id.et_goos_code);
        rvColor = findViewById(R.id.rv_color);
        rvSeason = findViewById(R.id.rv_season);
        etMinPrice = findViewById(R.id.et_min_price);
        etMaxPrice = findViewById(R.id.et_max_price);
        btnReset = findViewById(R.id.btn_reset);
        btnSure = findViewById(R.id.btn_sure);

        btnReset.setOnClickListener(this);
        btnSure.setOnClickListener(this);

        etGoodsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mGoodsCode = s.toString();
            }
        });
        init();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_select_cloth);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        return animation;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f);
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        return animation;
    }

    private void init() {
        mColorList = new ArrayList<>();
        mSeasonList = new ArrayList<>();

        //可以根据大小屏动态设置不同的布局
        if (Constants.isPlus()) {
            mColorAdapter = new SelectClothAdapter(R.layout.item_select_cloth_vertical, mColorList);
            mSeasonAdapter = new SelectClothAdapter(R.layout.item_select_cloth_vertical, mSeasonList);
        } else {
            mColorAdapter = new SelectClothAdapter(R.layout.item_select_cloth, mColorList);
            mSeasonAdapter = new SelectClothAdapter(R.layout.item_select_cloth, mSeasonList);
        }

        GridLayoutManager typeManager = new GridLayoutManager(mContext, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        GridLayoutManager seasonManager = new GridLayoutManager(mContext, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        rvColor.setLayoutManager(typeManager);
        rvSeason.setLayoutManager(seasonManager);
        rvColor.setHasFixedSize(true);
        rvSeason.setHasFixedSize(true);

        rvColor.setAdapter(mColorAdapter);
        rvSeason.setAdapter(mSeasonAdapter);

        mColorAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mCurrentColorPosition == position) {
                mColorList.get(position).setChecked(false);
                mColorAdapter.notifyItemChanged(position, "color");
                mColor = "";
                mCurrentColorPosition = -1;
                return;
            }
            SelectClothBean bean = mColorList.get(position);
            mColor = bean.getType();
            bean.setChecked(true);
            mColorAdapter.notifyItemChanged(position, "color");
            if (mCurrentColorPosition != -1) {
                mColorList.get(mCurrentColorPosition).setChecked(false);
                mColorAdapter.notifyItemChanged(mCurrentColorPosition, "color");
            }
            mCurrentColorPosition = position;
        });

        mSeasonAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mCurrentSeasonPosition == position) {
                mSeasonList.get(position).setChecked(false);
                mSeasonAdapter.notifyItemChanged(position, "season");
                mSeason = "";
                mCurrentSeasonPosition = -1;
                return;
            }
            SelectClothBean bean = mSeasonList.get(position);
            mSeason = bean.getType();
            bean.setChecked(true);
            mSeasonAdapter.notifyItemChanged(position, "season");
            if (mCurrentSeasonPosition != -1) {
                mSeasonList.get(mCurrentSeasonPosition).setChecked(false);
                mSeasonAdapter.notifyItemChanged(mCurrentSeasonPosition, "season");
            }
            mCurrentSeasonPosition = position;
        });

    }

    /**
     * 设置颜色集合
     *
     * @param list
     */
    public void setColorList(@NonNull List<SelectClothBean> list) {
        mColorList.clear();
        mColorList.addAll(list);
        mColorAdapter.setNewData(mColorList);
    }

    /**
     * 设置版型集合
     *
     * @param list
     */
    public void setSeasonList(@NonNull List<SelectClothBean> list) {
        mSeasonList.clear();
        mSeasonList.addAll(list);
        mSeasonAdapter.setNewData(mSeasonList);
    }

    public void setOnSelectClothDetailListener(OnSelectClothDetailListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                if (mCurrentColorPosition != -1) {
                    mColorList.get(mCurrentColorPosition).setChecked(false);
                    mColorAdapter.notifyItemChanged(mCurrentColorPosition, "color");
                }
                if (mCurrentSeasonPosition != -1) {
                    mSeasonList.get(mCurrentSeasonPosition).setChecked(false);
                    mSeasonAdapter.notifyItemChanged(mCurrentSeasonPosition, "season");
                }
                etGoodsCode.setText(null);
                etMinPrice.setText(null);
                etMaxPrice.setText(null);
                mCurrentColorPosition = -1;
                mCurrentSeasonPosition = -1;
                mGoodsCode = "";
                mColor = "";
                mSeason = "";
                break;
            case R.id.btn_sure:
                String maxPrice = etMaxPrice.getText().toString().trim();
                String minPrice = etMinPrice.getText().toString().trim();
                mMinPrice = 0.0;
                mMaxPrice = Integer.MAX_VALUE;
                if (!TextUtils.isEmpty(minPrice)) {
                    mMinPrice = Double.parseDouble(minPrice);
                }
                if (!TextUtils.isEmpty(maxPrice)) {
                    mMaxPrice = Double.parseDouble(maxPrice);
                }
                if (mListener != null) {
                    mListener.selectCloth(mGoodsCode, mColor, mSeason, mMinPrice, mMaxPrice);
                }
                dismiss();
                break;
        }
    }

    public interface OnSelectClothDetailListener {
        /**
         * 传递筛选条件信息
         *
         * @param goodsCode 商品编号
         * @param color     颜色
         * @param season    季节
         * @param minPrice  最低价
         * @param maxPrice  最高价
         */
        void selectCloth(String goodsCode, String color, String season, double minPrice, double maxPrice);
    }
}
