package ru.clevertec.ecl.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LRUTest {
    @Autowired
    private LRU<String, String> lruCache;

    @Test
    public void testPutAndGet() {
        lruCache.put("key1", "value1");
        String value = lruCache.get("key1");
        assertEquals("value1", value);
    }

    @Test
    public void testRemove() {
        lruCache.put("key2", "value2");
        lruCache.remove("key2");
        String value = lruCache.get("key2");
        assertNull(value);
    }

    @Test
    public void testEvict() {
        for (int i = 0; i < 10; i++) {
            lruCache.put("key" + i, "value" + i);
        }

        lruCache.get("key1");
        lruCache.get("key2");


        lruCache.put("key10", "value10");

        String value = lruCache.get("key0");
        assertNull(value);
    }
}