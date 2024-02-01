package ru.clevertec.ecl.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class LFUTest {
    @Autowired
    private LFU<String, String> lfuCache;

    @Test
    public void testPutAndGet() {
        lfuCache.put("key1", "value1");
        String value = lfuCache.get("key1");
        assertEquals("value1", value);
    }

    @Test
    public void testRemove() {
        lfuCache.put("key2", "value2");
        lfuCache.remove("key2");
        String value = lfuCache.get("key2");
        assertNull(value);
    }

    @Test
    public void testEvict() {
        for (int i = 0; i < 10; i++) {
            lfuCache.put("key" + i, "value" + i);
        }
        // Access some keys to change their frequency
        lfuCache.get("key1");
        lfuCache.get("key1");
        lfuCache.get("key2");

        // Add one more key to trigger eviction
        lfuCache.put("key10", "value10");

        // Check that the least frequently used key ("key0") has been evicted
        String value = lfuCache.get("key0");
        assertNull(value);
    }
}