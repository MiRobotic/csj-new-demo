package com.csjbot.mobileshop.guide_patrol.patrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.guide_patrol.patrol.bean.PatrolBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2019/1/19.
 */

public class PatrolLineListAdapter extends RecyclerView.Adapter<PatrolLineListAdapter.PatrolLineListHolder>{
    private Context mContext;
    private List<PatrolBean> mLists = new ArrayList<>();

    public PatrolLineListAdapter(Context context, ArrayList<PatrolBean> lists) {
        mContext = context;
        mLists = lists;
    }

    public PatrolLineListAdapter(Context context) {
        mContext = context;
    }

    public void setGuideList(List<PatrolBean> patrolBeans) {
        mLists = patrolBeans;
        notifyDataSetChanged();
    }

    @Override
    public PatrolLineListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PatrolLineListHolder(LayoutInflater.from(mContext).inflate(R.layout.item_guide_list, parent, false));
    }

    @Override
    public void onBindViewHolder(PatrolLineListHolder holder, int position) {
        PatrolBean patrolBean = mLists.get(position);
        holder.tvGuideIndex.setText(position+1+"");
        holder.tvGuideName.setText(limitNaviName(patrolBean.getName()));
    }

    @Override
    public int getItemCount() {
        if (mLists == null || mLists.size() <= 0) {
            return 0;
        }
        return mLists.size();
    }

    private String limitNaviName(String name) {
        String naviName = name;
        if (name != null) {
            if (name.length() > 18) {
                naviName = name.substring(0, 18) + "...";
            }
        }
        return naviName;
    }

    class PatrolLineListHolder extends RecyclerView.ViewHolder {
        TextView tvGuideIndex;//单种产品总计
        TextView tvGuideName;//删除按钮
        PatrolLineListHolder(View view) {
            super(view);
            tvGuideIndex = (TextView) view.findViewById(R.id.tvGuideIndex);
            tvGuideName = (TextView) view.findViewById(R.id.tvGuideName);
        }
    }
}
