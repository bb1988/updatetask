package com.tytzy.network.observer;

import com.tytzy.base.toast.ToastUtils;
import com.tytzy.network.ExceptionHandle;
import com.tytzy.network.listener.UploadProgressListener;
import com.tytzy.network.response.ResponseThrowable;

import io.reactivex.rxjava3.observers.DisposableObserver;

/**
 * 作者: 白勃
 * 时间: 2020/5/23 12:19 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 上传observer
 * @param
 */
public abstract class UpLoadObserver<T> extends DisposableObserver<T> implements UploadProgressListener{

    public UpLoadObserver() {
    }



    @Override
    public void onComplete() {
        unsubscribe();
    }

    @Override
    public void onError(Throwable e) {
        unsubscribe();
        ResponseThrowable rError = ExceptionHandle.handleException(e);
        ToastUtils.show(rError.message);
    }

    @Override
    public void onNext(T t) {
        onResult(t);
    }

    public abstract void onResult(T t);

    @Override
    public void onProgress(long bytesWritten, long contentLength) {
        onProgress((int)(bytesWritten * 100 / contentLength));
    }

    public abstract void onProgress(int percent);

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