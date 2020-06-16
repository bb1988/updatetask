package com.tytzy.base.toast.style;

import android.content.Context;

/**
 * 作者: 白勃
 * 时间: 2020/6/16 9:41 AM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 默认白色样式实现
 */
public class ToastWhiteStyle extends ToastBlackStyle {

    public ToastWhiteStyle(Context context) {
        super(context);
    }

    @Override
    public int getBackgroundColor() {
        return 0XFFEAEAEA;
    }

    @Override
    public int getTextColor() {
        return 0XBB000000;
    }
}