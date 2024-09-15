package org.itmo.master.opvp.storage;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public interface IStorageLoader<K, V> {

    Map<K, V> load();

    default CompletableFuture<Map<K, V>> asyncLoad() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return load();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    void save(IStorage<K, V> storage);

    default void asyncSave(IStorage<K, V> storage) {
        CompletableFuture.supplyAsync(() -> {
            try {
                save(storage);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new CompletionException(e);
            }
            return null;
        });
    }
}
