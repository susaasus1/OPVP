package org.itmo.master.opvp.operation;

import jakarta.annotation.Nullable;

import java.util.List;

public interface IManagementDataService {

    List<String> getAll();

    String get(String key);

    @Nullable
    String getIfPresent(String key);

    void add(String key, String value);

    void update(String key, String value);

    void remove(String key);

    void clear();

}
