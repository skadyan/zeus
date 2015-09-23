package com.cumulativeminds.zeus.core.spi;

import java.util.List;

import com.cumulativeminds.zeus.core.meta.PropertySource;

public interface SourceKey {
    Object get(String id);

    List<PropertySource> getSourceIdentifiers();
}
