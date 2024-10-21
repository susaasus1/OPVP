package org.itmo.master.opvp.storage;

import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

//String String
public interface ITable<K, V> {

    List<V> getAll();

    V get(K key);

    @Nullable
    V getIfPresent(K key);

    void put(K key, V value);

    void add(K key, V value);

    void update(K key, V value);

    void remove(K key);

    void clear();

    Map<String, String> getDataTable();

}