package com.tytzy.network.observer;

import android.app.Dialog;
import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.tytzy.base.ui.prograss.LoadingCancelListener;
import com.tytzy.base.ui.prograss.LoadingDialog;
import com.tytzy.network.ExceptionHandle;
import com.tytzy.network.response.BaseResponse;
import com.tytzy.network.response.ResponseCode;
import com.tytzy.network.response.ResponseThrowable;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import utils.ToastUtils;


/**
 * 作者: 白勃
 * 时间: 2020/5/20 2:12 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 带进度条的返回包装器
 * @param
 */

public abstract class LoadingObserver<T, E> extends DisposableObserver<T> implements LoadingCancelListener {

    private Context context;
    private String msg;
    private Dialog dialog;
    private boolean canShowDialog;
    @Autowired(name = "/ui/LoadingDialog")
    LoadingDialog loadingDialog;

    public LoadingObserver(Context context) {
        init(context, "", false);
    }

    public LoadingObserver(Context context, boolean canShowDialog) {
        init(context, "", canShowDialog);
    }

    public LoadingObserver(Context context, String msg, boolean canShowDialog) {
        init(context, msg, canShowDialog);
    }

    private void init(Context context, String msg, boolean canShowDialog) {
        this.context = context;
        this.msg = msg;
        this.canShowDialog = canShowDialog;
        ARouter.getInstance().inject(this);
    }

    private void showProgressDialog() {
        if (dialog == null) {
            dialog = loadingDialog.createLoadingDialog(context, msg, this);
        } else {
            dialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (dialog != null) {
            loadingDialog.closeDialog(dialog);
            dialog = null;
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if (canShowDialog) {
            showProgressDialog();
        }
    }

    @Override
    public void onNext(@NonNull T t) {
        BaseResponse<E> baseResponse = (BaseResponse<E>) t;
        switch (baseResponse.getCode()) {
            case ResponseCode.CODE_200:
                //请求成功, 正确的操作方式
                onResult(baseResponse.getData());
                break;
            default:
                ToastUtils.showShort("错误代码:", baseResponse.getCode());
                break;
        }
    }

    public abstract void onResult(E t);

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onComplete() {
        if (canShowDialog) {
            dismissProgressDialog();
        }
        unsubscribe();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (canShowDialog) {
            dismissProgressDialog();
        }
        unsubscribe();
        ResponseThrowable rError = ExceptionHandle.handleException(e);
        ToastUtils.showShort(rError.message);
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        onComplete();
    }

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
