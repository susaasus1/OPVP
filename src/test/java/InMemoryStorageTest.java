import org.itmo.master.opvp.storage.InMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.itmo.master.opvp.storage.StorageConfiguration;
import org.itmo.master.opvp.storage.exception.StorageOverflowException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryStorageTest {
    private InMemoryStorage<String, String> storage;

    @BeforeEach
    void setUp() {
        StorageConfiguration config = new StorageConfiguration(1024L, null); // 1 KB limit
        storage = new InMemoryStorage<>(config);
    }

    @Test
    void testPutAndGet() {
        storage.put("key1", "value1");
        String value = storage.get("key1");
        assertEquals("value1", value);
    }

    @Test
    void testPut_WhenOverflow_ShouldThrowException() {
        storage.put("key1", "value1");
        storage.put("key2", "value2");

        assertThrows(StorageOverflowException.class, () -> {
            storage.put("key3", "a".repeat(1024)); // 1 ГБ
        });
        storage.clear();
    }

    @Test
    void testGetAll() {
        storage.put("key1", "value1");
        storage.put("key2", "value2");
        List<String> allValues = storage.getAll();
        assertTrue(allValues.contains("value1"));
        assertTrue(allValues.contains("value2"));
        assertEquals(2, allValues.size());
    }

    @Test
    void testGet_NonExistentKey_ShouldThrowException() {
        assertThrows(RuntimeException.class, () -> storage.get("nonExistentKey"));
    }

    @Test
    void testGetIfPresent_ExistingKey() {
        storage.put("key1", "value1");
        String value = storage.getIfPresent("key1");
        assertEquals("value1", value);
    }

    @Test
    void testGetIfPresent_NonExistentKey_ShouldReturnNull() {
        String value = storage.getIfPresent("nonExistentKey");
        assertNull(value);
    }

    @Test
    void testAdd_NewKey() {
        storage.add("key1", "value1");
        assertEquals("value1", storage.get("key1"));
    }

    @Test
    void testAdd_ExistingKey_ShouldThrowException() {
        storage.add("key1", "value1");
        assertThrows(RuntimeException.class, () -> storage.add("key1", "value2"));
    }

    @Test
    void testUpdate_ExistingKey() {
        storage.put("key1", "value1");
        storage.update("key1", "newValue");
        assertEquals("newValue", storage.get("key1"));
    }

    @Test
    void testUpdate_NonExistentKey_ShouldThrowException() {
        assertThrows(RuntimeException.class, () -> storage.update("nonExistentKey", "value"));
    }

    @Test
    void testRemove_ExistingKey() {
        storage.put("key1", "value1");
        storage.remove("key1");
        assertThrows(RuntimeException.class, () -> storage.get("key1"));
    }

    @Test
    void testRemove_NonExistentKey_ShouldThrowException() {
        assertThrows(RuntimeException.class, () -> storage.remove("nonExistentKey"));
    }

    @Test
    void testClear() {
        storage.put("key1", "value1");
        storage.clear();
        assertEquals(0, storage.getAll().size());
    }

}
