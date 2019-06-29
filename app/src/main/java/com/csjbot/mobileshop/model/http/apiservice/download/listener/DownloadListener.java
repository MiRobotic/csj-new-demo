package com.csjbot.mobileshop.model.http.apiservice.download.listener;

/**
 * @author ShenBen
 * @date 2018/12/25 18:20
 * @email 714081644@qq.com
 */

public interface DownloadListener {
    /**
     * 开始下载
     * 子线程
     */
    void downloadStart();

    /**
     * 下载成功
     * 子线程
     *
     * @param path 下载完成地址
     */
    void downloadSuccessful(String path);

    /**
     * 下载进度
     * 子线程
     *
     * @param progress 下载进度
     */
    void onProgressUpdate(int progress);

    /**
     * 下载失败
     * 子线程
     *
     * @param error 错误内容
     */
    void downloadFailed(String error);

}
