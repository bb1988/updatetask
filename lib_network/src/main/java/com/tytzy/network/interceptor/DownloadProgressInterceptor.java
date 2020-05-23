package com.tytzy.network.interceptor;

import com.tytzy.network.listener.DownloadProgressListener;
import com.tytzy.network.response.responsebody.DownloadProgressResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 *  文件名：DownloadProgressInterceptor
 *  版权：Copyright © 2016 RMD Inc. All Rights Reserved
 *  描述：下载进度监听拦截器
 *  创建人：白勃
 *  创建时间：2016/9/27 9:44
 *  版本号：0.1
 */

public class DownloadProgressInterceptor implements Interceptor {

    private DownloadProgressListener listener;

    public DownloadProgressInterceptor(DownloadProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new DownloadProgressResponseBody(originalResponse.body(), listener))
                .build();
    }
}
