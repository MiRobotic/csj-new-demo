package com.csjbot.mobileshop.feature.content.contract;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.content.adapter.ContentLevelThreeAdapter;
import com.csjbot.mobileshop.feature.content.bean.ChildContentLevelThreeBean;
import com.csjbot.mobileshop.feature.content.popup.ContentThreePopup;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.bean.rsp.ContentInfoBean;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridLayoutManager;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridSnapHelper;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author ShenBen
 * @date 2018/10/19 16:46
 * @email 714081644@qq.com
 */
public class ContentLevelThreePresenterImpl implements ContentLevelThreeContract.Presenter, PagerGridLayoutManager.PageListener {
    private static final String TAG = "ContentLevelThree";
    private Context mContext;
    private ContentLevelThreeContract.View mView;
    private RecyclerView mRvTitle;
    private PagerGridLayoutManager mManager;
    private List<ChildContentLevelThreeBean> mList;
    private ContentLevelThreeAdapter mAdapter;
    private int mCurrentPosition = 0;//当前点击的位置

    private ContentThreePopup mPopup;
    /**
     * 是否是大屏
     */
    private boolean isPlus;

    public ContentLevelThreePresenterImpl(Context mContext, ContentLevelThreeContract.View mView) {
        this.mContext = mContext;
        this.mView = mView;
        isPlus = Constants.isPlus();
        mView.setPresenter(this);
    }

    @Override
    public void loadData(String data) {
        mRvTitle = mView.getRvTitle();
        mManager = new PagerGridLayoutManager(1, 4, PagerGridLayoutManager.HORIZONTAL);
        mManager.setPageListener(this);
        mRvTitle.setLayoutManager(mManager);
        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(mRvTitle);
        mRvTitle.setHasFixedSize(true);

        mList = new ArrayList<>();
        mPopup = new ContentThreePopup(mContext);
        if (isPlus) {
            mAdapter = new ContentLevelThreeAdapter(R.layout.item_content_level_three);
        } else {
            mAdapter = new ContentLevelThreeAdapter(R.layout.item_content_level_three);
        }
        mRvTitle.setAdapter(mAdapter);

        mPopup.setOnItemClickListener((data1, word, position) -> mView.loadUrl(word, data1));

        mPopup.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mList.get(mCurrentPosition).setDropping(false);
                mAdapter.notifyDataSetChanged();
            }
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            updateSelectedIndex(position);
        });
        /*
        加载数据
         */
        List<ContentInfoBean.ChildrenBeanX> children = GsonUtils.jsonToObject(data,
                new TypeToken<List<ContentInfoBean.ChildrenBeanX>>() {
                }.getType());
        ChildContentLevelThreeBean.ChildrenBean child;
        List<ChildContentLevelThreeBean.ChildrenBean> childrenList;
        for (ContentInfoBean.ChildrenBeanX beanX : children) {
            if (!beanX.isHomeShow()) {//如果父目录不显示，则全部不显示，直接进入下一次循环
                continue;
            }
            childrenList = new ArrayList<>();

            if (beanX.getChildren() != null && !beanX.getChildren().isEmpty()) {//有child，则添加child
                for (ContentInfoBean.ChildrenBeanX.ChildrenBean childrenBean : beanX.getChildren()) {//遍历child
                    if (childrenBean.isHomeShow()) {//如果显示
                        child = new ChildContentLevelThreeBean.ChildrenBean();
                        child.setTitle(childrenBean.getName());
                        child.setSpeakWords(childrenBean.getWords());
                        child.setContentUrl(childrenBean.getDetails());
                        child.setChecked(false);
                        child.setOrder(beanX.getSequence());
                        childrenList.add(child);
                    }
                }
                if (!childrenList.isEmpty()) {//child列表不为空
                    childrenList.get(0).setChecked(true);//默认的子集的第一项为默认状态,用于title显示
                    mList.add(new ChildContentLevelThreeBean(childrenList, childrenList.size() > 1));
                }
            } else {//无Children
                child = new ChildContentLevelThreeBean.ChildrenBean();
                child.setTitle(beanX.getName());
                child.setContentUrl(beanX.getDetails());
                child.setSpeakWords(beanX.getWords());
                child.setOrder(beanX.getSequence());
                child.setChecked(false);
                childrenList.add(child);
                if (!childrenList.isEmpty()) {
                    childrenList.get(0).setChecked(true);//默认的子集的第一项为默认状态,用于title显示
                    mList.add(new ChildContentLevelThreeBean(childrenList, false));
                }
            }
        }
        if (!mList.isEmpty()) {
            Collections.sort(mList);
            mList.get(0).setChecked(true);//默认项设为点击状态
            mView.loadUrl(mList.get(0).getList().get(0).getSpeakWords(),
                    mList.get(0).getList().get(0).getContentUrl());//加载默认项
            mAdapter.setNewData(mList);
        }
    }

    @Override
    public boolean voiceControl(String text) {
        if (!mList.isEmpty() && !TextUtils.isEmpty(text)) {
            int size = mList.size();
            int childSize;
            List<ChildContentLevelThreeBean.ChildrenBean> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.clear();
                list.addAll(mList.get(i).getList());
                childSize = list.size();
                for (int j = 0; j < childSize; j++) {
                    if (text.contains(list.get(j).getTitle())) {//用户说的话术 包含title，则选中该title
                        if (mPopup.isShowing()) {
                            mPopup.dismiss();
                        }
                        mList.get(mCurrentPosition).setChecked(false);//设置当前选中项未选中，进行重置
                        for (ChildContentLevelThreeBean.ChildrenBean bean : mList.get(mCurrentPosition).getList()) {
                            bean.setChecked(false);
                        }
                        mList.get(mCurrentPosition).getList().get(0).setChecked(true);

                        mList.get(i).setChecked(true);//匹配项选中，遍历子项选中
//                        mCurrentPosition = i;
                        for (ChildContentLevelThreeBean.ChildrenBean bean : mList.get(i).getList()) {
                            bean.setChecked(false);
                        }
                        mList.get(i).getList().get(j).setChecked(true);
                        mAdapter.notifyDataSetChanged();
                        mManager.smoothScrollToPage(i / 4);

                        mView.loadUrl(mList.get(i).getList().get(j).getSpeakWords(), mList.get(i).getList().get(j).getContentUrl());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 更新列表选中项
     *
     * @param position
     */
    private void updateSelectedIndex(int position) {
        ChildContentLevelThreeBean threeBean = mList.get(position);
        //如果重复点击
        if (mCurrentPosition == position) {
            //如果可以下拉
            if (threeBean.isCanDrop()) {
                //如果没有处于下拉状态
                if (!threeBean.isDropping()) {
                    threeBean.setDropping(true);
                    mAdapter.notifyDataSetChanged();
                    showPopup(threeBean.getList(), position);
                }
            }
            return;
        }
        mList.get(mCurrentPosition).setChecked(false);
        threeBean.setChecked(true);
        //加载子集中处于checked状态的网页
        for (ChildContentLevelThreeBean.ChildrenBean childrenBean : threeBean.getList()) {
            if (childrenBean.isChecked()) {
                mView.loadUrl(childrenBean.getSpeakWords(), childrenBean.getContentUrl());
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
        mCurrentPosition = position;
    }

    /**
     * 显示popupWindow
     *
     * @param list     数据
     * @param position 数据位置
     */
    private void showPopup(List<ChildContentLevelThreeBean.ChildrenBean> list, int position) {
        int index = 0;//子集当前选中的位置
        int size = list.size();
        ChildContentLevelThreeBean.ChildrenBean childrenBean;
        for (int i = 0; i < size; i++) {
            childrenBean = list.get(i);
            if (childrenBean.isChecked()) {
                index = i;
                break;
            }
        }
        list.get(index).setChecked(true);
        mPopup.setCheckedIndex(index);
        mPopup.setNewData(list);
        int location;
        switch (position % 4) {
            case 0:
                location = 1;
                break;
            case 1:
                location = 2;
                break;
            case 2:
                location = 3;
                break;
            case 3:
                location = 4;
                break;
            default:
                location = 1;
                break;
        }
        mPopup.setShowPopupLocation(location);
        mPopup.showPopupWindow(mRvTitle);
    }

    @Override
    public void onPageSizeChanged(int pageSize) {

    }

    @Override
    public void onPageSelect(int pageIndex) {

    }
}
