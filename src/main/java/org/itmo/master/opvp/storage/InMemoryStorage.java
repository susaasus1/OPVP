package org.itmo.master.opvp.storage;

import jakarta.annotation.Nullable;
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

@Component
public final class InMemoryStorage<K, V> implements IStorage {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryStorage.class);

    private final Map<K, V> storage = new ConcurrentHashMap<>();
    private final long maxMemoryUsage;
    private final AtomicLong currentMemoryUsage = new AtomicLong(0);  // Используем AtomicLong
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public InMemoryStorage(StorageConfiguration config) {
        this.maxMemoryUsage = config.maxSizeByte();
    }

    @Override
    public List<V> getAll() {
        return List.of();
    }

    @Override
    public V get(Object key) {
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
    public void add(Object key, Object value) {
        
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

    private long estimateSizeInBytes(K key, V value) {
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
