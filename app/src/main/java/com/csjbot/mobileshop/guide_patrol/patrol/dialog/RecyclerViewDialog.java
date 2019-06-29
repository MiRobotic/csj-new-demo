package com.csjbot.mobileshop.guide_patrol.patrol.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.guide_patrol.patrol.adapter.FileDialogAdapter;
import com.csjbot.mobileshop.util.BlackgagaLogger;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class RecyclerViewDialog extends Dialog {

    private RecyclerView recyclerView;
    private TextView tv_title;
    private Button bt_yes;
    private Button bt_no;
    private MyHandler mHandler;
    private List<File> fileList = new ArrayList<>();
    private FileDialogAdapter fileAdapter;
    private PicDialogAdapter picAdapter;
    private Context mContext;
    private OnDialogClickListener mListener;
    private String filePath;
    private Constants.ResourceSuffix.suffix rSuffix;
    private boolean isFinish = false;

    public RecyclerViewDialog(@NonNull Context context) {
        super(context, R.style.NewRetailDialog);
        mContext = context;
        initView();
    }

    public RecyclerViewDialog(@NonNull Context context, Constants.ResourceSuffix.suffix suffix) {
        super(context, R.style.NewRetailDialog);
        mContext = context;
        rSuffix = suffix;
        initView();
    }
    private void initView() {
        setContentView(R.layout.dialog_recyclerview);
        setCanceledOnTouchOutside(false);

        recyclerView = findViewById(R.id.lv_file_list);
        tv_title = findViewById(R.id.tv_title);
        bt_no = findViewById(R.id.bt_no);
        bt_yes = findViewById(R.id.bt_yes);

        bt_no.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.no();
            }
        });
        bt_yes.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.yes(filePath);
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width= ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height=ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    public void startSearch() {
        filePath = "";
        if (rSuffix == Constants.ResourceSuffix.suffix.IMAGEANDVIDEO || rSuffix == Constants.ResourceSuffix.suffix.IMAGE) {
            initPicRecyclerView();
        } else {
            initRecyclerView();
        }
        mHandler = new MyHandler(this);
        File root = new File(Constants.NAVI_PATH);
        new Thread(() -> scanFile(root, mHandler)).start();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        fileAdapter = new FileDialogAdapter(mContext);
        fileAdapter.setType(rSuffix);
        recyclerView.setAdapter(fileAdapter);
        fileAdapter.setClickListener(new FileDialogAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String path) {
                filePath = path;
            }
        });
    }

    private void initPicRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        picAdapter = new PicDialogAdapter(mContext);
        recyclerView.setAdapter(picAdapter);
        picAdapter.setClickListener(new PicDialogAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String path) {
                filePath = path;
            }
        });
    }

    public void scanFile(File root, Handler handler) {
        fileList.clear();
        File[] files = root.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                String fileName = file.getName();
                if (isExistFile(fileName)) {
                    handler.obtainMessage(Constants.SEARCH_SUC, file).sendToTarget();
                } else {
                    if (file.isDirectory()) {
                        scanFile(file, handler);
                    }
                }
            }
        }
    }

    private boolean isExistFile(String filename) {
        String[] suffixs = null;
        if (rSuffix == Constants.ResourceSuffix.suffix.MUSIC) {
            suffixs = Constants.ResourceSuffix.musicSuffix;
        } else if (rSuffix == Constants.ResourceSuffix.suffix.IMAGE) {
            suffixs = Constants.ResourceSuffix.imageSuffix;
        } else if (rSuffix == Constants.ResourceSuffix.suffix.IMAGEANDVIDEO) {
            suffixs = Constants.ResourceSuffix.imageAndVideoSuffix;
        } else if (rSuffix == Constants.ResourceSuffix.suffix.TXT) {
            suffixs = Constants.ResourceSuffix.txtSuffix;
        } else if (rSuffix == Constants.ResourceSuffix.suffix.VIDEO) {
            suffixs = Constants.ResourceSuffix.videoSuffix;
        }
        boolean isExist = false;
        if (suffixs != null && suffixs.length > 0) {
            for (int i=0; i<suffixs.length; i++) {
                if (filename.toLowerCase().contains(suffixs[i].toLowerCase())) {
                    isExist = true;
                    break;
                }
            }
        }
        return isExist;
    }

    public void addFile(File f) {
        fileList.add(f);
        refreshAdapter();
    }

    public void refreshAdapter() {
        if (rSuffix == Constants.ResourceSuffix.suffix.IMAGEANDVIDEO || rSuffix == Constants.ResourceSuffix.suffix.IMAGE) {
            picAdapter.setFileList(fileList);
        } else {
            fileAdapter.setFileList(fileList);
        }
    }

    /**
     * 静态内部类Handler(避免内存泄露)
     */
    class MyHandler extends Handler {
        WeakReference<RecyclerViewDialog> mContextWeakReference = null;
        public MyHandler(RecyclerViewDialog usbFragment) {
            super();
            mContextWeakReference = new WeakReference<>(usbFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RecyclerViewDialog mapSelectDialog = mContextWeakReference.get();
            if (mapSelectDialog != null) {
                switch (msg.what) {
                    case Constants.SEARCH_SUC:// 搜索到视频文件消息
                        // 发送给recyclerview进行局部刷新
                        addFile((File) msg.obj);
                        break;
                    default:
                        BlackgagaLogger.debug("default???");
                        break;
                }
            }
        }
    }

    public void setSuffix(Constants.ResourceSuffix.suffix suffix) {
        rSuffix = suffix;
    }

    public void setTitle(String text) {
        tv_title.setText(text);
    }

    public void setYes(String text) {
        bt_yes.setText(text);
    }

    public void setNo(String text) {
        bt_no.setText(text);
    }

    public void setListener(OnDialogClickListener listener){
        mListener = listener;
    }

    public void setResourceType(Constants.ResourceSuffix.suffix suffix) {
        rSuffix = suffix;
    }

    public interface OnDialogClickListener{
        void yes(String path);
        void no();
    }
}

