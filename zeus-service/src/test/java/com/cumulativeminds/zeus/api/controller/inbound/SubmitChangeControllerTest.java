package com.cumulativeminds.zeus.api.controller.inbound;

import org.junit.Test;

import com.cumulativeminds.zeus.ServiceIntegrationTestCase;
import com.jayway.restassured.http.ContentType;

public class SubmitChangeControllerTest extends ServiceIntegrationTestCase {

    @Test
    public void submitRawChangeRequest() throws Exception {
        givenNormalUserWithGoodCredentials()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{ }")
                .expect()
                .statusCode(SC_OK)
                .when().post("/airport/change");
    }

    @Test
    public void submitUrlencodedChangeRequest() throws Exception {
        givenNormalUserWithGoodCredentials()
                .accept(ContentType.JSON)
                .contentType(ContentType.URLENC)
                .param("runId", 1002)
                .expect()
                .statusCode(SC_OK)
                .when().post("/airport/change");
    }

    @Test
    public void submitFormDataChangeRequest() throws Exception {
        givenNormalUserWithGoodCredentials()
                .accept(ContentType.JSON)
                .contentType("multipart/form-data")
                .multiPart("file", "test-file.json", "{ }".getBytes(), "application/json")
                .multiPart("runId", 1001)

        .expect()
                .statusCode(SC_OK)

        .when()
                .post("/airport/change");
    }
}
