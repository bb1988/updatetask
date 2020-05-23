package com.tytzy.network.observer;

import com.tytzy.network.ExceptionHandle;
import com.tytzy.network.listener.DownloadProgressListener;
import com.tytzy.network.response.ResponseThrowable;

import io.reactivex.rxjava3.observers.DisposableObserver;
import utils.ToastUtils;

/**
 * 作者: 白勃
 * 时间: 2020/5/23 12:18 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 下载observer
 * @param
 */
public abstract class DownLoadObserver<T> extends DisposableObserver<T> implements DownloadProgressListener{

    public DownLoadObserver() {
    }

    @Override
    public void onComplete() {
        unsubscribe();
    }

    @Override
    public void onError(Throwable e) {
        unsubscribe();
        ResponseThrowable rError = ExceptionHandle.handleException(e);
        ToastUtils.showShort(rError.message);
    }

    @Override
    public void onNext(T t) {
//        onResult(t);
    }

//    public abstract void onResult(T t);

    @Override
    public void onProgress(long bytesRead, long contentLength, boolean done) {
        onProgress((int)(bytesRead * 100 / contentLength),done);
    }

    public abstract void onProgress(int percent, boolean done);

    /**
     * 作者: 白勃
     * 时间: 2020/5/20 5:39 PM
     * 版权: Copyright © 2020 BB Inc. All Rights Reserved
     * 描述: 取消订阅
     * @param
     */
    private void unsubscribe(){
        if (!isDisposed()) {
            dispose();
        }
    }


}