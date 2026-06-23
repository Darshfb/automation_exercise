package com.automationexercise.qe.api.client;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class SearchApiClient extends ApiCoreClient {

    private static final String SEARCH_PRODUCT_ENDPOINT = "/searchProduct";

    public Response searchProduct(String searchParam) {
        return given()
                .spec(requestSpec)
                .formParam("search_product", searchParam)
                .when()
                .post(SEARCH_PRODUCT_ENDPOINT);
    }
}
