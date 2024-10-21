import org.itmo.master.opvp.storage.MemoryUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.wildfly.common.Assert.assertTrue;

class MemoryUtilTest {

    @Test
    void testEstimateStringSize() {
        MemoryUtil memoryUtil = new MemoryUtil(1024L);

        // Проверка строки обычной длины
        long size = memoryUtil.estimateStringSize("test");
        assertEquals(40L, size);

        // Проверка пустой строки
        long sizeEmpty = memoryUtil.estimateStringSize("");
        assertEquals(32L, sizeEmpty);

        // Проверка null
        long sizeNull = memoryUtil.estimateStringSize(null);
        assertEquals(0L, sizeNull);
    }

    @Test
    void testEstimateSizeInBytes() {
        MemoryUtil memoryUtil = new MemoryUtil(1024L);

        // Проверка для key и value
        long size = memoryUtil.estimateSizeInBytes("key", "value");
        assertTrue(size > 0);

        // Проверка для null значений
        long sizeNull = memoryUtil.estimateSizeInBytes(null, null);
        assertEquals(0L, sizeNull);
    }

    @Test
    void testGetMaxMemoryUsage() {
        MemoryUtil memoryUtil = new MemoryUtil(2048L);
        assertEquals(2048L, memoryUtil.getMaxMemoryUsage());
    }

    @Test
    void testGetCurrentMemoryUsage() {
        MemoryUtil memoryUtil = new MemoryUtil(2048L);
        AtomicLong currentUsage = memoryUtil.getCurrentMemoryUsage();
        assertEquals(0L, currentUsage.get());
    }
}