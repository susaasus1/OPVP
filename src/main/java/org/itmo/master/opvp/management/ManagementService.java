package org.itmo.master.opvp.management;

import jakarta.annotation.Nullable;
import org.itmo.master.opvp.storage.IStorage;
import ru.tinkoff.kora.common.Component;

import java.util.List;

@Component
public final class ManagementService implements IManagementDataService {

    private final IStorage<String, String> storage;

    public ManagementService(IStorage<String, String> storage) {
        this.storage = storage;
    }

    @Override
    public List<String> getAll() {
        return storage.getAll();
    }

    @Override
    public String get(String key) {
        return storage.get(key);
    }

    @Nullable
    @Override
    public String getIfPresent(String key) {
        return storage.getIfPresent(key);
    }

    @Override
    public void add(String key, String value) {
        storage.add(key, value);
    }

    @Override
    public void put(String key, String value) {
        storage.put(key, value);
    }

    @Override
    public void update(String key, String value) {
        storage.update(key, value);
    }

    @Override
    public void remove(String key) {
        storage.remove(key);
    }

    @Override
    public void clear() {
        storage.clear();
    }
}
