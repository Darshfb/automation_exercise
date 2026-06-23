package com.automationexercise.qe.api.client;

import io.restassured.response.Response;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class AccountApiClient extends ApiCoreClient {

    private static final String CREATE_ACCOUNT_ENDPOINT = "/createAccount";
    private static final String DELETE_ACCOUNT_ENDPOINT = "/deleteAccount";
    private static final String UPDATE_ACCOUNT_ENDPOINT = "/updateAccount";
    private static final String GET_USER_DETAIL_ENDPOINT = "/getUserDetailByEmail";

    public Response createAccount(Map<String, String> userDetails) {
        return given()
                .spec(requestSpec)
                .formParams(userDetails)
                .when()
                .post(CREATE_ACCOUNT_ENDPOINT);
    }

    public Response deleteAccount(String email, String password) {
        return given()
                .spec(requestSpec)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .delete(DELETE_ACCOUNT_ENDPOINT);
    }

    public Response updateAccount(Map<String, String> userDetails) {
        return given()
                .spec(requestSpec)
                .formParams(userDetails)
                .when()
                .put(UPDATE_ACCOUNT_ENDPOINT);
    }

    public Response getUserDetailByEmail(String email) {
        return given()
                .spec(requestSpec)
                .queryParam("email", email)
                .when()
                .get(GET_USER_DETAIL_ENDPOINT);
    }
}
