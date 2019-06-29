package com.csjbot.mobileshop.guide_patrol.patrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class FileDialogAdapter extends RecyclerView.Adapter<FileDialogAdapter.NaviFileHolder>{

    private List<File> files = new ArrayList<>();
    private Context mContext;
    private ItemClickListener clickListener;
    private Constants.ResourceSuffix.suffix rSuffix;
    private int selectedIndex = -1;

    public FileDialogAdapter(Context context, List<File> lists) {
        mContext = context;
        files = lists;
    }

    public FileDialogAdapter(Context context) {
        mContext = context;
    }

    public void setFileList(List<File> list) {
        files = list;
        notifyDataSetChanged();
    }

    @Override
    public NaviFileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FileDialogAdapter.NaviFileHolder(LayoutInflater.from(mContext).inflate(R.layout.item_file_list, parent, false));
    }

    @Override
    public void onBindViewHolder(NaviFileHolder holder, int position) {
        File f = files.get(position);
        holder.tvFileName.setText(f.getName());
        holder.tvFileNumber.setText(position+"");
        if (selectedIndex == position) {
            holder.tvFileName.setTextColor(mContext.getResources().getColor(R.color.file_select));
        } else {
            holder.tvFileName.setTextColor(mContext.getResources().getColor(R.color.file_not_select));
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = position;
                notifyDataSetChanged();
                if (rSuffix == Constants.ResourceSuffix.suffix.TXT) {
                    if (!FileUtil.isExceedSize(f.getAbsolutePath(), Constants.descLimit, Constants.KB)) {
                        onClickee(holder, f);
                    } else {
                        Toast.makeText(mContext, "所选文件超过指定大小", Toast.LENGTH_LONG).show();
                    }
                } else {
                    onClickee(holder, f);
                }
            }
        });
    }

    private void onClickee(NaviFileHolder holder, File f) {
        if (clickListener != null) {
            clickListener.onItemClick(f.getName());
        }
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
        TextView tvFileNumber;//文件索引
        TextView tvFileName;//文件名称
        NaviFileHolder(View view) {
            super(view);
            tvFileNumber = view.findViewById(R.id.tv_number);
            tvFileName = (TextView) view.findViewById(R.id.tvfileName);
            linearLayout = (LinearLayout) view.findViewById(R.id.layout_file);
        }
    }

    public void setClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setType(Constants.ResourceSuffix.suffix suffix) {
        rSuffix = suffix;
    }

    public interface ItemClickListener {
        void onItemClick(String path);
    }
}

