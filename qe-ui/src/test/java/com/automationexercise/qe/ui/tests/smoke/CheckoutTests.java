package com.automationexercise.qe.ui.tests.smoke;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.core.config.EnvironmentConfig;
import com.automationexercise.qe.ui.pages.CartPage;
import com.automationexercise.qe.ui.pages.CheckoutPage;
import com.automationexercise.qe.ui.pages.HomePage;
import com.automationexercise.qe.ui.pages.LoginPage;
import com.automationexercise.qe.ui.pages.PaymentPage;
import com.automationexercise.qe.ui.pages.ProductsPage;
import com.automationexercise.qe.ui.pages.SignupPage;
import com.github.javafaker.Faker;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Cart & Checkout")
@Feature("Checkout Flow")
public class CheckoutTests extends BaseTest {

    private final Faker faker = new Faker();

    @BeforeMethod
    public void setupTest() {
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
    }

    @Test(groups = {"smoke", "regression"})
    @Story("Place Order: Register before Checkout")
    public void testPlaceOrderRegisterBeforeCheckout() {
        HomePage homePage = new HomePage(getDriver());
        
        // 1. Register
        LoginPage loginPage = homePage.navigateToLogin();
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();
        SignupPage signupPage = loginPage.signup(name, email);
        signupPage.fillAccountInformation(
                "Pass123!", name, faker.name().lastName(), 
                faker.address().streetAddress(), faker.address().state(), 
                faker.address().city(), faker.address().zipCode(), 
                faker.phoneNumber().cellPhone()
        );
        signupPage.submitAccount();
        homePage = signupPage.continueToHome();
        
        // 2. Add product to cart
        ProductsPage productsPage = homePage.navigateToProducts();
        productsPage.addFirstProductToCart();
        
        // 3. Go to Cart
        CartPage cartPage = homePage.navigateToCart();
        Assert.assertTrue(cartPage.getCartItemCount() > 0, "Cart should have items");
        
        // 4. Proceed to Checkout
        cartPage.proceedToCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        
        Assert.assertTrue(checkoutPage.isDeliveryAddressDisplayed(), "Delivery address missing");
        Assert.assertTrue(checkoutPage.isBillingAddressDisplayed(), "Billing address missing");
        checkoutPage.enterComment("Please deliver between 9 AM and 5 PM");
        
        // 5. Payment
        PaymentPage paymentPage = checkoutPage.placeOrder();
        paymentPage.fillPaymentDetails(
                name, 
                faker.finance().creditCard(), 
                "123", 
                "12", 
                "2030"
        );
        paymentPage.payAndConfirm();
        
        // 6. Verify success
        Assert.assertTrue(paymentPage.isPaymentSuccessful(), "Order confirmation message missing");
    }
}
