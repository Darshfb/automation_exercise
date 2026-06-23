package com.automationexercise.qe.ui.tests.negative;

import com.automationexercise.qe.core.base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import com.automationexercise.qe.core.config.EnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Feature("Security & Routing - Negative Scenarios")
public class SecurityRoutingTests extends BaseTest {

    @BeforeMethod
    public void setupTest() {
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
    }

    @Test(groups = {"regression", "negative", "security"})
    @Story("Unauthenticated Checkout Access")
    public void testUnauthenticatedCheckoutAccess() {
        // Attempt to access secure page directly
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl() + "/checkout");
        
        // Since user is not logged in and cart is empty, should redirect to login or show an error
        // Or if it allows access to the page, it should prompt to login/register
        String currentUrl = getDriver().getCurrentUrl();
        String pageSource = getDriver().getPageSource();
        
        boolean redirected = currentUrl.contains("/login");
        boolean accessDeniedOrEmpty = pageSource.contains("Register / Login account to proceed on checkout");
        
        Assert.assertTrue(redirected || accessDeniedOrEmpty, "Unauthenticated user should not be able to proceed with checkout");
    }

    @Test(groups = {"regression", "negative", "security"})
    @Story("Unauthenticated Payment Access")
    public void testUnauthenticatedPaymentAccess() {
        // Attempt to access payment page directly
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl() + "/payment");
        
        // Ensure no payment form is visible or user is redirected
        boolean hasPaymentForm = getDriver().getPageSource().contains("data-qa=\"pay-button\"");
        
        Assert.assertFalse(hasPaymentForm, "Unauthenticated user should not see the payment form");
    }
}
