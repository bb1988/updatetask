package com.tytzy.ui.multitype;

import com.tytzy.ui.multitype.linker.Linker;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import static com.tytzy.ui.multitype.Preconditions.checkNotNull;

/**
 * 作者: 白勃
 * 时间: 2020/6/8 3:17 PM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: An List implementation of TypePool.
 */
public class MultiTypePool implements TypePool {

    private final @NonNull
    List<Class<?>> classes;
    private final @NonNull List<ItemViewBinder<?, ?>> binders;
    private final @NonNull List<Linker<?>> linkers;


    /**
     * Constructs a MultiTypePool with default lists.
     */
    public MultiTypePool() {
        this.classes = new ArrayList<>();
        this.binders = new ArrayList<>();
        this.linkers = new ArrayList<>();
    }


    /**
     * Constructs a MultiTypePool with default lists and a specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     */
    public MultiTypePool(int initialCapacity) {
        this.classes = new ArrayList<>(initialCapacity);
        this.binders = new ArrayList<>(initialCapacity);
        this.linkers = new ArrayList<>(initialCapacity);
    }


    /**
     * Constructs a MultiTypePool with specified lists.
     *
     * @param classes the list for classes
     * @param binders the list for binders
     * @param linkers the list for linkers
     */
    public MultiTypePool(
            @NonNull List<Class<?>> classes,
            @NonNull List<ItemViewBinder<?, ?>> binders,
            @NonNull List<Linker<?>> linkers) {
        checkNotNull(classes);
        checkNotNull(binders);
        checkNotNull(linkers);
        this.classes = classes;
        this.binders = binders;
        this.linkers = linkers;
    }


    @Override
    public <T> void register(
            @NonNull Class<? extends T> clazz,
            @NonNull ItemViewBinder<T, ?> binder,
            @NonNull Linker<T> linker) {
        checkNotNull(clazz);
        checkNotNull(binder);
        checkNotNull(linker);
        classes.add(clazz);
        binders.add(binder);
        linkers.add(linker);
    }


    @Override
    public boolean unregister(@NonNull Class<?> clazz) {
        checkNotNull(clazz);
        boolean removed = false;
        while (true) {
            int index = classes.indexOf(clazz);
            if (index != -1) {
                classes.remove(index);
                binders.remove(index);
                linkers.remove(index);
                removed = true;
            } else {
                break;
            }
        }
        return removed;
    }


    @Override
    public int size() {
        return classes.size();
    }


    @Override
    public int firstIndexOf(@NonNull final Class<?> clazz) {
        checkNotNull(clazz);
        int index = classes.indexOf(clazz);
        if (index != -1) {
            return index;
        }
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).isAssignableFrom(clazz)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public @NonNull Class<?> getClass(int index) {
        return classes.get(index);
    }


    @Override
    public @NonNull ItemViewBinder<?, ?> getItemViewBinder(int index) {
        return binders.get(index);
    }


    @Override
    public @NonNull Linker<?> getLinker(int index) {
        return linkers.get(index);
    }
}
