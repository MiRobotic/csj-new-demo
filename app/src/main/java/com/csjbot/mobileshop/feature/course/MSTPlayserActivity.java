package com.csjbot.mobileshop.feature.course;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.widget.MyVideoView;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridLayoutManager;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridSnapHelper;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import skin.support.widget.SkinCompatImageView;

/**
 * 课件播放
 * Created by Administrator on 2019/2/15.
 */

@Route(path = BRouterPath.MST_PLAYER)
public class MSTPlayserActivity extends BaseModuleActivity {


    protected String[] suffixs = {
            "ppt", "mp4", "avi"
    };

    private List<File> mFiles;

    private long mLastTime;

    @BindView(R.id.ll)
    LinearLayout ll;

    @BindView(R.id.rl_video)
    RelativeLayout rl_video;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.videoview)
    MyVideoView videoview;

    @BindView(R.id.tv_play_state)
    TextView tv_play_state;

    @BindView(R.id.ll_dots)
    LinearLayout llDots;


    private MyAdapter myAdapter;

    private boolean otherSwitch = false;

    private int selectedIndex = -1;

    private boolean boo = true;

    private boolean isVideo;

    private PagerGridLayoutManager manager;


    private int pageSize = 0;//页数

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_course_introduction;
    }

    @Override
    public void init() {
        super.init();
        mFiles = new ArrayList<>();
        myAdapter = new MyAdapter(R.layout.item_course_introduction_list, mFiles);
        manager = new PagerGridLayoutManager(1, 3, PagerGridLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(manager);

        manager.setPageListener(new PagerGridLayoutManager.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {

            }

            @Override
            public void onPageSelect(int pageIndex) {
                SkinCompatImageView imageView;
                for (int i = 0; i < llDots.getChildCount(); i++) {
                    imageView = (SkinCompatImageView) llDots.getChildAt(i);
                    if (pageIndex == i) {
                        imageView.setImageResource(R.drawable.jiaoyu_rotation_point_selected);
                    } else {
                        imageView.setImageResource(R.drawable.jiaoyu_rotation_point_unselected);
                    }
                }
            }
        });
        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(recyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener((adapter, view, position) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastTime < 1000) {//1秒钟只能点击一次
                return;
            }
            mLastTime = currentTime;

            myAdapter.notifyDataSetChanged();
            selectedIndex = position;

            File file = mFiles.get(position);
            String fileName = file.getName();
            if (fileName.contains(suffixs[0])) {
                openDoc(file);
            } else if (fileName.contains(suffixs[1]) || fileName.contains(suffixs[2])) {
                playVideo(file);
            }

        });

        getAllFiles();
    }

    private void updateDots() {
        double a = (double) mFiles.size() / (double) (1 * 3);
        pageSize = (int) Math.ceil(a);
        llDots.removeAllViews();
        if (pageSize > 1) {
            SkinCompatImageView dot;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;
            for (int i = 0; i < pageSize; i++) {
                //小圆点
                dot = new SkinCompatImageView(this);
                dot.setScaleType(ImageView.ScaleType.CENTER_CROP);
                dot.setLayoutParams(params);
                if (i == 0) {
                    dot.setImageResource(R.drawable.jiaoyu_rotation_point_selected);
                } else {
                    dot.setImageResource(R.drawable.jiaoyu_rotation_point_unselected);
                }
                llDots.addView(dot);
            }
        }
    }

    @OnClick(R.id.course_ib_back)
    public void course_ib_back() {
        closeVideo();
    }

    @OnClick(R.id.tv_play_state)
    public void tv_play_state() {
        switchVideoState();
    }

    public void getAllFiles() {
        File root = new File("/sdcard/csjbot/snow_class");
        if (root.exists()) {
            File[] files = root.listFiles();
            if (files != null && files.length > 0) {
                mFiles = new ArrayList<>();
                for (File file : files) {
                    String fileName = file.getName();
                    if (fileName.contains(suffixs[0])
                            || fileName.contains(suffixs[1])
                            || fileName.contains(suffixs[2])
                            ) {
                        selectedIndex = 0;
                        mFiles.add(file);
                        myAdapter.setNewData(mFiles);
                        updateDots();
                    }
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isVideo){
            if (!videoview.isPlaying()) {
                tv_play_state.setBackground(getDrawable(R.drawable.jiaoyu_pause_btn));
                videoview.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isVideo){
            if (videoview.isPlaying()) {
                videoview.pause();
                tv_play_state.setBackground(getDrawable(R.drawable.jiaoyu_play_btn));
            }
        }
    }

    class MyAdapter extends BaseQuickAdapter<File, BaseViewHolder> {
        /**
         * 字符串编码格式
         */
        private static final String ENCODE = "UTF-8";
        private static final int K = 0x80;

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<File> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, File item) {
            ImageView iv = helper.getView(R.id.iv);
            String fileName = item.getName();

            int position = helper.getAdapterPosition();
            if (fileName.contains(suffixs[0])) {
                if (boo) {
                    if (position == 0) {
                        iv.setImageResource(R.drawable.ppt_selected);
                    }
                    boo = false;
                } else {
                    if (selectedIndex == position) {
                        iv.setImageResource(R.drawable.ppt_selected);
                    } else {
                        iv.setImageResource(R.drawable.ppt);
                    }

                }

            } else if (fileName.contains(suffixs[1]) || fileName.contains(suffixs[2])) {
                if (boo) {
                    if (position == 0) {
                        iv.setImageResource(R.drawable.mov_selected);
                    }
                    boo = false;
                } else {
                    if (selectedIndex == position) {
                        iv.setImageResource(R.drawable.mov_selected);
                    } else {
                        iv.setImageResource(R.drawable.mov);
                    }

                }
            }
            helper.setText(R.id.tv, getSubString(item.getName(), 8));
        }

        /**
         * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
         *
         * @param c
         * @return boolean
         */
        private boolean isLetter(char c) {
            return (c / K) == 0;
        }

        /**
         * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
         *
         * @param s 需要得到长度的字符串
         * @return 得到的字符串长度
         */
        private double length(String s) {
            if (s == null) {
                return 0;
            }
            char[] c = s.toCharArray();
            double len = 0;
            for (char aC : c) {
                if (isLetter(aC)) {
                    len += 0.5;
                } else {
                    len += 1;
                }
            }
            return Math.ceil(len);
        }

        /**
         * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
         *
         * @param origin 原始字符串
         * @param len    截取长度(一个汉字长度按1算的)
         * @return String, 返回的字符串
         */
        private String getSubString(String origin, int len) {
            try {
                // 字符串为空
                if (TextUtils.isEmpty(origin)) {
                    return "";
                }
                // 截取长度大于字符串长度
                if (len > length(origin)) {
                    return origin;
                }

                StringBuilder buffer = new StringBuilder();
                char[] array = origin.toCharArray();
                double currentLength = 0;
                for (char c : array) {
                    // 字符长度
                    int charlen = String.valueOf(c).getBytes(ENCODE).length;
                    // 汉字按一个长度，字母数字按半个长度
                    if (charlen == 3) {
                        currentLength += 1;
                    } else {
                        currentLength += 0.5;
                    }
                    if (currentLength <= len) {
                        buffer.append(c);
                    } else {
                        break;
                    }
                }
                buffer.append("…");
                return buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    private void playVideo(File file) {
        isVideo = true;
        ll.setVisibility(View.GONE);
        videoview.setOnCompletionListener(mp -> {
            closeVideo();
        });
        videoview.setVideoPath(file.getAbsolutePath());
        videoview.start();
        tv_play_state.setBackground(getDrawable(R.drawable.jiaoyu_pause_btn));
        new Handler().postDelayed(() -> rl_video.setVisibility(View.VISIBLE),500);

    }

    private void switchVideoState() {
        if (videoview.isPlaying()) {
            videoview.pause();
            tv_play_state.setBackground(getDrawable(R.drawable.jiaoyu_play_btn));
        } else {
            tv_play_state.setBackground(getDrawable(R.drawable.jiaoyu_pause_btn));
            videoview.start();
        }

    }

    private void closeVideo() {
        isVideo = false;
        videoview.stopPlayback();
        rl_video.setVisibility(View.GONE);
        ll.setVisibility(View.VISIBLE);

    }

    /*
    * check the app is installed
    */
    private boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        }catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo ==null){
            //System.out.println("没有安装");
            return false;
        }else{
            //System.out.println("已经安装");
            return true;
        }
    }


    public void openDoc(File file) {
        if(!isAppInstalled(this,"cn.wps.moffice_eng")){
            Toast.makeText(context, "请先安装wps应用！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = getPackageManager().getLaunchIntentForPackage("cn.wps.moffice_eng");

        Bundle bundle = new Bundle();
        bundle.putString("OpenMode", "ReadOnly");// 只读模式
        bundle.putBoolean("SendSaveBroad", true);// 关闭保存时是否发送广播
        bundle.putBoolean("SendCloseBroad", true);// 关闭文件时是否发送广播
        bundle.putBoolean("HomeKeyDown", true);// 按下Home键
        bundle.putBoolean("BackKeyDown", true);// 按下Back键
        bundle.putBoolean("IsShowView", false);// 是否显示wps界面
        bundle.putBoolean("AutoJump", true);// //第三方打开文件时是否自动跳转
        //设置广播
        bundle.putString("ThirdPackage", getPackageName());
        //第三方应用的包名，用于对改应用合法性的验证
        //bundle.putBoolean(Define.CLEAR_FILE, true);
        //关闭后删除打开文件
        intent.setAction(android.content.Intent.ACTION_MAIN);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        String fileUrl = file.getAbsolutePath();
//        intent.setData(Uri.parse(fileUrl));
        File openPPT = new File(fileUrl);

        if (!openPPT.exists()) {
            LogUtils.e("fileUrl  " + fileUrl + "   !exists ");
            return;
        }
        Uri uri = Uri.fromFile(openPPT);
        intent.setData(uri);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case 19:
            case 20:
                break;
            case 111:
            case 135:
            case 47:
                LogUtils.e("onKeyUp" + keyCode + "event " + event.getKeyCode());
                otherSwitch = true;
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case 19:
                if (isVideo) {
                    closeVideo();
                    return false;
                }
                if ((selectedIndex - 1) > -1) {
                    selectedIndex--;
                    myAdapter.notifyDataSetChanged();

                    manager.scrollToPosition(selectedIndex);
                }
                LogUtils.d("onKeyDown" + keyCode + "event " + event.getKeyCode());
                break;
            case 20:
                if (isVideo) {
                    switchVideoState();
                    return false;
                }
                if ((selectedIndex + 1) < myAdapter.getData().size()) {
                    selectedIndex++;
                    myAdapter.notifyDataSetChanged();
                    manager.scrollToPosition(selectedIndex);
                }
                otherSwitch = true;
                LogUtils.d("onKeyDown" + keyCode + "event " + event.getKeyCode());
                break;
            case 135:
            case 111:
            case 47:
                if (otherSwitch) {
                    if (isVideo) {
                        return false;
                    }
                    if (mFiles.size() > 0) {
                        File file = mFiles.get(selectedIndex);
                        String fileName = file.getName();
                        if (fileName.contains(suffixs[0])) {
                            openDoc(file);
                        } else if (fileName.contains(suffixs[1]) || fileName.contains(suffixs[2])) {
                            playVideo(file);
                        }
                    }
                    LogUtils.d("onKeyDown" + keyCode + "event " + event.getKeyCode());
                }
                otherSwitch = false;
            default:

                break;
        }

        return false;
    }
}
