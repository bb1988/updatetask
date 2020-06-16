package com.tytzy.network;

import android.annotation.SuppressLint;
import android.content.Context;

import com.tytzy.network.gson.GsonSerializer;
import com.tytzy.network.interceptor.CacheInterceptor;
import com.tytzy.network.interceptor.HeaderInterceptor;
import com.tytzy.network.interceptor.logging.Level;
import com.tytzy.network.interceptor.logging.LoggingInterceptor;
import com.tytzy.network.scalars.ScalarsConverterFactory;
import com.tytzy.network.utils.NetConstant;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.LogUtils;

/**
 * 作者: 白勃
 * 时间: 2020/5/23 10:38 AM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: NetWork入口类，需在应用启动时装载
 */

public class NetWork {

    //App上下文
    private Context context;

    //默认地址
    private String baseUrl = NetConstant.BASE_URL;
    //连接超时 单位秒
    private int connectTimeout = NetConstant.TIMEOUT;
    //读超时 单位秒
    private int readTimeout = NetConstant.TIMEOUT;
    //写超时 单位秒
    private int writeTimeout = NetConstant.TIMEOUT;
    //请求头
    private Map<String, String> headers;
    //是否开启Log打印
    private boolean isLog = false;

    //是否开启连接池
    private boolean isSetPools = false;
    //连接池数
    private int connectionPoolsSize = NetConstant.CONNECTION_POOL_SIZE;
    //单个连接保持时间 单位秒
    private int keepLive = NetConstant.POOL_KEEP_LIVE;

    //缓存配置
    //是否开启缓存
    private boolean isSetCache = false;
    //缓存地址
    private String cacheUrl = NetConstant.CACHE;
    //缓存文件大小 单位 m
    private int cacheSize = NetConstant.CACHE_SIZE;
    //在线缓存时间 单位 秒
    private int onlineTime = NetConstant.ONLINE_TIME;
    //离线缓存时间 单位 秒
    private int offlineTime = NetConstant.OFFLINE_TIME;

    //并发情况设置
    //并发请求最大数量
    private int maxRequests = NetConstant.MAX_REQUESTS;
    //单机请求并发数
    private int maxRequestsPerHost = NetConstant.MAX_REQUESTS_PER_HOST;

    //全局OkHttpClient
    private OkHttpClient okHttpClient;
    //全局Retrofit配置
    private Retrofit retrofit;

    //上传Retrofit配置
    private Retrofit uploadRetrofit;
    //下载Retrofit配置
    private Retrofit downloadRetrofit;

    //全局NetWork
    @SuppressLint("StaticFieldLeak")
    private static NetWork mNetwork;

    public static NetWork init(Context context) {
        if (mNetwork == null) {
            synchronized (NetWork.class) {
                if (mNetwork == null) {
                    mNetwork = new NetWork(context);
                }
            }
        }
        return mNetwork;
    }

    //构造方法私有
    private NetWork(Context context) {
        this.context = context;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public NetWork baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public NetWork connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public NetWork readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public NetWork writeTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public NetWork headers(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    public NetWork withLog(boolean isLog) {
        this.isLog = isLog;
        return this;
    }

    public NetWork withPool(boolean isSetPools) {
        this.isSetPools = isSetPools;
        return this;
    }

    public NetWork connectionPoolsSize(int connectionPoolsSize) {
        this.connectionPoolsSize = connectionPoolsSize;
        return this;
    }

    public NetWork keepLive(int keepLive) {
        this.keepLive = keepLive;
        return this;
    }

    public NetWork withCache(boolean isSetCache) {
        this.isSetCache = isSetCache;
        return this;
    }

    public NetWork cacheUrl(String cacheUrl) {
        this.cacheUrl = cacheUrl;
        return this;
    }

    public NetWork cacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
        return this;
    }

    public NetWork onlineTime(int onlineTime) {
        this.onlineTime = onlineTime;
        return this;
    }

    public NetWork offlineTime(int offlineTime) {
        this.offlineTime = offlineTime;
        return this;
    }

    public NetWork maxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
        return this;
    }

    public NetWork maxRequestsPerHost(int maxRequestsPerHost) {
        this.maxRequestsPerHost = maxRequestsPerHost;
        return this;
    }

    public void go() {
        okHttpClient = getCommonClient();
        retrofit = getRetrofit(baseUrl);
    }


    /**
     * 作者: 白勃
     * 时间: 2020/5/22 2:21 PM
     * 版权: Copyright © 2020 BB Inc. All Rights Reserved
     * 描述: 缓存地址
     *
     * @param context
     */
    @Nullable
    private Cache getCache(@NotNull Context context, String cacheUrl, int cacheSize) {
        File httpCacheDirectory = new File(context.getCacheDir(), cacheUrl);
        try {
            return new Cache(httpCacheDirectory, cacheSize * 1024 * 1024);
        } catch (Exception e) {
            LogUtils.e("OKHttp", "Could not create http cache");
            return null;
        }
    }


    @NotNull
    private OkHttpClient getCommonClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //ssl证书相关
//        SSLUtils.SSLParams sslParams = SSLUtils.getSslSocketFactory();
//        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//        builder.hostnameVerifier(SSLUtils.UnSafeHostnameVerifier);
        //cookies
//        builder.cookieJar(new CookieJarImpl(new PersistentCookieStore(context)));

        //设置header
        if (headers != null && headers.size() > 0) {
            builder.addInterceptor(new HeaderInterceptor(headers));
        }

        //设置Log
        if (isLog) {
            builder.addInterceptor(new LoggingInterceptor
                            .Builder()//构建者模式
                            .loggable(BuildConfig.DEBUG) //是否开启日志打印
                            .setLevel(Level.BASIC) //打印的等级
                            .log(Platform.INFO) // 打印类型
                            .request("Request") // request的Tag
                            .response("Response")// Response的Tag
//                          .addHeader("log-header", "I am the log request header.") // 添加打印头, 注意 key 和 value 都不能是中文
                            .build()
            );
        }

        //设置缓存
        if (isSetCache) {
            //设置缓存拦截器
            builder.addInterceptor(new CacheInterceptor.Builder()
                    .offLineTime(offlineTime)
                    .onlineTime(onlineTime)
                    .setContext(context)
                    .build()
            );
            //设置缓存空间
            builder.cache(getCache(context, cacheUrl, cacheSize));
        }

        //设置各种超时时间
        builder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        builder.readTimeout(readTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(writeTimeout, TimeUnit.SECONDS);

        //设置连接池
        if (isSetPools) {
            builder.connectionPool(new ConnectionPool(connectionPoolsSize, keepLive, TimeUnit.SECONDS));
        }

        OkHttpClient client = builder.build();
        //设置并发请求限流
        //设置最大并发请求数
        client.dispatcher().setMaxRequests(maxRequests);
        //设置单主机最大并发请求数
        client.dispatcher().setMaxRequestsPerHost(maxRequestsPerHost);

        return client;
    }

    @NotNull
    private Retrofit.Builder getCommonBuilder() {
        if (okHttpClient == null) {
            throw new RuntimeException("okHttpClient is null!");
        }
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient);
        builder.addConverterFactory(ScalarsConverterFactory.create());
        builder.addConverterFactory(GsonConverterFactory.create(GsonSerializer.get().getGson()));
        builder.addCallAdapterFactory(RxJava3CallAdapterFactory.create());
        return builder;
    }

    private  Retrofit getRetrofit(String url) {
        return getCommonBuilder().baseUrl(url).build();
    }

    /**
     * 作者: 白勃
     * 时间: 2020/5/22 4:13 PM
     * 版权: Copyright © 2020 BB Inc. All Rights Reserved
     * 描述: 创建Service
     *
     * @param service
     */
    public static <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        if(mNetwork == null){
            throw new RuntimeException("Network need init!");
        }
        if(mNetwork.retrofit == null){
            throw new RuntimeException("Network need go!");
        }
        return mNetwork.retrofit.create(service);
    }

    /**
     * 作者: 白勃
     * 时间: 2020/5/22 4:13 PM
     * 版权: Copyright © 2020 BB Inc. All Rights Reserved
     * 描述: 创建外部链接Service,因为链接可能不固定,每次创建新的retrofit,其他配置公用
     *
     * @param service
     */
    public static <T> T create(final Class<T> service, String baseUrl) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        if(mNetwork == null){
            throw new RuntimeException("Network need init!");
        }
        return mNetwork.getRetrofit(baseUrl).create(service);
    }
}
