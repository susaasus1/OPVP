package org.itmo.master.opvp.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Storage implements IStorage {

    private static final Logger logger = LoggerFactory.getLogger(Storage.class);

    private final Map<String, ITable<String, String>> storage;
    private final StorageConfiguration config;
    private final MemoryUtil memoryUtil;

    public Storage(StorageConfiguration config) {

        this.config = config;
        this.memoryUtil = new MemoryUtil(config.maxSizeByte());
        this.storage = new ConcurrentHashMap<>();
    }

    public Storage(Map<String, Map<String, String>> storage,
                   StorageConfiguration config) {

        this.config = config;
        this.memoryUtil = new MemoryUtil(config.maxSizeByte());
        this.storage = convertStorage(storage);
    }

    @Override
    public void createTable(String tableName) {
        storage.put(tableName, new Table(memoryUtil));
    }

    @Override
    public void dropTable(String tableName) {
        storage.remove(tableName);
    }

    @Override
    public ITable<String, String> getTable(String tableName) {
        return storage.get(tableName);
    }

    @Override
    public Map<String, Map<String, String>> getStorage() {
        Map<String, Map<String, String>> data = new HashMap<>();
        for(String key : storage.keySet()) {
            data.put(key, storage.get(key).getDataTable());
        }

        return data;
    }

    @Override
    public List<String> getAllTables() {
        return new ArrayList<>(storage.keySet());
    }

    @Override
    public StorageConfiguration getConfig() {
        return null;
    }

    private Map<String, ITable<String, String>> convertStorage(Map<String, Map<String, String>> storage) {
        Map<String, ITable<String, String>> data = new HashMap<>();
        for(String key : storage.keySet()) {
            data.put(key, new Table(storage.get(key), memoryUtil));
        }
        return data;
    }
}
