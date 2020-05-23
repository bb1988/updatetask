package com.tytzy.network.interceptor;

import com.tytzy.network.listener.UploadProgressListener;
import com.tytzy.network.requestbody.UploadProgressRequestBody;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件名：UploadProgressInterceptor
 * 版权：Copyright © 2016 RMD Inc. All Rights Reserved
 * 描述：上传进度监听拦截器
 * 创建人：白勃
 * 创建时间：2016/9/27 16:46
 * 版本号：0.1
 */

public class UploadProgressInterceptor implements Interceptor {

    private UploadProgressListener listener;

    public UploadProgressInterceptor(UploadProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();

        if (originalRequest.body() == null) {
            return chain.proceed(originalRequest);
        }

        Request progressRequest = originalRequest.newBuilder()
                .method(originalRequest.method(),
                        new UploadProgressRequestBody(originalRequest.body(), listener))
                .build();

        return chain.proceed(progressRequest);

    }
}
