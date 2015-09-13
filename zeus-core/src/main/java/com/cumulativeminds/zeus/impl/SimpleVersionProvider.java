package com.cumulativeminds.zeus.impl;

import static com.cumulativeminds.zeus.impl.VersionImpl.UNSPECIFIED;
import static com.cumulativeminds.zeus.impl.VersionImpl.parse;

import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.core.spi.Version;
import com.cumulativeminds.zeus.core.spi.VersionProvider;

@Component
public class SimpleVersionProvider implements VersionProvider {
    @Override
    public Version fromText(String version) {
        return parse(version);
    }

    @Override
    public boolean isUnspecified(Version version) {
        return UNSPECIFIED == version;
    }

}
