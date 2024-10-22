package org.itmo.master.opvp.storage;

import jakarta.annotation.Nullable;
import ru.tinkoff.kora.config.common.annotation.ConfigSource;

@ConfigSource("storage")
public record StorageConfiguration(Long maxSizeByte,
                                   @Nullable String filePath){}
