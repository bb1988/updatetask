package com.tytzy.ui.multitype;

import androidx.annotation.NonNull;
/**
 * 作者: 白勃
 * 时间: 2020/6/8 1:44 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: BinderNotFoundException
 * @param
 */
class BinderNotFoundException extends RuntimeException {

    BinderNotFoundException(@NonNull Class<?> clazz) {
        super("Have you registered {className}.class to the binder in the adapter/pool?"
                .replace("{className}", clazz.getSimpleName()));
    }
}
