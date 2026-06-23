package com.automationexercise.qe.api.tests.smoke;

import com.automationexercise.qe.api.client.ProductApiClient;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Feature("Product Catalog API")
public class ProductApiTests {

    private ProductApiClient productClient;

    @BeforeClass
    public void setup() {
        productClient = new ProductApiClient();
    }

    @Test(groups = "smoke")
    @Story("Get All Products List")
    public void testGetAllProductsApi() {
        Response response = productClient.getAllProducts();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("products"));
    }

    @Test(groups = "regression")
    @Story("Search Product API")
    public void testSearchProductApi() {
        Response response = productClient.searchProduct("tshirt");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("products"));
    }

    @Test(groups = "regression")
    @Story("Get All Brands List")
    public void testGetAllBrandsApi() {
        Response response = productClient.getAllBrands();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("brands"));
    }

    @Test(groups = "regression")
    @Story("API 2: POST To All Products List")
    public void testPostToAllProductsList() {
        Response response = productClient.postAllProducts();
        Assert.assertEquals(response.getStatusCode(), 200); // The API actually returns 200 with responseCode 405 inside JSON body
        String body = response.getBody().asString();
        Assert.assertTrue(body.contains("405") || body.contains("This request method is not supported."), "Expected 405 or method not supported error, got: " + body);
    }

    @Test(groups = "regression")
    @Story("API 4: PUT To All Brands List")
    public void testPutToAllBrandsList() {
        Response response = productClient.putAllBrands();
        Assert.assertEquals(response.getStatusCode(), 200);
        String body = response.getBody().asString();
        Assert.assertTrue(body.contains("405") || body.contains("This request method is not supported."), "Expected 405 or method not supported error, got: " + body);
    }

    @Test(groups = "regression")
    @Story("API 6: POST To Search Product without search_product parameter")
    public void testSearchProductWithoutParam() {
        Response response = productClient.searchProductWithoutParam();
        Assert.assertEquals(response.getStatusCode(), 200);
        String body = response.getBody().asString();
        Assert.assertTrue(body.contains("400") || body.contains("Bad request, search_product parameter is missing in POST request."), "Expected 400 error, got: " + body);
    }
}
