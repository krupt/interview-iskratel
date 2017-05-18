package ru.iskratel.server.util;

public interface Cache<K, V> {

    V get(K key);

    void put(K key, V value);

    void clear();
}
