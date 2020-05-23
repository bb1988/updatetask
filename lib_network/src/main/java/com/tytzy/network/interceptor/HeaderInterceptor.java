package com.tytzy.network.interceptor;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件名：HeaderInterceptor
 * 版权：Copyright © 2016 RMD Inc. All Rights Reserved
 * 描述：公共参数拦截器
 * 创建人：白勃
 * 创建时间：2016/10/11 10:04
 * 版本号：0.1
 */

public class HeaderInterceptor implements Interceptor {
    private Map<String, String> headers;

    public HeaderInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request()
                .newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey));
            }
        }
        return chain.proceed(builder.build());
    }
}
