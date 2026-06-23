package com.automationexercise.qe.api.client;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class AuthApiClient extends ApiCoreClient {

    private static final String VERIFY_LOGIN_ENDPOINT = "/verifyLogin";

    public Response verifyLogin(String email, String password) {
        return given()
                .spec(requestSpec)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post(VERIFY_LOGIN_ENDPOINT);
    }

    public Response deleteVerifyLogin() {
        return given()
                .spec(requestSpec)
                .when()
                .delete(VERIFY_LOGIN_ENDPOINT);
    }
}
