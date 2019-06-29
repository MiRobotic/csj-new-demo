package com.csjbot.mobileshop.model.http.apiservice.download.task;


import android.os.SystemClock;
import android.text.TextUtils;

import com.csjbot.mobileshop.model.http.apiservice.download.listener.DownloadListener;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by 孙秀艳 on 2018/12/18.
 */

public class DownloadTask implements Runnable {
    private static final int BUFFER_SIZE = 1024 * 8;
    private String mUrl;
    private String mFileFolder;//存放的文件夹
    private String mFileName;// 文件名 需带后缀
    private String mFilePath;
    private long mTotalSize = 0;
    private long mCurrentSize = 0;
    private DownloadListener mListener;
    private long mLastRefreshTime;

    public DownloadTask(String mUrl, String mFileFolder, String mFileName) {
        this.mUrl = mUrl;
        this.mFileFolder = mFileFolder;
        this.mFileName = mFileName;
    }

    public DownloadTask setDownloadListener(DownloadListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mUrl) || TextUtils.isEmpty(mFileFolder) || TextUtils.isEmpty(mFileName)) {
            postOnFailed("url、文件夹、文件名不能为空");
            return;
        }
        if (!FileUtil.makeDirectory(mFileFolder)) {
            postOnFailed("File Directory is created failed!");
            return;
        }
        mFilePath = mFileFolder + mFileName;
        postOnStart();
        getData();
    }

    /**
     * 网络请求
     */
    private void getData() {
        ServerFactory.createApi().downloadFile(mUrl, new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ResponseBody responseBody) {
                if (responseBody == null) {
                    postOnFailed("The download file is failed!");
                    return;
                }
                new Thread(() -> writeResponseBodyToDisk(responseBody)).start();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                postOnFailed(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void writeResponseBodyToDisk(ResponseBody body) {
        mTotalSize = body.contentLength();
        File file = new File(mFileFolder, mFileName);
        if (file.exists()) {
            if (file.length() == mTotalSize) {
                postOnSuccess();
                return;
            } else {
                file.delete();
            }
        }
        RandomAccessFile randomAccessFile = null;
        BufferedInputStream bis = null;
        InputStream is = body.byteStream();
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            byte[] buffer = new byte[BUFFER_SIZE];
            bis = new BufferedInputStream(is, BUFFER_SIZE);
            int len;
            while ((len = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                randomAccessFile.write(buffer, 0, len);
                mCurrentSize += len;
                if (mCurrentSize == mTotalSize) {
                    postOnProgress(100);
                    postOnSuccess();
                    break;
                }
                changeProgress(mCurrentSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert randomAccessFile != null;
                randomAccessFile.close();
                assert bis != null;
                bis.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void changeProgress(long currentSize) {
        long currentTime = SystemClock.elapsedRealtime();
        boolean isNotify = (currentTime - mLastRefreshTime) >= 300;//防止频繁更新进度
        if (!isNotify) {
            return;
        }
        int progress = (int) (currentSize * 1.0f * 100f / mTotalSize);
        postOnProgress(progress);//当前进度
        mLastRefreshTime = SystemClock.elapsedRealtime();
    }


    private void postOnStart() {
        if (mListener != null) {
            mListener.downloadStart();
        }
    }

    private void postOnSuccess() {
        if (mListener != null) {
            mListener.downloadSuccessful(mFilePath);
        }
    }

    private void postOnProgress(int progress) {
        if (mListener != null) {
            mListener.onProgressUpdate(progress);
        }
    }

    private void postOnFailed(String error) {
        if (mListener != null) {
            mListener.downloadFailed(error);
        }
    }

}


