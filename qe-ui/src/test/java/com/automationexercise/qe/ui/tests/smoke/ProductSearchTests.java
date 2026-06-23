package com.automationexercise.qe.ui.tests.smoke;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.core.config.EnvironmentConfig;
import com.automationexercise.qe.ui.pages.HomePage;
import com.automationexercise.qe.ui.pages.ProductsPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Product Catalog")
@Feature("Product Search")
public class ProductSearchTests extends BaseTest {



    @Test(groups = "smoke")
    @Story("View All Products")
    public void testViewAllProducts() {
        HomePage homePage = new HomePage(getDriver());
        ProductsPage productsPage = homePage.navigateToProducts();
        
        Assert.assertTrue(productsPage.getVisibleProductCount() > 0, "Products should be visible");
    }

    @Test(groups = "regression")
    @Story("Search Product - Valid Keyword")
    public void testSearchProductValid() {
        HomePage homePage = new HomePage(getDriver());
        ProductsPage productsPage = homePage.navigateToProducts();
        
        productsPage.searchProduct("Shirt");
        Assert.assertTrue(productsPage.getVisibleProductCount() > 0, "Searched products should be visible");
    }

    @Test(groups = "regression")
    @Story("Search Product - Invalid Keyword")
    public void testSearchProductInvalid() {
        HomePage homePage = new HomePage(getDriver());
        ProductsPage productsPage = homePage.navigateToProducts();
        
        productsPage.searchProduct("InvalidProductThatDoesNotExist123");
        Assert.assertEquals(productsPage.getVisibleProductCount(), 0, "No products should be visible for invalid search");
    }
}
