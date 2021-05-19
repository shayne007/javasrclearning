package com.feng.concurrency.patterns.tspecstorage;

import java.lang.ref.WeakReference;
import java.util.AbstractMap.SimpleEntry;

/**
 * 能够避免内存泄露的ThreadLocal子类
 * 
 * @author fengsy
 * @date 5/18/21
 * @Description
 * 
 * @param <T>
 *            相应的线程特有对象类型
 */
public class ManagedThreadLocal<T> extends ThreadLocal<T> {
    private final ThreadLocal<SimpleEntry<String, T>> storageHelper;
    private volatile WeakReference<SimpleEntry<String, ?>> wrTSOWrapper;

    {
        storageHelper = new ThreadLocal<SimpleEntry<String, T>>() {
            @Override
            protected SimpleEntry<String, T> initialValue() {
                SimpleEntry<String, T> tsoWrapper = new SimpleEntry<>(null, null);
                wrTSOWrapper = new WeakReference<SimpleEntry<String, ?>>(tsoWrapper);
                return tsoWrapper;
            }
        };

    }

    @Override
    public T get() {
        // entry是一个线程特有对象
        SimpleEntry<String, T> entry = storageHelper.get();
        T v = entry.getValue();
        if (null == v) {
            v = this.initialValue();
            entry.setValue(v);
        }
        return v;
    }

    @Override
    public void set(T value) {
        SimpleEntry<String, T> entry = storageHelper.get();
        // 对线程特有对象进行更新操作无需使用任何线程同步措施
        entry.setValue(value);
    }

    @Override
    public void remove() {
        storageHelper.remove();
    }

    public void destroy() {
        final WeakReference<SimpleEntry<String, ?>> wrTSOWrapper = this.wrTSOWrapper;
        if (null != wrTSOWrapper) {
            SimpleEntry<String, ?> entry = wrTSOWrapper.get();
            if (null != entry) {
                entry.setValue(null);
            }
        }
    }
}
