package com.automationexercise.qe.ui.tests.smoke;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.core.config.EnvironmentConfig;
import com.automationexercise.qe.ui.pages.CartPage;
import com.automationexercise.qe.ui.pages.HomePage;
import com.automationexercise.qe.ui.pages.ProductDetailsPage;
import com.automationexercise.qe.ui.pages.ProductsPage;
import com.automationexercise.qe.ui.pages.LoginPage;
import com.automationexercise.qe.ui.pages.SignupPage;
import com.github.javafaker.Faker;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Cart Edge Cases")
@Feature("Cart Modifications")
public class CartEdgeCaseTests extends BaseTest {

    private final Faker faker = new Faker();

    @BeforeMethod
    public void setupTest() {
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
    }

    @Test(groups = "regression")
    @Story("Verify Product quantity in Cart")
    public void testProductQuantity() {
        HomePage homePage = new HomePage(getDriver());
        ProductsPage productsPage = homePage.navigateToProducts();
        
        ProductDetailsPage detailsPage = productsPage.viewFirstProductDetails();
        detailsPage.setQuantity("4");
        detailsPage.addToCart();
        
        CartPage cartPage = homePage.navigateToCart();
        Assert.assertEquals(cartPage.getFirstItemQuantity(), "4", "Quantity in cart does not match!");
    }

    @Test(groups = "regression")
    @Story("Remove Products From Cart")
    public void testRemoveProduct() {
        HomePage homePage = new HomePage(getDriver());
        ProductsPage productsPage = homePage.navigateToProducts();
        
        productsPage.addFirstProductToCart();
        CartPage cartPage = homePage.navigateToCart();
        
        Assert.assertTrue(cartPage.getCartItemCount() > 0, "Cart should have items initially");
        
        cartPage.removeFirstItem();
        // Assume wait for cart to be empty is handled or we just check count again after a short wait
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Cart should be empty after removal");
    }

    @Test(groups = "regression")
    @Story("Search Products and Verify Cart After Login")
    public void testSearchAndCartAfterLogin() {
        String name = faker.name().firstName();
        String email = "cart_user_" + System.currentTimeMillis() + "@testmail.com";
        String password = "Pass123!";
        
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();
        
        // 1. Register the user first
        SignupPage signupPage = loginPage.signup(name, email);
        signupPage.fillAccountInformation(
                password, 
                name, 
                faker.name().lastName(), 
                faker.address().streetAddress(), 
                faker.address().state(), 
                faker.address().city(), 
                faker.address().zipCode(), 
                faker.phoneNumber().cellPhone()
        );
        signupPage.submitAccount();
        homePage = signupPage.continueToHome();
        homePage.logout();
        
        // 2. Perform anonymous product search and add to cart
        ProductsPage productsPage = homePage.navigateToProducts();
        productsPage.searchProduct("Shirt");
        productsPage.addFirstProductToCart();
        
        CartPage cartPage = homePage.navigateToCart();
        Assert.assertTrue(cartPage.getCartItemCount() > 0, "Cart should have items");
        
        // 3. Login with the registered user
        loginPage = homePage.navigateToLogin();
        loginPage.login(email, password);
        
        // 4. Verify cart retains items after login
        CartPage cartPageAfterLogin = homePage.navigateToCart();
        Assert.assertTrue(cartPageAfterLogin.getCartItemCount() > 0, "Cart should retain items after login");
    }

    @Test(groups = "regression")
    @Story("Add to cart from Recommended items")
    public void testAddToCartFromRecommended() {
        HomePage homePage = new HomePage(getDriver());
        homePage.scrollToBottom();
        
        homePage.addRecommendedItemToCart();
        
        CartPage cartPage = homePage.navigateToCart();
        Assert.assertTrue(cartPage.getCartItemCount() > 0, "Cart should have items from recommended section");
    }
}
