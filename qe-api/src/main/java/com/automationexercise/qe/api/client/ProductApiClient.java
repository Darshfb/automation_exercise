package com.automationexercise.qe.api.client;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class ProductApiClient extends ApiCoreClient {

    private static final String PRODUCTS_LIST_ENDPOINT = "/productsList";
    private static final String BRANDS_LIST_ENDPOINT = "/brandsList";
    private static final String SEARCH_PRODUCT_ENDPOINT = "/searchProduct";

    public Response getAllProducts() {
        return given()
                .spec(requestSpec)
                .when()
                .get(PRODUCTS_LIST_ENDPOINT);
    }

    public Response getAllBrands() {
        return given()
                .spec(requestSpec)
                .when()
                .get(BRANDS_LIST_ENDPOINT);
    }

    public Response searchProduct(String searchProduct) {
        return given()
                .spec(requestSpec)
                .formParam("search_product", searchProduct)
                .when()
                .post(SEARCH_PRODUCT_ENDPOINT);
    }

    public Response postAllProducts() {
        return given()
                .spec(requestSpec)
                .when()
                .post(PRODUCTS_LIST_ENDPOINT);
    }

    public Response putAllBrands() {
        return given()
                .spec(requestSpec)
                .when()
                .put(BRANDS_LIST_ENDPOINT);
    }

    public Response searchProductWithoutParam() {
        return given()
                .spec(requestSpec)
                .when()
                .post(SEARCH_PRODUCT_ENDPOINT);
    }
}
