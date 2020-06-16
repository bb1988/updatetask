package com.tytzy.base.toast;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import java.lang.reflect.Field;



/**
 * 作者: 白勃
 * 时间: 2020/6/16 10:12 AM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: Toast 显示安全处理
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public final class SafeToast extends BaseToast {


    public SafeToast(Application application) {
        super(application);

        // 反射 Toast 中的字段
        try {

            // 获取 mTN 字段对象
            Field mTNField = Toast.class.getDeclaredField("mTN");
            mTNField.setAccessible(true);
            Object mTN = mTNField.get(this);

            // 获取 mTN 中的 mHandler 字段对象
            Field mHandlerField = mTNField.getType().getDeclaredField("mHandler");
            mHandlerField.setAccessible(true);
            Handler mHandler = (Handler) mHandlerField.get(mTN);

            // 偷梁换柱
            mHandlerField.set(mTN, new SafeHandler(mHandler));

        } catch (IllegalAccessException | NoSuchFieldException ignored) {}
    }
}