package com.kangengine.retrofitlibrary;

/**
 * @author : Vic
 * time   : 2018/09/24
 * desc   :
 */
public interface JsDownloadListener {
    /**
     * 开始下载
     */
    void onStartDownload();

    /**
     * 下载中的进度回调
     * @param progress
     */
    void onProgress(int progress);

    /**
     * 下载完成
     */
    void onFinishDownload();

    /**
     *  下载失败
     * @param errorInfo
     */
    void onFail(String errorInfo);

}
