package org.itmo.master.opvp.storage;

import jakarta.annotation.Nullable;
import org.itmo.master.opvp.storage.exception.StorageNotFoundException;
import org.itmo.master.opvp.storage.exception.StorageOverflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.kora.common.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 * Реализация предназначена только для x64.
 * </p>
 * <p>
 * Реализация String/String хранилища.
 * </p>
 */
public final class InMemoryStorage<K, V> implements IStorage {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryStorage.class);

    private final Map<String, String> storage;
    private final StorageConfiguration config;
    private final long maxMemoryUsage;
    private final AtomicLong currentMemoryUsage = new AtomicLong(0);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();


    public InMemoryStorage(StorageConfiguration config) {

        this.storage = new ConcurrentHashMap<>();
        this.config = config;
        this.maxMemoryUsage = config.maxSizeByte();
    }

    public InMemoryStorage(Map<String, String> storage,
                           StorageConfiguration config) {

        this.storage = storage;
        this.config = config;
        this.maxMemoryUsage = config.maxSizeByte();
    }

    @Override
    public List<String> getAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(storage.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String get(Object key) {
        lock.readLock().lock();
        try {
            var value = storage.get(key.toString());
            if (value == null) {
                throw new StorageNotFoundException("Объект с ключом '" + key + "' не найден");
            }
            return value;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Nullable
    @Override
    public String getIfPresent(Object key) {
        lock.readLock().lock();
        try {
            return storage.get(key.toString());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void put(Object key, Object value) {
        lock.writeLock().lock();
        try {
            long entrySize = estimateSizeInBytes(key.toString(), value.toString());

            if (currentMemoryUsage.get() + entrySize > maxMemoryUsage) {
                throw new StorageOverflowException("Превышен лимит памяти в " + maxMemoryUsage / (1024 * 1024 * 1024) + " ГБ");
            }

            storage.put(key.toString(), value.toString());
            currentMemoryUsage.addAndGet(entrySize);
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
                throw new StorageNotFoundException("Значение с ключем '" + key + "' уже существует");
            }

            storage.put(key.toString(), value.toString());
            currentMemoryUsage.addAndGet(entrySize);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void update(Object key, Object value) {
        lock.writeLock().lock();
        try {
            String keyString = key.toString();
            String oldValue = storage.get(keyString);

            if (oldValue == null) {
                throw new StorageNotFoundException("Объект с ключом '" + key + "' не найден");
            }

            long oldEntrySize = estimateSizeInBytes(keyString, oldValue);
            long newEntrySize = estimateSizeInBytes(keyString, value.toString());

            if (currentMemoryUsage.get() - oldEntrySize + newEntrySize > maxMemoryUsage) {
                throw new StorageOverflowException("Превышен лимит памяти в " + maxMemoryUsage / (1024 * 1024 * 1024) + " ГБ");
            }

            storage.put(keyString, value.toString());
            currentMemoryUsage.addAndGet(newEntrySize - oldEntrySize);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(Object key) {
        lock.writeLock().lock();
        try {
            String keyString = key.toString();
            String oldValue = storage.remove(keyString);

            if (oldValue != null) {
                long entrySize = estimateSizeInBytes(keyString, oldValue);
                currentMemoryUsage.addAndGet(-entrySize);
            } else {
                throw new StorageNotFoundException("Объект с ключом '" + key + "' не найден");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            storage.clear();
            currentMemoryUsage.set(0);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Map<String, String> getStorage() {
        return storage;
    }

    @Override
    public StorageConfiguration getConfig() {
        return config;
    }

    public long getMemoryUsage(){
        return currentMemoryUsage.get();
    }

    /**
     * <p>
     * Потребление памяти для объекта String в 64-битной JVM:
     * <ul>
     *   <li>16 байт - заголовок объекта</li>
     *   <li>4 байта - ссылка на массив байтов</li>
     *   <li>4 байта - хэш-код </li>
     *   <li>4 байта - длина строки</li>
     *   <li>2 байта на символ (так как UTF16)</li>
     * </ul>
     * <p>
     * Также объекты выравниваются до 8 байт.
     * </p>
     */
    private long estimateStringSize(String str) {
        if (str == null) {
            return 0;
        }
        long sizeWithoutLeveling = 16 + 4 + 4 + 4 + (str.length() * 2L);
        long leveling = 8 - sizeWithoutLeveling % 8;
        return sizeWithoutLeveling + (leveling == 8 ? 0 : leveling);
    }

    private long estimateSizeInBytes(String key, String value) {
        long size = 0;

        if (key != null) {
            size += estimateStringSize(key);
        }

        if (value != null) {
            size += estimateStringSize(value);
        }

        return size;
    }

}
