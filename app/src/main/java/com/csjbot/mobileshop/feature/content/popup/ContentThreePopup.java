package com.csjbot.mobileshop.feature.content.popup;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.content.adapter.ContentThreePopupAdapter;
import com.csjbot.mobileshop.feature.content.bean.ChildContentLevelThreeBean;
import com.csjbot.mobileshop.global.Constants;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author ShenBen
 * @date 2018/10/20 15:12
 * @email 714081644@qq.com
 */
public class ContentThreePopup extends BasePopupWindow {
    private RecyclerView mRvChild;
    private List<ChildContentLevelThreeBean.ChildrenBean> mList;
    private ContentThreePopupAdapter mAdapter;
    private OnItemClickListener mListener;
    private int mLocation = 1;//显示位置
    private int mCheckedIndex = 0;//选择的位置

    public ContentThreePopup(Context context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        callInit(context, isPlus() ? 237 : 237, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout root = findViewById(R.id.fl_root);
        root.setBackgroundColor(Color.parseColor(Constants.SceneColor.sContentPopupItemUnSelectedBg));
        mRvChild = findViewById(R.id.rv_popup_child);

        setBackgroundColor(Color.TRANSPARENT);
        mList = new ArrayList<>();
        if (isPlus()) {
            mAdapter = new ContentThreePopupAdapter(R.layout.item_content_three_popup_vertical);
        } else {
            mAdapter = new ContentThreePopupAdapter(R.layout.item_content_three_popup_vertical);
        }
        mRvChild.setLayoutManager(new LinearLayoutManager(context));
        mRvChild.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mListener != null) {
                if (mCheckedIndex != position) {
                    mList.get(mCheckedIndex).setChecked(false);
                    mList.get(position).setChecked(true);
                    mListener.onItemClick(mList.get(position).getContentUrl(), mList.get(position).getSpeakWords(), position);
                }
            }
            dismiss();
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(isPlus() ? R.layout.popup_content_three_vertical : R.layout.popup_content_three_vertical);
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

    @Override
    public void showPopupWindow(View anchorView) {
        int offsetX;
        int offsetY = 5;
        switch (mLocation) {
            case 1:
                offsetX = 6;
                break;
            case 2:
                offsetX = 266;
                break;
            case 3:
                offsetX = 526;
                break;
            case 4:
                offsetX = 786;
                break;
            default:
                offsetX = 6;
                break;
        }
        setOffsetX(offsetX);
        setOffsetY(offsetY);
        super.showPopupWindow(anchorView);
    }

    /**
     * 设置下拉列表默认选中的位置
     *
     * @param index
     */
    public void setCheckedIndex(int index) {
        mCheckedIndex = index;
    }

    /**
     * 添加显示的数据
     *
     * @param list
     */
    public void setNewData(List<ChildContentLevelThreeBean.ChildrenBean> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.setNewData(mList);
    }

    /**
     * 显示PopupWindow位置
     *
     * @param location 位置
     */
    public void setShowPopupLocation(int location) {
        mLocation = location;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String data, String word, int position);
    }

    /**
     * 判断是否是大屏
     *
     * @return
     */
    private boolean isPlus() {
        return Constants.isPlus();
    }
}
