package com.tytzy.network.listener;

/**
 *  文件名：DownloadProgressListener
 *  版权：Copyright © 2016 RMD Inc. All Rights Reserved
 *  描述：下载进度监听接口
 *  创建人：白勃
 *  创建时间：2016/9/27 9:21
 *  版本号：0.1
 */

public interface DownloadProgressListener {
    void onProgress(long bytesRead, long contentLength, boolean done);
}
