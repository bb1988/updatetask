package com.tytzy.ui.multitype.otm;

import com.tytzy.ui.multitype.ItemViewBinder;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
/**
 * 作者: 白勃
 * 时间: 2020/6/8 2:15 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: Process and flow operators for one-to-many.
 */
public interface OneToManyFlow<T> {

    /**
     * Sets some item view binders to the item type.
     *
     * @param binders the item view binders
     * @return end flow operator
     */
    @CheckResult
    @SuppressWarnings("unchecked")
    @NonNull
    OneToManyEndpoint<T> to(@NonNull ItemViewBinder<T, ?>... binders);
}
