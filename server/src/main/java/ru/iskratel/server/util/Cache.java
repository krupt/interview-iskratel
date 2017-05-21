package ru.iskratel.server.util;

import java.util.Map;

public interface Cache<K, V> {

    V get(K key);

    void put(K key, V value);

    Map<K, V> asUnmodifiableMap();
}
