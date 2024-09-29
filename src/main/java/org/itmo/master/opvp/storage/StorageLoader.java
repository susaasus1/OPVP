package org.itmo.master.opvp.storage;

import java.io.*;
import java.util.HashMap;


public class StorageLoader implements IStorageLoader {

    @Override
    public IStorage load(StorageConfiguration config) {
        IStorage storage;

        try (ObjectInputStream ois = new ObjectInputStream(
                getClass().getClassLoader().getResourceAsStream(config.filePath()))) {
            System.out.println();
            HashMap<String, String> map = (HashMap<String, String>) ois.readObject();
            storage = new InMemoryStorage(map, config);
            return storage;
        } catch (IOException | ClassNotFoundException e) {
            new RuntimeException("Не возможно загрузить данные!");
        }

        return null;
    }

    @Override
    public void save(IStorage storage) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("src/main/resources/" + storage))) {

            oos.writeObject(storage.getStorage());
        } catch (IOException e) {
            new RuntimeException("Не возможно сохранить данные!");
        }
    }
}
