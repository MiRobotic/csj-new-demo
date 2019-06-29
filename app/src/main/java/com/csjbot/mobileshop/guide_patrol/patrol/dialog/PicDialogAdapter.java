package com.csjbot.mobileshop.guide_patrol.patrol.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csjbot.mobileshop.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class PicDialogAdapter extends RecyclerView.Adapter<PicDialogAdapter.NaviFileHolder>{

    private List<File> files = new ArrayList<>();
    private Context mContext;
    private ItemClickListener clickListener;
    private int selectedIndex = -1;

    public PicDialogAdapter(Context context, List<File> lists) {
        mContext = context;
        files = lists;
    }

    public PicDialogAdapter(Context context) {
        mContext = context;
    }

    public void setFileList(List<File> list) {
        files.clear();
        files.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public NaviFileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PicDialogAdapter.NaviFileHolder(LayoutInflater.from(mContext).inflate(R.layout.item_pic_list, parent, false));
    }

    @Override
    public void onBindViewHolder(NaviFileHolder holder, int position) {
        if (getItemCount() <= 0) {
            return;
        }
        File f = files.get(position);
        Glide.with(mContext).load(f).into(holder.imageView);
        holder.tvFileName.setText(f.getName());
        holder.tv_number.setText(position+1+"");
        if (selectedIndex == position) {
            holder.tvFileName.setTextColor(mContext.getResources().getColor(R.color.file_select));
        } else {
            holder.tvFileName.setTextColor(mContext.getResources().getColor(R.color.file_not_select));
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = position;
                if (clickListener != null) {
                    clickListener.onItemClick(f.getName());
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (files == null || files.size() <= 0) {
            return 0;
        }
        return files.size();
    }

    class NaviFileHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;//条目布局
        ImageView imageView;//图片或者视频
        TextView tvFileName;//文件名称
        TextView tv_number;//文件索引
        NaviFileHolder(View view) {
            super(view);
            tvFileName = (TextView) view.findViewById(R.id.tvfileName);
            tv_number = view.findViewById(R.id.tv_number);
            imageView =view.findViewById(R.id.iv_image_video);
            linearLayout = (LinearLayout) view.findViewById(R.id.layout_file);
        }
    }

    public void setClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(String path);
    }
}


