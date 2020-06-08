package com.tytzy.ui.multitype.linker;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import com.tytzy.ui.multitype.ItemViewBinder;
import com.tytzy.ui.multitype.otm.OneToManyEndpoint;
import com.tytzy.ui.multitype.otm.OneToManyFlow;

/**
 * 作者: 白勃
 * 时间: 2020/6/8 2:27 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: An interface to link the items and binders by array integer index.
 */
public interface Linker<T> {
    /**
     * Returns the index of your registered binders for your item. The result should be in range of
     * {@code [0, one-to-multiple-binders.length)}.
     *
     * <p>Note: The argument of {@link OneToManyFlow#to(ItemViewBinder[])} is the
     * one-to-multiple-binders.</p>
     *
     * @param position The position in items
     * @param t Your item data
     * @return The index of your registered binders
     * @see OneToManyFlow#to(ItemViewBinder[])
     * @see OneToManyEndpoint#withLinker(com.tytzy.ui.multitype.linker.Linker)
     */
    @IntRange(from = 0) int index(int position, @NonNull T t);
}
