package com.cumulativeminds.zeus;

import static com.jayway.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;

@WebAppConfiguration
@IntegrationTest({ "server.port=0", "management.port=0" })
public abstract class ServiceIntegrationTestCase extends ServiceTestCase implements HttpStatus {
    @Value("${local.server.port}")
    protected int localServerPort;
    @Value("${local.management.port}")
    protected int localManagementPort;

    protected String basePath;
    protected String endpoint;

    protected RequestSpecification givenSupportUserWithGoodCredentials() {
        return given().spec(supportApiSpec()).auth().preemptive().basic("user", "password1");
    }

    protected RequestSpecification givenNormalUserWithGoodCredentials() {
        return given().spec(serviceApiSpec()).auth().preemptive().basic("user", "password1");
    }

    private RequestSpecification serviceApiSpec() {
        return new RequestSpecBuilder()
                .setPort(localServerPort)
                .setBasePath("/api")
                .build();
    }

    private RequestSpecification supportApiSpec() {
        return new RequestSpecBuilder()
                .setPort(localManagementPort)
                .setBasePath("/support")
                .build();
    }

}
