package com.csjbot.mobileshop.feature.settings.checked_update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnGetVersionListener;
import com.csjbot.coshandler.listener.OnUpgradeListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.core.RobotManager;

import com.csjbot.mobileshop.dialog.ProgressDialog;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.apiservice.download.listener.DownloadListener;
import com.csjbot.mobileshop.model.http.apiservice.download.task.DownloadTask;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.bean.rsp.UpdateAPKBean;

import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.util.CSJToast;


import java.io.File;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author ShenBen
 * @date 2018/12/25 16:38
 * @email 714081644@qq.com
 */

public class SettingUpdateAppPresenterImpl implements SettingUpdateAppContract.Presenter {
    private Context mContext;
    private SettingUpdateAppContract.View mView;
    /**
     * 当前Android版本
     */
    private int mCurrentAndroidVersionCode = 0;
    /**
     * 远程Android版本
     */
    private int mRemoteAndroidVersionCode = 0;
    /**
     * 远程Android下载地址
     */
    private String mRemoteAndroidDownloadUrl;
    /**
     * 存放文件 文件夹
     */
    private String mFileFolder = Constants.APP_PATH;
    /**
     * 下载文件名
     */
    private String mFileName;
    /**
     * 底层是否连接
     */
    private boolean isConnected = true;
    /**
     * 是否正在更新Linux
     */
    private boolean isUpdatingLinux = false;

    private ProgressDialog mLinuxDialog;
    private ProgressDialog mAndroidDialog;

    private DownloadTask mTask;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://Toast
                    CSJToast.showToast(mContext, String.valueOf(msg.obj));
                    break;
                case 2://显示更新Linux Dialog
                    showUpdateLinuxDialog();
                    break;
                case 3://Linux 更新进度
                    if (mLinuxDialog != null) {
                        mLinuxDialog.setProgress(msg.arg1);
                    }
                    if (msg.arg1 == 100) {
                        if (mLinuxDialog != null) {
                            mLinuxDialog.dismiss();
                        }
                        CSJToast.showToast(mContext, mContext.getString(R.string.download_completes_install));
                    }
                    break;
                case 4://更新Android 下载进度
                    if (mAndroidDialog != null && mAndroidDialog.isShowing()) {
                        mAndroidDialog.setProgress(msg.arg1);
                    }
                    break;
                case 5://安装APK
                    if (mAndroidDialog != null && mAndroidDialog.isShowing()) {
                        mAndroidDialog.dismiss();
                    }
                    String path = String.valueOf(msg.obj);
                    installApk(path);
                    break;
                case 6://Android更新dialog消失
                    CSJToast.showToast(mContext, String.valueOf(msg.obj));
                    if (mAndroidDialog != null && mAndroidDialog.isShowing()) {
                        mAndroidDialog.dismiss();
                    }
                    break;
            }
        }
    };

    public SettingUpdateAppPresenterImpl(Context mContext, SettingUpdateAppContract.View mView) {
        this.mContext = mContext;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void initData() {
        if (!RobotManager.getInstance().getConnectState()) {
            isConnected = false;
            CSJToast.showToast(mContext, mContext.getString(R.string.not_connect_slam));
        }
        RobotManager.getInstance().addListener(mGetVersionListener);
        RobotManager.getInstance().robot.reqProxy.getVersion();

        mCurrentAndroidVersionCode = BuildConfig.VERSION_CODE;
        ServerFactory.createApi().getAppVersion(Robot.SN, BuildConfig.category, new Observer<ResponseBean<UpdateAPKBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                CsjlogProxy.getInstance().info("Android mRemoteAndroidVersionCode ： ");
            }

            @Override
            public void onNext(@NonNull ResponseBean<UpdateAPKBean> listResponseBean) {
                CsjlogProxy.getInstance().info("Android mRemoteAndroidVersionCode ： " + listResponseBean.toString());
                CsjlogProxy.getInstance().info("listResponseBean.getCode() mRemoteAndroidVersionCode ： " + listResponseBean.getCode());

                if (listResponseBean.getCode() == 200 && listResponseBean.getRows() != null) {
                    UpdateAPKBean bean = listResponseBean.getRows();
                    mRemoteAndroidVersionCode = bean.getVersionCode();
                    mRemoteAndroidDownloadUrl = bean.getFileUrl();
                    CsjlogProxy.getInstance().info("Android mRemoteAndroidVersionCode ： " + mRemoteAndroidVersionCode);

                    String name = bean.getVersionName().trim();
                    mFileName = name.endsWith(".apk") ? name : name + ".apk";
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                CsjlogProxy.getInstance().error("获取Android版本失败： " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void updateLinux() {
//        if (!isConnected){
//            CSJToast.showToast(mContext, "底层未连接");
//            return;
//        }
        RobotManager.getInstance().addListener(mLinuxUpgradeListener);
        com.csjbot.mobileshop.model.tcp.factory.ServerFactory.getVersionInstance().softwareCheck();
    }

    @Override
    public void updateAndroid() {
        if (mCurrentAndroidVersionCode >= mRemoteAndroidVersionCode) {
            CSJToast.showToast(mContext, mContext.getString(R.string.already_latest_version));
            return;
        }
        if (TextUtils.isEmpty(mRemoteAndroidDownloadUrl) || TextUtils.isEmpty(mFileName)) {
            CSJToast.showToast(mContext, mContext.getString(R.string.Please_check_network));
            return;
        }
        showUpdateAndroidDialog();
        if (mTask == null) {
            mTask = new DownloadTask(mRemoteAndroidDownloadUrl, mFileFolder, mFileName)
                    .setDownloadListener(new DownloadListener() {
                        @Override
                        public void downloadStart() {

                        }

                        @Override
                        public void downloadSuccessful(String path) {
                            Message message = new Message();
                            message.what = 5;
                            message.obj = path;
                            mHandler.sendMessage(message);
                        }

                        @Override
                        public void onProgressUpdate(int progress) {
                            Message message = new Message();
                            message.what = 4;
                            message.arg1 = progress;
                            mHandler.sendMessage(message);
                        }

                        @Override
                        public void downloadFailed(String error) {
                            Message message = new Message();
                            message.what = 6;
                            message.obj = error;
                            mHandler.sendMessage(message);
                            CsjlogProxy.getInstance().error("APK下载错误：" + error);
                        }
                    });
        }
        mHandler.post(mTask);
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mTask);
    }

    /**
     * 显示更新Linux Dialog
     */
    private void showUpdateLinuxDialog() {
        isUpdatingLinux = true;
        if (mAndroidDialog != null && mAndroidDialog.isShowing()) {
            mAndroidDialog.dismiss();
        }
        if (mLinuxDialog == null) {
            mLinuxDialog = new ProgressDialog(mContext);
            mLinuxDialog.setTitle(mContext.getString(R.string.linuxupdata));
            mLinuxDialog.setCancelButtonGone();
        }
        mLinuxDialog.setProgress(0);
        mLinuxDialog.show();
    }

    private void showUpdateAndroidDialog() {
        if (mLinuxDialog != null && mLinuxDialog.isShowing()) {
            mLinuxDialog.dismiss();
        }
        if (mAndroidDialog == null) {
            mAndroidDialog = new ProgressDialog(mContext);
            mAndroidDialog.setTitle(mContext.getString(R.string.Androidupdata));
            mAndroidDialog.setCancelButtonGone();
            mAndroidDialog.setOnCancelListener(() -> {
                mHandler.removeCallbacks(mTask);
                mAndroidDialog.dismiss();
                mAndroidDialog.setProgress(0);
            });
        }
        mAndroidDialog.setProgress(0);
        mAndroidDialog.show();
    }

    private OnGetVersionListener mGetVersionListener = new OnGetVersionListener() {
        @Override
        public void response(String version) {
            mView.setLinuxVersion(version);
        }
    };

    private OnUpgradeListener mLinuxUpgradeListener = new OnUpgradeListener() {
        @Override
        public void checkRsp(int errorCode) {
            if (errorCode == 60002) {//已是最新版本
                Message message = new Message();
                message.what = 1;
                message.obj = mContext.getString(R.string.already_latest_version);
                mHandler.sendMessage(message);
            } else if (errorCode == 60001) {//没有获取到版本信息，请检查网络
                Message message = new Message();
                message.what = 1;
                message.obj = mContext.getString(R.string.Please_check_network);
                mHandler.sendMessage(message);
            } else if (errorCode == 0) {//正常更新
                if (!isUpdatingLinux) {
                    mHandler.sendEmptyMessage(2);
                    com.csjbot.mobileshop.model.tcp.factory.ServerFactory.getVersionInstance().softwareUpgrade();//发送更新命令
                }
            }
        }

        @Override
        public void upgradeRsp(int errorCode) {

        }

        @Override
        public void upgradeProgress(int downloadProgress) {
            Message message = new Message();
            message.what = 3;
            message.arg1 = downloadProgress;
            mHandler.sendMessage(message);
        }
    };

    /**
     * 安装APK
     *
     * @param path 安装包路径
     */
    private void installApk(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {//Android6.0 一下有效
                Intent mIntent = new Intent();
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.fromFile(file);
                mIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                mContext.startActivity(mIntent);
            }
        }
    }

}
