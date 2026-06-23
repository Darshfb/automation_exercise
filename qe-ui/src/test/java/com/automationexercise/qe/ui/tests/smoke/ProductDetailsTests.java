package com.automationexercise.qe.ui.tests.smoke;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.core.config.EnvironmentConfig;
import com.automationexercise.qe.ui.pages.HomePage;
import com.automationexercise.qe.ui.pages.ProductDetailsPage;
import com.automationexercise.qe.ui.pages.ProductsPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Product Catalog")
@Feature("Product Details")
public class ProductDetailsTests extends BaseTest {

    @BeforeMethod
    public void setupTest() {
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
    }

    @Test(groups = {"regression", "smoke"})
    @Story("Verify Product Details Elements")
    public void testProductDetailsInformation() {
        HomePage homePage = new HomePage(getDriver());
        ProductsPage productsPage = homePage.navigateToProducts();
        
        // Ensure there are products before trying to click
        Assert.assertTrue(productsPage.getVisibleProductCount() > 0, "No products found on Products Page");
        
        ProductDetailsPage detailsPage = productsPage.viewFirstProductDetails();
        
        Assert.assertFalse(detailsPage.getProductName().isEmpty(), "Product Name is empty!");
        Assert.assertFalse(detailsPage.getProductCategory().isEmpty(), "Product Category is empty!");
        Assert.assertFalse(detailsPage.getProductPrice().isEmpty(), "Product Price is empty!");
        Assert.assertFalse(detailsPage.getProductAvailability().isEmpty(), "Product Availability is empty!");
        Assert.assertFalse(detailsPage.getProductCondition().isEmpty(), "Product Condition is empty!");
        Assert.assertFalse(detailsPage.getProductBrand().isEmpty(), "Product Brand is empty!");
    }
}
