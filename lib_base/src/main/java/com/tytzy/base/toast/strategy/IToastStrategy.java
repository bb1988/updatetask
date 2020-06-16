package com.tytzy.base.toast.strategy;

import android.widget.Toast;

/**
 * 作者: 白勃
 * 时间: 2020/6/16 10:03 AM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: Toast 处理策略
 */
public interface IToastStrategy {


    /** 短吐司显示的时长 */
    int SHORT_DURATION_TIMEOUT = 2000;
    /** 长吐司显示的时长 */
    int LONG_DURATION_TIMEOUT = 3500;

    /**
     * 绑定 Toast 对象
     */
    void bind(Toast toast);

    /**
     * 显示 Toast
     */
    void show(CharSequence text);

    /**
     * 取消 Toast
     */
    void cancel();
}