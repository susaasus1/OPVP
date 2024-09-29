import org.itmo.master.opvp.storage.StorageLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.itmo.master.opvp.storage.IStorage;
import org.itmo.master.opvp.storage.InMemoryStorage;
import org.itmo.master.opvp.storage.StorageConfiguration;

import java.io.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class StorageLoaderTest {
    private StorageLoader storageLoader;
    private StorageConfiguration config;
    private IStorage storage;

    @BeforeEach
    void setUp() {
        storageLoader = new StorageLoader();
        config = new StorageConfiguration(1024L, "db.txt"); // 1 GB limit
        storage = new InMemoryStorage(config);
    }

    @Test
    void testLoad_WhenFileExists_ShouldLoadStorage() throws IOException {
        // Prepare a sample file with data
        HashMap<String, String> sampleData = new HashMap<>();
        sampleData.put("key1", "value1");
        sampleData.put("key2", "value2");

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("src/main/resources/" + config.filePath()))) {
            oos.writeObject(sampleData);
        }


        InMemoryStorage loadedStorage = (InMemoryStorage) storageLoader.load(config);
        assertNotNull(loadedStorage);
        assertEquals("value1", loadedStorage.get("key1"));
        assertEquals("value2", loadedStorage.get("key2"));
    }



    @Test
    void testSave_ShouldSaveStorage() {
        storage.put("key1", "value1");
        storageLoader.save(storage);

        // Load the saved data to verify
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("src/main/resources/"+ config.filePath()))) {
            HashMap<String, String> savedData = (HashMap<String, String>) ois.readObject();
            assertEquals("value1", savedData.get("key1"));
        } catch (IOException | ClassNotFoundException e) {
            fail("Failed to load saved data");
        }
    }

}
