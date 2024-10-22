import org.itmo.master.opvp.storage.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StorageLoaderTest {
    private StorageLoader storageLoader;
    private StorageConfiguration config;

    @BeforeEach
    void setUp() {
        storageLoader = new StorageLoader();
        config = new StorageConfiguration(1024L, "db.txt"); // 1 KB limit
    }

    @Test
    void testLoad_WhenFileExists_ShouldLoadStorage() throws IOException {
        Storage storage = new Storage(config);
        storage.createTable("table1");
        storage.getTable("table1").put("key1", "value1");
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("src/main/resources/" + config.filePath()))) {

            oos.writeObject(storage.getStorage());
        }

        Storage loadedStorage = (Storage) storageLoader.load(config);
        System.out.println(storage.getStorage());
        assertNotNull(loadedStorage);
        assertEquals("value1", loadedStorage.getTable("table1").get("key1"));
    }



    @Test
    void testSave_ShouldSaveStorage() {
        Storage storage = new Storage(config);
        storage.createTable("table1");
        storage.getTable("table1").put("key1", "value1");
        storageLoader.save(storage, config);

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("src/main/resources/"+ config.filePath()))) {

            Map<String, Map<String, String>> savedData = (Map<String, Map<String, String>>) ois.readObject();
            assertEquals("value1", savedData.get("table1").get("key1"));

        } catch (IOException | ClassNotFoundException e) {
            fail("Failed to load saved data");
        }
    }

}
