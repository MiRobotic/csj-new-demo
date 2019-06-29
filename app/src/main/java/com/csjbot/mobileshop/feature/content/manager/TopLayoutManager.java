package com.csjbot.mobileshop.feature.content.manager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author ShenBen
 * @date 2018/10/19 15:31
 * @email 714081644@qq.com
 */
public class TopLayoutManager extends LinearLayoutManager {
    public TopLayoutManager(Context context) {
        super(context);
    }

    public TopLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public TopLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller scroller = new TopSmoothScroller(recyclerView.getContext());
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }

    private static class TopSmoothScroller extends LinearSmoothScroller {

        public TopSmoothScroller(Context context) {
            super(context);
        }

        /**
         * 以下参数以LinearSmoothScroller解释
         *
         * @param viewStart      RecyclerView的top位置
         * @param viewEnd        RecyclerView的bottom位置
         * @param boxStart       Item的top位置
         * @param boxEnd         Item的bottom位置
         * @param snapPreference 判断滑动方向的标识
         * @return 移动偏移量
         */
        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return boxStart - viewStart;
        }
    }
}
