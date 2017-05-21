package ru.iskratel.server.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class FunctionalConcurrencyExpirationCacheTest {

    private static final Logger log = LoggerFactory.getLogger(FunctionalConcurrencyExpirationCacheTest.class);
    private final Cache<String, String> cache = new ConcurrentExpirationCache<>(10, TimeUnit.SECONDS);
    private volatile Throwable threadException;

    @Test
    public void complexConcurrencyTest() throws Throwable {
        cache.put("kovalev", "12345");
        log.debug("Putted \"kovalev\"");
        cache.put("ivanov", "123");
        log.debug("Putted \"ivanov\"");
        log.debug("Started");
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(4000);
                String kovalev = cache.get("kovalev");
                log.debug("Value for \"kovalev\": {}", kovalev);
                assertEquals("12345", kovalev);
                cache.put("kozlov", "543");
                log.debug("Putted \"kozlov\"");
                Thread.sleep(8975);
                String ivanov = cache.get("ivanov");
                log.debug("Value for \"ivanov\": {}", ivanov);
                assertEquals(null, ivanov);
                String kovalev1 = cache.get("kovalev");
                log.debug("Value for \"kovalev\": {}", kovalev1);
                assertEquals("12345", kovalev1);
                cache.put("kovalev", "654321");
                log.debug("Putted new value for \"kovalev\"");
                Thread.sleep(9999);
                String kovalev2 = cache.get("kovalev");
                log.debug("Value for \"kovalev\": {}", kovalev2);
                assertEquals("654321", kovalev2);
            } catch (Exception e) {
                threadException = new AssertionError("Thread execution failed", e);
            }
        });
        thread.start();
        thread.join();
        if (threadException != null) {
            throw threadException;
        }
        Map map = getMapByHack();
        log.debug("Checking map size");
        assertEquals(1, map.size());
        Thread.sleep(3000);
        log.debug("Checking map size");
        assertEquals(1, map.size());
        Thread.sleep(10000);
        log.debug("Checking map size");
        assertEquals(0, map.size());
    }

    private Map getMapByHack() throws IllegalAccessException, NoSuchFieldException {
        Field mapField = cache.getClass().getDeclaredField("map");
        mapField.setAccessible(true);
        return (Map) mapField.get(cache);
    }
}
