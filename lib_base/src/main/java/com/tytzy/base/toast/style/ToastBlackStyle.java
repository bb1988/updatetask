package com.tytzy.base.toast.style;

import android.content.Context;

/**
 * 作者: 白勃
 * 时间: 2020/6/16 9:40 AM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 默认黑色样式实现
 */
public class ToastBlackStyle extends BaseToastStyle {

    public ToastBlackStyle(Context context) {
        super(context);
    }

    @Override
    public int getCornerRadius() {
        return dp2px(8);
    }

    @Override
    public int getBackgroundColor() {
        return 0X88000000;
    }

    @Override
    public int getTextColor() {
        return 0XEEFFFFFF;
    }

    @Override
    public float getTextSize() {
        return sp2px(14);
    }

    @Override
    public int getPaddingStart() {
        return dp2px(24);
    }

    @Override
    public int getPaddingTop() {
        return dp2px(16);
    }
}