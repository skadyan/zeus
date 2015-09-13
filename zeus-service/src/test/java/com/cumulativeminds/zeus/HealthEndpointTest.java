package com.cumulativeminds.zeus;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;

public class HealthEndpointTest extends ServiceIntegrationTestCase {

    @Before
    public void setup() {
        RestAssured.port = localManagementPort;
        RestAssured.basePath = "/support";

    }

    @Test
    public void validateTheHealthStatus() throws Exception {
        given().auth().basic("user", "password1")
                .when().get("/health")
                .then()
                .statusCode(SC_OK);
    }

}
