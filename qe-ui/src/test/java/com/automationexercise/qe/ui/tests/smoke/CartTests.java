package com.automationexercise.qe.ui.tests.smoke;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.core.config.EnvironmentConfig;
import com.automationexercise.qe.ui.pages.CartPage;
import com.automationexercise.qe.ui.pages.HomePage;
import com.automationexercise.qe.ui.pages.ProductsPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Cart & Checkout")
@Feature("Cart Operations")
public class CartTests extends BaseTest {

    @BeforeMethod
    public void setupTest() {
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
    }

    @Test(groups = "regression")
    @Story("Add Products to Cart")
    public void testAddProductToCart() {
        HomePage homePage = new HomePage(getDriver());
        ProductsPage productsPage = homePage.navigateToProducts();
        
        productsPage.addFirstProductToCart();
        
        CartPage cartPage = homePage.navigateToCart();
        Assert.assertTrue(cartPage.getCartItemCount() >= 1, "Product was not added to cart!");
    }
}
