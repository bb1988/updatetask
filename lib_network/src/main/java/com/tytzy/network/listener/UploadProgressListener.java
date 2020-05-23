package com.tytzy.network.listener;

/**
 *  文件名：UploadProgressListener
 *  版权：Copyright © 2016 RMD Inc. All Rights Reserved
 *  描述：上传进度监听接口
 *  创建人：白勃
 *  创建时间：2016/9/27 16:44
 *  版本号：0.1
 */

public interface UploadProgressListener {
    void onProgress(long bytesWritten, long contentLength);
}
