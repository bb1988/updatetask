package com.tytzy.ui.multitype.linker;

import androidx.annotation.NonNull;

/**
 * 作者: 白勃
 * 时间: 2020/6/8 2:35 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: DefaultLinker 预设值
 */
public final class DefaultLinker<T> implements Linker<T> {

    @Override
    public int index(int position, @NonNull T t) {
        return 0;
    }
}
