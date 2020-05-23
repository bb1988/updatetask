package com.tytzy.network.interceptor;

import android.content.Context;

import com.tytzy.network.utils.NetUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import utils.LogUtils;

/**
 * 作者: 白勃
 * 时间: 2020/5/21 5:13 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 缓存拦截器
 * @param
 */
public class CacheInterceptor implements Interceptor {

    private Context context;
    private int ONLINE_TIME;
    private int OFFLINE_TIME;

    public CacheInterceptor(Builder builder) {
        this.context = builder.context;
        this.OFFLINE_TIME = builder.OFFLINE_TIME;
        this.ONLINE_TIME = builder.ONLINE_TIME;
    }

    public static class Builder{

        private Context context;
        private int ONLINE_TIME;
        private int OFFLINE_TIME;
        public Builder setContext(Context context){
            this.context=context;
            return this;
        }
        public Builder onlineTime(int time){
            this.ONLINE_TIME=time;
            return this;
        }
        public Builder offLineTime(int time){
            this.OFFLINE_TIME=time;
            return this;
        }

        public CacheInterceptor build(){
            return new CacheInterceptor(this);
        }
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        if (NetUtils.checkNet(context)) {
            Response response = chain.proceed(request);
            int maxAge = ONLINE_TIME; // 在线缓存在1分钟内可读取
            String cacheControl = request.cacheControl().toString();
            LogUtils.e("bb-cache", "在线缓存在1分钟内可读取" + cacheControl);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            LogUtils.e("bb-cache", "离线时缓存时间设置");
            CacheControl FORCE_CACHE = new CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(OFFLINE_TIME, TimeUnit.SECONDS)//这里是7s，CacheControl.FORCE_CACHE--是int型最大值
                    .build();
            request = request.newBuilder()
                    .cacheControl(FORCE_CACHE)//此处设置了7秒---修改了系统方法
                    .build();

            Response response = chain.proceed(request);
            //下面注释的部分设置也没有效果，因为在上面已经设置了
            return response.newBuilder().build();
        }
    }
}
