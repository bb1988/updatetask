package com.tytzy.base.toast.interceptor;

import android.widget.Toast;

/**
 * 作者: 白勃
 * 时间: 2020/6/16 9:56 AM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: Toast 默认拦截器
 */
public class ToastInterceptor implements IToastInterceptor {

    @Override
    public boolean intercept(Toast toast, CharSequence text) {
        // 如果是空对象或者空文本就进行拦截
        return text == null || "".equals(text.toString());
    }
}