package org.itmo.master.opvp.storage;

import java.util.List;

//String String
public interface IStorage<K, V> {

    List<V> getAll();

    V get(K key);

    void add(K key, V value);

    void put(K key, V value);

    void remove(K key);

    void clear();


}
