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
@Feature("Filtering")
public class CatalogFilterTests extends BaseTest {

    @BeforeMethod
    public void setupTest() {
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
    }

    @Test(groups = "regression")
    @Story("View Category Products")
    public void testViewCategoryProducts() {
        HomePage homePage = new HomePage(getDriver());
        ProductsPage productsPage = homePage.navigateToProducts();
        
        productsPage.selectCategory("Women", "Dress");
        Assert.assertTrue(productsPage.getCategoryTitle().toUpperCase().contains("WOMEN - DRESS"), "Category title did not match");
        
        productsPage.selectCategory("Men", "Tshirts");
        Assert.assertTrue(productsPage.getCategoryTitle().toUpperCase().contains("MEN - TSHIRTS"), "Category title did not match");
    }

    @Test(groups = "regression")
    @Story("View & Cart Brand Products")
    public void testViewBrandProducts() {
        HomePage homePage = new HomePage(getDriver());
        ProductsPage productsPage = homePage.navigateToProducts();
        
        productsPage.selectBrand("Polo");
        Assert.assertTrue(productsPage.getCategoryTitle().toUpperCase().contains("POLO"), "Brand title did not match");
        
        productsPage.selectBrand("H&M");
        Assert.assertTrue(productsPage.getCategoryTitle().toUpperCase().contains("H&M"), "Brand title did not match");
    }
}
