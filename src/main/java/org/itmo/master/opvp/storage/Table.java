package org.itmo.master.opvp.storage;

import jakarta.annotation.Nullable;
import org.itmo.master.opvp.storage.exception.StorageNotFoundException;
import org.itmo.master.opvp.storage.exception.StorageOverflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public final class Table implements ITable<String, String> {

    private static final Logger logger = LoggerFactory.getLogger(Table.class);

    private final Map<String, String> table;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final MemoryUtil memoryUtil;
    private final long maxMemoryUsage;
    private final AtomicLong currentTableMemoryUsage = new AtomicLong(0);



    public Table(MemoryUtil memoryUtil) {

        this.memoryUtil = memoryUtil;
        this.maxMemoryUsage = memoryUtil.getMaxMemoryUsage();
        this.table = new ConcurrentHashMap<>();
    }

    public Table(Map<String, String> storage,
                 MemoryUtil memoryUtil) {

        this.memoryUtil = memoryUtil;
        this.maxMemoryUsage = memoryUtil.getMaxMemoryUsage();
        this.table = storage;
    }

    @Override
    public List<String> getAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(table.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String get(String key) {
        lock.readLock().lock();
        try {
            var value = table.get(key);
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
    public String getIfPresent(String key) {
        lock.readLock().lock();
        try {
            return table.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void put(String key, String value) {
        lock.writeLock().lock();
        try {
            long entrySize = memoryUtil.estimateSizeInBytes(key, value);

            if (memoryUtil.getCurrentMemoryUsage().get() + entrySize > maxMemoryUsage) {
                throw new StorageOverflowException("Превышен лимит памяти в " + maxMemoryUsage / (1024 * 1024 * 1024) + " ГБ");
            }

            table.put(key, value);
            memoryUtil.getCurrentMemoryUsage().addAndGet(entrySize);
            currentTableMemoryUsage.addAndGet(entrySize);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void add(String key, String value) {
        lock.writeLock().lock();
        try {
            long entrySize = memoryUtil.estimateSizeInBytes(key, value);

            if (memoryUtil.getCurrentMemoryUsage().get() + entrySize > maxMemoryUsage) {
                throw new StorageOverflowException("Превышен лимит памяти в " + maxMemoryUsage / (1024 * 1024 * 1024) + " ГБ");
            }

            String oldValue = table.getOrDefault(key, null);

            if (oldValue != null) {
                throw new StorageNotFoundException("Значение с ключем '" + key + "' уже существует");
            }

            table.put(key, value);
            memoryUtil.getCurrentMemoryUsage().addAndGet(entrySize);
            currentTableMemoryUsage.addAndGet(entrySize);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void update(String key, String value) {
        lock.writeLock().lock();
        try {
            String oldValue = table.get(key);

            if (oldValue == null) {
                throw new StorageNotFoundException("Объект с ключом '" + key + "' не найден");
            }

            long oldEntrySize = memoryUtil.estimateSizeInBytes(key, oldValue);
            long newEntrySize = memoryUtil.estimateSizeInBytes(key, value);

            if (memoryUtil.getCurrentMemoryUsage().get() - oldEntrySize + newEntrySize > maxMemoryUsage) {
                throw new StorageOverflowException("Превышен лимит памяти в " + maxMemoryUsage / (1024 * 1024 * 1024) + " ГБ");
            }

            table.put(key, value);
            memoryUtil.getCurrentMemoryUsage().addAndGet(newEntrySize - oldEntrySize);
            currentTableMemoryUsage.addAndGet(newEntrySize - oldEntrySize);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(String key) {
        lock.writeLock().lock();
        try {
            String oldValue = table.remove(key);

            if (oldValue != null) {
                long entrySize = memoryUtil.estimateSizeInBytes(key, oldValue);
                memoryUtil.getCurrentMemoryUsage().addAndGet(-entrySize);
                currentTableMemoryUsage.addAndGet(-entrySize);
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
            table.clear();
            memoryUtil.getCurrentMemoryUsage().addAndGet(-currentTableMemoryUsage.get());
            currentTableMemoryUsage.set(0);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Map<String, String> getDataTable() {
        return table;
    }

    public long getCurrentTableMemoryUsage() {
        return currentTableMemoryUsage.get();
    }

}
