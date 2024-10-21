package org.itmo.master.opvp.storage;

import java.util.Map;

public interface IStorage {

    void createTable(String tableName);

    void dropTable(String tableName);

    ITable<String, String> getTable(String tableName);

    Map<String, Map<String, String>> getStorage();

    StorageConfiguration getConfig();
}
