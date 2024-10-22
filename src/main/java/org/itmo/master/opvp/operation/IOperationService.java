package org.itmo.master.opvp.operation;

import jakarta.annotation.Nullable;

import java.util.List;

public interface IOperationService {


    void createTable(String tableName);

    void dropTable(String tableName);

    List<String> getAllTables();

    List<String> getAll(String tableName);

    String get(String tableName, String key);

    @Nullable
    String getIfPresent(String tableName, String key);

    void add(String tableName, String key, String value);

    void put(String tableName, String key, String value);

    void update(String tableName, String key, String value);

    void remove(String tableName, String key);

    void clear(String tableName);

}
