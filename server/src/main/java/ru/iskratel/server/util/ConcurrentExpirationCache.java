package ru.iskratel.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentExpirationCache<K, V> implements Cache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(ConcurrentExpirationCache.class);

    private final Map<K, ExpirationValue> map = new ConcurrentHashMap<>();
    private final long expirationTimeMillis;

    public ConcurrentExpirationCache(long expirationTime, TimeUnit unit) {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            log.debug("Cleanup started");
            Iterator<Map.Entry<K, ExpirationValue>> entryIterator = map.entrySet().iterator();
            while (entryIterator.hasNext()) {
                Map.Entry<K, ExpirationValue> entry = entryIterator.next();
                if (entry.getValue().isExpired()) {
                    entryIterator.remove();
                    log.debug("Removed expired entry with key = {}, value = {}", entry.getKey(), entry.getValue());
                }
            }
        }, expirationTime, expirationTime / 2, unit);
        this.expirationTimeMillis = unit.toMillis(expirationTime);
    }

    public V get(K key) {
        ExpirationValue expirationValue = map.get(key);
        if (expirationValue != null) {
            if (!expirationValue.isExpired()) {
                expirationValue.lastAccessTime = now();
                return expirationValue.value;
            }
        }
        return null;
    }

    public void put(K key, V value) {
        Objects.requireNonNull(value, "Value can't be null");
        map.put(key, new ExpirationValue(value));
    }

    public Map<K, V> asUnmodifiableMap() {
        Map<K, V> map = new HashMap<>();
        for (Map.Entry<K, ExpirationValue> entry : this.map.entrySet()) {
            if (!entry.getValue().isExpired()) {
                map.put(entry.getKey(), entry.getValue().value);
            }
        }
        return Collections.unmodifiableMap(map);
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    private class ExpirationValue {

        private final V value;

        private volatile long lastAccessTime;

        private ExpirationValue(V value) {
            lastAccessTime = now();
            this.value = value;
        }

        private boolean isExpired() {
            return now() > lastAccessTime + expirationTimeMillis;
        }

        @Override
        public String toString() {
            return "Value{value = " + value +
                    ", lastAccess = " + LocalDateTime.ofInstant(new Date(lastAccessTime).toInstant(), ZoneId.systemDefault()) +
                    "}";
        }
    }
}
