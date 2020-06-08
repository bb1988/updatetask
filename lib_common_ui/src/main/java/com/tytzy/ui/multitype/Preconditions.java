package com.tytzy.ui.multitype;

import androidx.annotation.NonNull;

/**
 * 作者: 白勃
 * 时间: 2020/6/8 2:30 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: Preconditions
 */
@SuppressWarnings("WeakerAccess")
public final class Preconditions {

    @SuppressWarnings("ConstantConditions")
    public static @NonNull
    <T> T checkNotNull(@NonNull final T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }


    private Preconditions() {}
}
