package com.automationexercise.qe.ui.tests.exhaustive;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.ui.pages.HomePage;
import com.automationexercise.qe.ui.pages.ProductDetailsPage;
import com.automationexercise.qe.ui.pages.ProductsPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Feature("Exhaustive UI Testing - Cart & Products")
public class ExhaustiveCartTests extends BaseTest {

    @DataProvider(name = "maliciousQuantities")
    public Object[][] getMaliciousQuantities() {
        return new Object[][]{
            {"0"},
            {"-1"},
            {"-500"},
            {"999999999"}, // Huge boundary
            {"abc"}, // Non-numeric
            {"!@#"} // Special chars
        };
    }

    @Test(dataProvider = "maliciousQuantities", groups = {"exhaustive"})
    @Story("Exhaustive Cart Quantity Boundary & Type Testing")
    public void testExhaustiveCartQuantities(String quantity) {
        HomePage homePage = new HomePage(getDriver());
        ProductsPage productsPage = homePage.navigateToProducts();
        ProductDetailsPage detailsPage = productsPage.viewFirstProductDetails();
        
        detailsPage.setQuantity(quantity);
        
        try {
            detailsPage.addToCart(); // This also clicks continue shopping
        } catch (Exception e) {
            // Modal might not appear if input is completely invalid, or addToCart throws error
        }
        
        // Ensure no server crash
        boolean isSafe = !getDriver().getPageSource().contains("Internal Server Error") && 
                         !getDriver().getPageSource().contains("Exception");
        
        Assert.assertTrue(isSafe, "Application crashed handling malicious quantity: " + quantity);
    }
}
