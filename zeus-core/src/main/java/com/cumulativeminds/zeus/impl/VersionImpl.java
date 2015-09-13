package com.cumulativeminds.zeus.impl;

import org.springframework.util.StringUtils;

import com.cumulativeminds.zeus.core.spi.Version;

public enum VersionImpl implements Version {
    UNSPECIFIED(X, X),

    V_1_0(1, 0), V_1_1(1, 1),

    V_1_X(1, X);

    private int major;
    private int minor;

    private VersionImpl(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    @Override
    public int major() {
        return major;
    }

    @Override
    public int minor() {
        return minor;
    }

    @Override
    public boolean isMajorUnspecified() {
        return major == X;
    }

    @Override
    public boolean isMinorUnspecified() {
        return minor == X;
    }

    @Override
    public String toString() {
        return String.format("v%s.%s", (isMajorUnspecified() ? "x" : major), (isMinorUnspecified() ? "x" : minor));
    }

    @Override
    public String toFormattedString() {
        return toString();
    }

    public static Version parse(String text) {
        if (StringUtils.isEmpty(text)) {
            return UNSPECIFIED;
        }
        int start = 0;
        if (text.charAt(0) == 'v' || text.charAt(0) == 'V') {
            start = 1;
        }
        String major = "X";
        String minor = "X";

        int dot = text.indexOf('.');
        if (dot > 0) {
            major = text.substring(start, dot);
            minor = text.substring(dot + 1).toUpperCase();
        } else {
            major = text.substring(start);
            minor = "X";
        }

        if (String.valueOf(X).equals(major)) {
            return UNSPECIFIED;
        } else {
            return Enum.valueOf(VersionImpl.class, String.format("V_%s_%s", major, minor));
        }
    }

    @Override
    public int compareWith(Version o) {
        int cmp = this.major() - o.major();
        if (cmp == 0) {
            cmp = this.minor() - o.minor();
        }
        return cmp;
    }

}