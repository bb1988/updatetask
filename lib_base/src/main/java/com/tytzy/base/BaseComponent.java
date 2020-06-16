package com.tytzy.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tytzy.base.toast.ToastUtils;
import com.tytzy.base.toast.interceptor.ToastInterceptor;
import com.tytzy.base.toast.style.ToastBlackStyle;
import com.tytzy.base.utils.AppUtils;

/**
 * 作者: 白勃
 * 时间: 2020/6/16 10:36 AM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: Base库初始化入口
 */
public final class BaseComponent {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    /**
     * Init, it must be call before used router.
     */
    public static void init(Application application) {
        mContext = application.getApplicationContext();
        AppUtils.init(mContext);

        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "空 Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });
        // 初始化吐司工具类
        ToastUtils.init(application, new ToastBlackStyle(application));
    }

}
