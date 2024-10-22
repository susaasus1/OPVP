package org.itmo.master.opvp.storage;

import org.itmo.master.opvp.storage.exception.StorageLoaderException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class StorageLoader implements IStorageLoader {

    @Override
    public IStorage load(StorageConfiguration config) {
        IStorage storage;

        try (ObjectInputStream ois = new ObjectInputStream(
                getClass().getClassLoader().getResourceAsStream(config.filePath()))) {
            Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) ois.readObject();
            storage = new Storage(map, config);
            return storage;
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageLoaderException("Не возможно загрузить данные!");
        }
    }

    @Override
    public void save(IStorage storage, StorageConfiguration config) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("src/main/resources/" + config.filePath()))) {

            oos.writeObject(storage.getStorage());
        } catch (IOException e) {
            throw new StorageLoaderException("Не возможно сохранить данные!");
        }
    }
}
