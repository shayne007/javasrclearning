package net.mindview.util;

public class Generated {
    public static <T> T[] array(T[] a, Generator<T> gen) {
        for(int i = 0; i < a.length; i++)
            a[i] = gen.next();
        return a;
    }

    public static <T> T[] array(Class<T> type, Generator<T> gen, int size) {
        @SuppressWarnings("unchecked")
        T[] a = (T[]) java.lang.reflect.Array.newInstance(type, size);
        for(int i = 0; i < size; i++)
            a[i] = gen.next();
        return a;
    }
} 