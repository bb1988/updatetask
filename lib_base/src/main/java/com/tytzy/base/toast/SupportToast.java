package com.tytzy.base.toast;

import android.app.Application;

/**
 * 作者: 白勃
 * 时间: 2020/6/16 10:12 AM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: Toast 无通知栏权限兼容
 */
public final class SupportToast extends BaseToast {


    /** 吐司弹窗显示辅助类 */
    private final ToastHelper mToastHelper;

    public SupportToast(Application application) {
        super(application);
        mToastHelper = new ToastHelper(this, application);
    }

    @Override
    public void show() {
        // 显示吐司
        mToastHelper.show();
    }

    @Override
    public void cancel() {
        // 取消显示
        mToastHelper.cancel();
    }
}