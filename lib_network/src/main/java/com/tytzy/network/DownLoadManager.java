package com.tytzy.network;

import android.content.Context;

import com.tytzy.network.gson.GsonSerializer;
import com.tytzy.network.interceptor.DownloadProgressInterceptor;
import com.tytzy.network.listener.DownloadProgressListener;
import com.tytzy.network.observer.DownLoadObserver;
import com.tytzy.network.scalars.ScalarsConverterFactory;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import utils.FileUtils;

/**
 * Created by goldze on 2017/5/11.
 * 文件下载管理，封装一行代码实现下载
 */

public class DownLoadManager {

    private static DownLoadManager instance;

    private static Retrofit retrofit;

    private Context context;


    private DownLoadManager(Context context) {
       this.context = context;
    }

    /**
     * 单例模式
     *
     * @return DownLoadManager
     */
    public static DownLoadManager init(Context context) {
        if (instance == null) {
            synchronized (DownLoadManager.class) {
                if (instance == null) {
                    instance = new DownLoadManager(context);
                }
            }
        }
        return instance;
    }

    //下载
    public void downLoad(String downUrl, DownLoadObserver<ResponseBody> downLoadObserver , File file) {
        buildNetWork(this.context,downLoadObserver::onProgress);
        retrofit.create(DownloadService.class)
                .download(downUrl)
                .subscribeOn(Schedulers.io())//请求网络 在调度者的io线程
                .observeOn(Schedulers.io()) //指定线程保存文件
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        try {
                            FileUtils.writeFile(responseBody.byteStream(), file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                        .from((LifecycleOwner) context, Lifecycle.Event.ON_DESTROY)))
                .subscribe(downLoadObserver);

    }


    private void buildNetWork(Context context,DownloadProgressListener listener) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new DownloadProgressInterceptor(listener))
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonSerializer.get().getGson()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(NetWork.init(context).getBaseUrl())
                .build();
    }


    private interface DownloadService {
        @Streaming
        @GET
        Observable<ResponseBody> download(@Url String url);
    }
}
