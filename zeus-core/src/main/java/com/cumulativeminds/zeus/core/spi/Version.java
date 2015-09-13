package com.cumulativeminds.zeus.core.spi;

public interface Version {
    int X = -999;

    int major();

    int minor();

    boolean isMajorUnspecified();

    boolean isMinorUnspecified();

    String toFormattedString();

    int compareWith(Version o);
}
