package org.itmo.master.opvp.storage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

public interface IStorageLoader<K, V> {

    IStorage<K, V> load(StorageConfiguration config);

    default CompletableFuture<IStorage<K, V>> asyncLoad(Executor executor, StorageConfiguration config) {
        return CompletableFuture.supplyAsync(() -> load(config), executor)
                .exceptionally(ex -> {
                    throw new CompletionException(ex);
                });
    }

    void save(IStorage<K, V> storage);

    default CompletableFuture<Void> asyncSave(IStorage<K, V> storage, Executor executor) {
        return CompletableFuture.runAsync(() -> save(storage), executor)
                .exceptionally(ex -> {
                    throw new CompletionException(ex);
                });
    }
}
