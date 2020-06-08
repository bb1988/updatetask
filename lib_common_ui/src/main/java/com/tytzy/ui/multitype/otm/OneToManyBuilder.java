package com.tytzy.ui.multitype.otm;

import com.tytzy.ui.multitype.ItemViewBinder;
import com.tytzy.ui.multitype.MultiTypeAdapter;
import com.tytzy.ui.multitype.linker.ClassLinker;
import com.tytzy.ui.multitype.linker.ClassLinkerWrapper;
import com.tytzy.ui.multitype.linker.Linker;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import static com.tytzy.ui.multitype.Preconditions.checkNotNull;
/**
 * 作者: 白勃
 * 时间: 2020/6/8 2:28 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: OneToManyBuilder
 */

public class OneToManyBuilder<T> implements OneToManyFlow<T>, OneToManyEndpoint<T> {
    private final @NonNull
    MultiTypeAdapter adapter;
    private final @NonNull Class<? extends T> clazz;
    private ItemViewBinder<T, ?>[] binders;


    public OneToManyBuilder(@NonNull MultiTypeAdapter adapter, @NonNull Class<? extends T> clazz) {
        this.clazz = clazz;
        this.adapter = adapter;
    }


    @Override @CheckResult
    @SafeVarargs
    public final @NonNull OneToManyEndpoint<T> to(@NonNull ItemViewBinder<T, ?>... binders) {
        checkNotNull(binders);
        this.binders = binders;
        return this;
    }


    @Override
    public void withLinker(@NonNull Linker<T> linker) {
        checkNotNull(linker);
        doRegister(linker);
    }


    @Override
    public void withClassLinker(@NonNull ClassLinker<T> classLinker) {
        checkNotNull(classLinker);
        doRegister(ClassLinkerWrapper.wrap(classLinker, binders));
    }


    private void doRegister(@NonNull Linker<T> linker) {
        for (ItemViewBinder<T, ?> binder : binders) {
            adapter.register(clazz, binder, linker);
        }
    }
}
