package com.tytzy.base.toast.interceptor;

import android.widget.Toast;

/**
 * 作者: 白勃
 * 时间: 2020/6/16 9:45 AM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: Toast 拦截器接口
 */
public interface IToastInterceptor {

    /**
     * 根据显示的文本决定是否拦截该 Toast
     */
    boolean intercept(Toast toast, CharSequence text);
}