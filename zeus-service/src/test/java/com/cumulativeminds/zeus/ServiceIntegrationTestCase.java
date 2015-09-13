package com.cumulativeminds.zeus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@IntegrationTest({ "server.port=0", "management.port=0" })
public class ServiceIntegrationTestCase extends ServiceTestCase {
    @Value("${local.server.port}")
    protected int localServerPort;
    @Value("${local.management.port}")
    protected int localManagementPort;
}
