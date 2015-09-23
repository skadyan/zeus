package com.cumulativeminds.zeus;

import org.junit.Test;

public class HealthEndpointTest extends ServiceIntegrationTestCase {

    @Test
    public void validateTheHealthStatus() throws Exception {
        givenSupportUserWithGoodCredentials()
                .expect()
                .statusCode(SC_OK)
                .when().get("/health");
    }

}
