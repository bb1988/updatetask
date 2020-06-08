package com.tytzy.ui.multitype.linker;

import androidx.annotation.NonNull;
import com.tytzy.ui.multitype.ItemViewBinder;
import com.tytzy.ui.multitype.otm.OneToManyEndpoint;


/**
 * 作者: 白勃
 * 时间: 2020/6/8 1:49 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: An interface to link the items and binders by the classes of binders.
 * @param
 */
public interface ClassLinker<T> {

    /**
     * Returns the class of your registered binders for your item.
     *
     * @param position The position in items
     * @param t The item
     * @return The index of your registered binders
     * @see OneToManyEndpoint#withClassLinker(ClassLinker)
     */
    @NonNull
    Class<? extends ItemViewBinder<T, ?>> index(int position, @NonNull T t);
}
