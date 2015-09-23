package com.cumulativeminds.zeus.bootstrap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulativeminds.zeus.ServiceTestCase;

public class LoggingVerificationTest extends ServiceTestCase {
    private static Logger log = LoggerFactory.getLogger(LoggingVerificationTest.class);

    @Test
    public void infoMessageAreGettingGenerated() throws Exception {
        log.info("SomeRandomString");
    }
}
