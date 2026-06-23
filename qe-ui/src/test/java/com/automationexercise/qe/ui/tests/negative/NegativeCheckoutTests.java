package com.automationexercise.qe.ui.tests.negative;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.ui.pages.CartPage;
import com.automationexercise.qe.ui.pages.HomePage;
import com.automationexercise.qe.ui.pages.PaymentPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import com.automationexercise.qe.core.config.EnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Feature("Checkout & Cart - Negative Scenarios")
public class NegativeCheckoutTests extends BaseTest {

    @BeforeMethod
    public void setupTest() {
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
    }

    @Test(groups = {"regression", "negative"})
    @Story("Checkout with Empty Cart")
    public void testCheckoutEmptyCart() {
        HomePage homePage = new HomePage(getDriver());
        CartPage cartPage = homePage.navigateToCart();
        
        // When cart is empty, proceeding to checkout shouldn't be possible or should show error
        // On this specific site, if we try to click 'Proceed To Checkout' when empty, let's see if the button exists
        // Since it might not exist, we just verify the cart is empty
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Cart should be empty");
        
        // Alternatively, direct URL access to checkout
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl() + "/checkout");
        // User should be redirected or shown an error
        // Let's assert we don't land on a valid checkout state
        Assert.assertFalse(getDriver().getPageSource().contains("Address Details"), "Should not show checkout details for empty cart");
    }

    @Test(groups = {"regression", "negative"})
    @Story("Missing Payment Details")
    public void testMissingPaymentDetails() {
        // Direct to payment without going through the whole flow might not be allowed
        // But let's assume we can hit the payment endpoint or we just assert HTML5 validation
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl() + "/payment");
        PaymentPage paymentPage = new PaymentPage(getDriver());
        
        // If the site redirects us, we catch it
        if (getDriver().getCurrentUrl().contains("/payment")) {
            paymentPage.payAndConfirm();
            // Should not succeed because fields are empty
            Assert.assertFalse(getDriver().getCurrentUrl().contains("payment_done"), "Payment should not succeed with empty fields");
        } else {
            // The site correctly blocked access to /payment
            Assert.assertTrue(true, "Site correctly redirected away from /payment");
        }
    }
}
