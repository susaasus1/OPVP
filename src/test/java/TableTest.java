import org.itmo.master.opvp.storage.MemoryUtil;
import org.itmo.master.opvp.storage.Table;
import org.itmo.master.opvp.storage.exception.StorageNotFoundException;
import org.itmo.master.opvp.storage.exception.StorageOverflowException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class TableTest {

    @Test
    void testPutAndGet() {
        MemoryUtil memoryUtil = new MemoryUtil(1024L);
        Table table = new Table(memoryUtil);

        table.put("key1", "value1");
        assertEquals("value1", table.get("key1"));
    }

    @Test
    void testRemove() {
        MemoryUtil memoryUtil = new MemoryUtil(1024L);
        Table table = new Table(memoryUtil);

        table.put("key1", "value1");
        table.remove("key1");

        assertThrows(StorageNotFoundException.class, () -> table.get("key1"));
    }

    @Test
    void testUpdate() {
        MemoryUtil memoryUtil = new MemoryUtil(1024L);
        Table table = new Table(memoryUtil);

        table.put("key1", "value1");
        table.update("key1", "value2");

        assertEquals("value2", table.get("key1"));
    }

    @Test
    void testMemoryOverflow() {
        MemoryUtil memoryUtil = new MemoryUtil(1L);  // Ограничиваем память до 1 байта
        Table table = new Table(memoryUtil);

        assertThrows(StorageOverflowException.class, () -> table.put("key1", "value1"));
    }

    @Test
    void testClear() {
        MemoryUtil memoryUtil = new MemoryUtil(1024L);
        Table table = new Table(memoryUtil);

        table.put("key1", "value1");
        table.clear();

        assertEquals(0, table.getAll().size());
    }
}
