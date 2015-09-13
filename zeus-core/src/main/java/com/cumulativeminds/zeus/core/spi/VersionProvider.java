package com.cumulativeminds.zeus.core.spi;

public interface VersionProvider {
    Version fromText(String version);

    boolean isUnspecified(Version version);
}
