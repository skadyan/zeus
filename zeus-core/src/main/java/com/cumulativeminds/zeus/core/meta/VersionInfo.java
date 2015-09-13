package com.cumulativeminds.zeus.core.meta;

import com.cumulativeminds.zeus.core.spi.Version;

public class VersionInfo {

    private Version since;
    private Version obsolete;

    public VersionInfo(Version since, Version obsolete) {
        this.since = since;
        this.obsolete = obsolete;
    }

    public Version getObsolete() {
        return obsolete;
    }

    public Version getSince() {
        return since;
    }

    @Override
    public String toString() {
        return String.format("[since=%s, obsolete=%s]", since, obsolete);
    }
}
