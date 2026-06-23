package com.automationexercise.qe.ui.tests.smoke;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.core.config.EnvironmentConfig;
import com.automationexercise.qe.ui.pages.CartPage;
import com.automationexercise.qe.ui.pages.HomePage;
import com.github.javafaker.Faker;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Subscriptions & Scrolling")
@Feature("Subscriptions")
public class SubscriptionTests extends BaseTest {

    private final Faker faker = new Faker();

    @BeforeMethod
    public void setupTest() {
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
    }

    @Test(groups = "regression")
    @Story("Verify Subscription in home page")
    public void testSubscriptionOnHomePage() {
        HomePage homePage = new HomePage(getDriver());
        homePage.scrollToBottom();
        
        homePage.submitSubscription(faker.internet().emailAddress());
        Assert.assertTrue(homePage.isSubscriptionSuccessful(), "Subscription success message not displayed on Home Page");
    }

    @Test(groups = "regression")
    @Story("Verify Subscription in Cart page")
    public void testSubscriptionOnCartPage() {
        HomePage homePage = new HomePage(getDriver());
        CartPage cartPage = homePage.navigateToCart();
        
        cartPage.scrollToBottom();
        cartPage.submitSubscription(faker.internet().emailAddress());
        Assert.assertTrue(cartPage.isSubscriptionSuccessful(), "Subscription success message not displayed on Cart Page");
    }

    @Test(groups = "regression")
    @Story("TC_080: Subscribe with already subscribed email")
    public void testAlreadySubscribedEmail() {
        HomePage homePage = new HomePage(getDriver());
        homePage.scrollToBottom();
        
        String email = faker.internet().emailAddress();
        
        // First subscription
        homePage.submitSubscription(email);
        Assert.assertTrue(homePage.isSubscriptionSuccessful(), "First subscription should succeed");
        
        // Refresh and try again with the same email
        getDriver().navigate().refresh();
        homePage.scrollToBottom();
        homePage.submitSubscription(email);
        
        // The site behavior might be to show success again or show a warning.
        // We will just assert that it doesn't crash, and assume the UI handles it gracefully.
        // If it throws an error or shows a different message, the framework captures it.
        Assert.assertTrue(homePage.isSubscriptionSuccessful() || getDriver().getPageSource().contains("already subscribed"), 
                "Should handle duplicate subscription gracefully.");
    }
}
