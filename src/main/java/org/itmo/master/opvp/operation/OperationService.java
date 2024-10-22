package org.itmo.master.opvp.operation;

import jakarta.annotation.Nullable;
import org.itmo.master.opvp.storage.*;
import ru.tinkoff.kora.common.Component;

import java.util.List;

@Component
public final class OperationService implements IOperationService {

    private IStorage storage;
    private final IStorageLoader loader = new StorageLoader();
    private final StorageConfiguration config;

    public OperationService(StorageConfiguration configuration,
                            StorageConfiguration config) {
        this.config = config;
        this.storage = new Storage(configuration);
    }

    //Table manipulation
    @Override
    public void createTable(String tableName){
        storage.createTable(tableName);
    }

    @Override
    public void dropTable(String tableName){
        storage.dropTable(tableName);
    }

    @Override
    public List<String> getAllTables() {
        return storage.getAllTables();
    }

    //Data manipulation
    @Override
    public List<String> getAll(String tableName) {
        return storage.getTable(tableName).getAll();
    }

    @Override
    public String get(String tableName, String key) {
        return storage.getTable(tableName).get(key);
    }

    @Nullable
    @Override
    public String getIfPresent(String tableName, String key) {
        return storage.getTable(tableName).getIfPresent(key);
    }

    @Override
    public void add(String tableName, String key, String value) {
        storage.getTable(tableName).add(key, value);
    }

    @Override
    public void put(String tableName, String key, String value) {
        storage.getTable(tableName).put(key, value);
    }

    @Override
    public void update(String tableName, String key, String value) {
        storage.getTable(tableName).update(key, value);
    }

    @Override
    public void remove(String tableName, String key) {
        storage.getTable(tableName).remove(key);
    }

    @Override
    public void clear(String tableName) {
        storage.getTable(tableName).clear();
    }

    public void loadStorage(){
        storage = loader.load(config);
    }

    public void saveStorage(){
        loader.save(storage, config);
    }
}
