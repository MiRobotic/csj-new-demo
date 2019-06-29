package com.csjbot.mobileshop.feature.media.contract;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.media.adapter.MediaPublicizeAdapter;
import com.csjbot.mobileshop.feature.media.bean.MediaPublicizeBean;
import com.csjbot.mobileshop.feature.media.video.MyVideoPlayer;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridLayoutManager;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridSnapHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import skin.support.widget.SkinCompatImageView;

/**
 * @author ShenBen
 * @date 2019/1/21 16:38
 * @email 714081644@qq.com
 */
public class MediaPublicizePresenterImpl implements MediaPublicizeContract.Presenter {
    private Context mContext;
    private MediaPublicizeContract.View mView;
    private MediaPublicizeAdapter mAdapter;
    private List<MediaPublicizeBean> mList;
    private static final String[] MEDIAS = new String[]{".mp4", ".3gp", ".mkv"};

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mList.isEmpty()) {
                        mView.isNoData(true);
                        return;
                    }
                    mView.isNoData(false);
                    mAdapter.setNewData(mList);
                    updateDots();
                    break;
            }
        }
    };

    public MediaPublicizePresenterImpl(Context mContext, MediaPublicizeContract.View mView) {
        this.mContext = mContext;
        this.mView = mView;
        mView.setPresenter(this);
    }


    @Override
    public void init() {
        mList = new ArrayList<>();
        initRecycler();
        new Thread(this::getMediaFile).start();
    }

    /**
     * 获取媒体文件
     */
    private void getMediaFile() {
        File file = new File(Constants.MEDIA_PATH);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            for (File f : files) {
                if (f.isFile()) {
                    for (String media : MEDIAS) {
                        MediaPublicizeBean bean;
                        String fileName;
                        if (f.getName().trim().toLowerCase().endsWith(media)) {
                            bean = new MediaPublicizeBean();
                            fileName = f.getName().trim();
                            fileName = fileName.substring(0, fileName.length() - media.length());
                            bean.setFileName(fileName);
                            bean.setFilePath(f.getAbsolutePath());
                            mList.add(bean);
                            break;
                        }
                    }
                }
            }
            mHandler.sendEmptyMessage(1);
        } else {
            file.mkdirs();
        }
    }

    private void initRecycler() {
        mAdapter = new MediaPublicizeAdapter();
        PagerGridLayoutManager manager = new PagerGridLayoutManager(2, 3, PagerGridLayoutManager.HORIZONTAL);
        mView.getRecycler().setLayoutManager(manager);
        mView.getRecycler().setAdapter(mAdapter);
        manager.setPageListener(new PagerGridLayoutManager.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {

            }

            @Override
            public void onPageSelect(int pageIndex) {
                SkinCompatImageView imageView;
                for (int i = 0; i < mView.getDots().getChildCount(); i++) {
                    imageView = (SkinCompatImageView) mView.getDots().getChildAt(i);
                    if (pageIndex == i) {
                        imageView.setImageResource(R.drawable.iv_point_selected);
                    } else {
                        imageView.setImageResource(R.drawable.iv_point_unselected);
                    }
                }
            }
        });
        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(mView.getRecycler());
        mView.getRecycler().setHasFixedSize(true);

        mAdapter.setOnItemClickListener((adapter, view, position) -> Jzvd.startFullscreen(mContext,
                MyVideoPlayer.class, mList.get(position).getFilePath(), mList.get(position).getFileName()));

    }

    private void updateDots() {
        double a = (double) mList.size() / (double) (6);
        int pageSize = (int) Math.ceil(a);
        mView.getDots().removeAllViews();
        if (pageSize > 1) {
            SkinCompatImageView dot;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;
            for (int i = 0; i < pageSize; i++) {
                //小圆点
                dot = new SkinCompatImageView(mContext);
                dot.setScaleType(ImageView.ScaleType.CENTER_CROP);
                dot.setLayoutParams(params);
                if (i == 0) {
                    dot.setImageResource(R.drawable.iv_point_selected);
                } else {
                    dot.setImageResource(R.drawable.iv_point_unselected);
                }
                mView.getDots().addView(dot);
            }
        }
    }
}
