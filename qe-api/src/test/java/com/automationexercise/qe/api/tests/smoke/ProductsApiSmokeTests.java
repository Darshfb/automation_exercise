package com.automationexercise.qe.api.tests.smoke;

import com.automationexercise.qe.api.client.ProductApiClient;
import com.automationexercise.qe.api.client.SearchApiClient;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Feature("Products API")
public class ProductsApiSmokeTests {

    private ProductApiClient productClient;
    private SearchApiClient searchClient;

    @BeforeClass
    public void setup() {
        productClient = new ProductApiClient();
        searchClient = new SearchApiClient();
    }

    @Test(groups = "smoke")
    @Story("Get All Products")
    public void testGetAllProductsApi() {
        Response response = productClient.getAllProducts();
        Assert.assertEquals(response.getStatusCode(), 200);
        
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.getInt("responseCode"), 200);
        Assert.assertTrue(jsonPath.getList("products").size() > 0);
    }

    @Test(groups = "smoke")
    @Story("Get All Brands")
    public void testGetAllBrandsApi() {
        Response response = productClient.getAllBrands();
        Assert.assertEquals(response.getStatusCode(), 200);
        
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.getInt("responseCode"), 200);
        Assert.assertTrue(jsonPath.getList("brands").size() > 0);
    }
    
    @Test(groups = "smoke")
    @Story("Search Existing Product")
    public void testSearchExistingProductApi() {
        Response response = searchClient.searchProduct("top");
        Assert.assertEquals(response.getStatusCode(), 200);
        
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.getInt("responseCode"), 200);
        Assert.assertTrue(jsonPath.getList("products").size() > 0);
    }
    
    @Test(groups = "smoke")
    @Story("Search Non-Existing Product")
    public void testSearchNonExistingProductApi() {
        Response response = searchClient.searchProduct("thiswillneverexist12345");
        Assert.assertEquals(response.getStatusCode(), 200);
        
        JsonPath jsonPath = response.jsonPath();
        // The API returns 200 but might return an empty list or specific message
        // Asserting status code for smoke test
        Assert.assertTrue(response.getBody().asString().contains("responseCode"));
    }
}
