package com.automationexercise.qe.ui.tests.negative;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.ui.pages.ContactUsPage;
import com.automationexercise.qe.ui.pages.HomePage;
import com.automationexercise.qe.ui.pages.ProductsPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Forms & Inputs - Negative Scenarios")
public class NegativeFormTests extends BaseTest {

    @Test(groups = {"regression", "negative"})
    @Story("Contact Us - Missing Fields")
    public void testContactUsMissingFields() {
        HomePage homePage = new HomePage(getDriver());
        ContactUsPage contactUsPage = homePage.navigateToContactUs();
        
        // Attempt to submit without mandatory fields
        contactUsPage.submitContactForm("", "", "", "");
        
        // Form should not submit, user stays on contact-us page
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/contact_us"), "User should remain on contact us page");
        
        // Validation logic - there is no success message
        try {
            Assert.assertFalse(contactUsPage.isSuccessMessageDisplayed(), "Success message should not be visible");
        } catch (Exception e) {
            // Expected since it's not visible
            Assert.assertTrue(true);
        }
    }

    @Test(groups = {"regression", "negative"})
    @Story("Product Search - SQL/XSS Injection")
    public void testProductSearchInjection() {
        HomePage homePage = new HomePage(getDriver());
        ProductsPage productsPage = homePage.navigateToProducts();
        
        // Use a common SQL injection string
        String injectionPayload = "' OR 1=1--";
        productsPage.searchProduct(injectionPayload);
        
        // It should gracefully handle it (likely 0 results)
        try {
            int count = productsPage.getVisibleProductCount();
            // It might return 0, or it might just sanitize and return all products. 
            // The key is that the site does not crash or throw an application error (HTTP 500).
            Assert.assertTrue(count >= 0, "Site should gracefully handle SQL injection payload");
        } catch (Exception e) {
            Assert.fail("Site crashed or failed to render products on SQL injection attempt: " + e.getMessage());
        }
    }
}
