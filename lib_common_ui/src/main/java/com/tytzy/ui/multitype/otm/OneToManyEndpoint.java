package com.tytzy.ui.multitype.otm;

import com.tytzy.ui.multitype.linker.ClassLinker;
import com.tytzy.ui.multitype.linker.Linker;

import androidx.annotation.NonNull;
/**
 * 作者: 白勃
 * 时间: 2020/6/8 2:17 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: End-operators for one-to-many.
 */
public interface OneToManyEndpoint<T> {
    /**
     * Sets a linker to link the items and binders by array index.
     *
     * @param linker the row linker
     * @see Linker
     */
    void withLinker(@NonNull Linker<T> linker);

    /**
     * Sets a class linker to link the items and binders by the class instance of binders.
     *
     * @param classLinker the class linker
     * @see ClassLinker
     */
    void withClassLinker(@NonNull ClassLinker<T> classLinker);
}
