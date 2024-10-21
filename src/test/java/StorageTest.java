import org.itmo.master.opvp.storage.Storage;
import org.itmo.master.opvp.storage.StorageConfiguration;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

class StorageTest {

    @Test
    void testCreateTable() {
        StorageConfiguration config = new StorageConfiguration(1024L,null);
        Storage storage = new Storage(config);

        storage.createTable("testTable");
        assertNotNull(storage.getTable("testTable"));
    }

    @Test
    void testDropTable() {
        StorageConfiguration config = new StorageConfiguration(1024L,null);
        Storage storage = new Storage(config);

        storage.createTable("testTable");
        assertNotNull(storage.getTable("testTable"));

        storage.dropTable("testTable");
        assertNull(storage.getTable("testTable"));
    }

    @Test
    void testGetStorage() {
        StorageConfiguration config = new StorageConfiguration(1024L, null);
        Storage storage = new Storage(config);

        assertTrue(storage.getStorage() instanceof Map);
    }
}
