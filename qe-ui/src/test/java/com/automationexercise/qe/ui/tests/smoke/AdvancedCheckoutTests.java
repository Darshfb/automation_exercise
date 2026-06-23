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

import java.io.File;

@Epic("Cart & Checkout")
@Feature("Advanced Checkout Flows")
public class AdvancedCheckoutTests extends BaseTest {

    private final Faker faker = new Faker();



    @Test(groups = "regression")
    @Story("Place Order: Login before Checkout")
    public void testLoginBeforeCheckout() {
        String name = faker.name().firstName();
        String email = "checkout_user_" + System.currentTimeMillis() + "@testmail.com";
        String password = "Pass123!";
        
        HomePage homePage = new HomePage(getDriver());
        
        // 1. Register a user dynamically first
        LoginPage loginPage = homePage.navigateToLogin();
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
        
        // 2. Perform the test flow by logging in
        loginPage = homePage.navigateToLogin();
        homePage = loginPage.login(email, password);
        
        // 3. Add product
        ProductsPage productsPage = homePage.navigateToProducts();
        productsPage.addFirstProductToCart();
        
        // 4. Go to Cart & Checkout
        CartPage cartPage = homePage.navigateToCart();
        cartPage.proceedToCheckout();
        
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        Assert.assertTrue(checkoutPage.isDeliveryAddressDisplayed(), "Delivery address missing");
        
        // 5. Payment
        PaymentPage paymentPage = checkoutPage.placeOrder();
        paymentPage.fillPaymentDetails(
                faker.name().firstName(), 
                faker.finance().creditCard(), 
                "123", "12", "2030"
        );
        paymentPage.payAndConfirm();
        Assert.assertTrue(paymentPage.isPaymentSuccessful(), "Order failed");
    }

    @Test(groups = "regression")
    @Story("Download Invoice after purchase order")
    public void testDownloadInvoice() {
        String name = faker.name().firstName();
        String email = "invoice_user_" + System.currentTimeMillis() + "@testmail.com";
        String password = "Pass123!";
        
        HomePage homePage = new HomePage(getDriver());
        
        // 1. Register a user dynamically first
        LoginPage loginPage = homePage.navigateToLogin();
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
        
        // 2. Log in with the registered user
        loginPage = homePage.navigateToLogin();
        homePage = loginPage.login(email, password);
        
        // 3. Add product
        ProductsPage productsPage = homePage.navigateToProducts();
        productsPage.addFirstProductToCart();
        
        // 4. Go to Cart & Checkout
        CartPage cartPage = homePage.navigateToCart();
        cartPage.proceedToCheckout();
        
        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        PaymentPage paymentPage = checkoutPage.placeOrder();
        paymentPage.fillPaymentDetails(
                "Test User", "1111222233334444", "123", "12", "2030"
        );
        paymentPage.payAndConfirm();
        
        // 5. Download Invoice
        paymentPage.downloadInvoice();
        
        // 6. Verify File Downloaded
        File downloadDir = new File("target/downloads");
        boolean isInvoiceDownloaded = false;
        
        for (int i = 0; i < 20; i++) {
            File[] files = downloadDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().contains("invoice") && !file.getName().endsWith(".crdownload")) {
                        isInvoiceDownloaded = true;
                        file.delete(); // cleanup
                        break;
                    }
                }
            }
            if (isInvoiceDownloaded) break;
            try { Thread.sleep(500); } catch (InterruptedException e) { }
        }
        
        Assert.assertTrue(isInvoiceDownloaded, "Invoice file was not downloaded to target/downloads directory");
    }
}
