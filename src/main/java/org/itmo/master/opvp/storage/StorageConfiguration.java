package org.itmo.master.opvp.storage;

import ru.tinkoff.kora.config.common.annotation.ConfigSource;

@ConfigSource("storage")
public record StorageConfiguration(Long maxSizeByte){}
