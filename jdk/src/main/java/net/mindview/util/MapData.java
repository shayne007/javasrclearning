package net.mindview.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapData<K,V> extends LinkedHashMap<K,V> {
    public MapData(Generator<K> genK, Generator<V> genV, int quantity) {
        for(int i = 0; i < quantity; i++) {
            put(genK.next(), genV.next());
        }
    }

    public static <K,V> MapData<K,V> map(Generator<K> genK, Generator<V> genV, int quantity) {
        return new MapData<>(genK, genV, quantity);
    }
} 