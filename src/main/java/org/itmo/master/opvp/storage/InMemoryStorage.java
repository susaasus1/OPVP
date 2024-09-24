package org.itmo.master.opvp.storage;

import jakarta.annotation.Nullable;
import org.itmo.master.opvp.storage.storage.StorageOverflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.kora.common.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
//Реализация для String/String хранилища
@Component
public final class InMemoryStorage<K, V> implements IStorage {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryStorage.class);
    private final Map<String, String> storage = new ConcurrentHashMap<>();
    private final long maxMemoryUsage;
    private final AtomicLong currentMemoryUsage = new AtomicLong(0);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public InMemoryStorage(StorageConfiguration config) {
        this.maxMemoryUsage = config.maxSizeByte();
    }

    @Override
    public List<String> getAll() {
        return List.of();
    }

    @Override
    public String get(Object key) {
        lock.readLock().lock();
        try{
            var value = storage.get(key);
            if (value != null) {
                return value;
            }
        } finally {
            lock.readLock().unlock();
        }
        logger.error("Object with key '{}' not found", key);
        //miss counter??
        return null;
    }

    @Nullable
    @Override
    public V getIfPresent(Object key) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        lock.writeLock().lock();
        try {
            
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void add(Object key, Object value) {
        lock.writeLock().lock();
        try {
            long entrySize = estimateSizeInBytes(key.toString(), value.toString());

            if (currentMemoryUsage.get() + entrySize > maxMemoryUsage) {
                throw new StorageOverflowException("Превышен лимит памяти в " + maxMemoryUsage / (1024 * 1024 * 1024) + " ГБ");
            }

            String oldValue = storage.getOrDefault(key.toString(), null);

            if (oldValue != null) {
                //TODO сделать exception
                throw new RuntimeException("Значение с таким ключем уже существует");
            }

            currentMemoryUsage.addAndGet(entrySize);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void update(Object key, Object value) {

    }

    @Override
    public void remove(Object key) {

    }

    @Override
    public void clear() {

    }

    private long estimateStringSize(String str) {
        if (str == null) {
            return 0;
        }

        long stringObjectOverhead = 16;
        long charArrayOverhead = 16 + (str.length() * 2L);

        return stringObjectOverhead + charArrayOverhead;
    }

    private long estimateSizeInBytes(String key, String value) {
        long size = 0;

        if (key instanceof String) {
            size += estimateStringSize((String) key);
        }

        if (value instanceof String) {
            size += estimateStringSize((String) value);
        }

        return size;
    }
}
