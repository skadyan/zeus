package com.cumulativeminds.zeus.core.spi;

import java.util.List;

import com.cumulativeminds.zeus.core.meta.PropertySource;

public interface SourceKey {

    List<PropertySource> getSourceIdentifiers();
}
