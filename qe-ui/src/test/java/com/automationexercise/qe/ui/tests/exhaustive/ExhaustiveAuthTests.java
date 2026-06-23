package com.automationexercise.qe.ui.tests.exhaustive;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.ui.pages.HomePage;
import com.automationexercise.qe.ui.pages.LoginPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Feature("Exhaustive UI Testing - Authentication")
public class ExhaustiveAuthTests extends BaseTest {

    @DataProvider(name = "maliciousEmails")
    public Object[][] getMaliciousEmails() {
        return new Object[][]{
            // Boundary Analysis
            {"a".repeat(300) + "@test.com"}, // 300+ char email
            
            // Format Validation
            {" test@test.com"}, // leading space
            {"test@test.com "}, // trailing space
            {"test.com"}, // missing @
            {"test@"}, // missing domain
            
            // Special Characters
            {"أحمد@test.com"}, // Arabic
            {"テスト@test.com"}, // Japanese
            {"😎@test.com"}, // Emoji
            {"\"@\"@test.com"}, // Quotes
            
            // SQL Injection
            {"admin' --"},
            {"' OR 1=1;--"}
        };
    }

    @Test(dataProvider = "maliciousEmails", groups = {"exhaustive"})
    @Story("Exhaustive Signup Format & Injection Testing")
    public void testExhaustiveSignupFormats(String badEmail) {
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();
        
        loginPage.signup("Test User", badEmail);
        
        // Assert we do not progress to the "Account Information" page successfully or crash
        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertFalse(currentUrl.contains("signup") && !getDriver().getPageSource().contains("New User Signup!"), 
            "Should not proceed to detailed signup page with bad email: " + badEmail);
    }
    
    @DataProvider(name = "maliciousNames")
    public Object[][] getMaliciousNames() {
        return new Object[][]{
            // Boundary Analysis
            {"A".repeat(500)}, // 500+ char name
            
            // Special Characters
            {"!@#$%^&*()_+-=[]{}|;':,./<>?"}, 
            {"<script>alert(1)</script>"}, // XSS
            {"SELECT * FROM users"} // SQLi
        };
    }

    @Test(dataProvider = "maliciousNames", groups = {"exhaustive"})
    @Story("Exhaustive Signup Name Boundaries & XSS")
    public void testExhaustiveSignupNames(String badName) {
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();
        
        String validEmail = "temp_" + System.currentTimeMillis() + "@test.com";
        loginPage.signup(badName, validEmail);
        
        // It might succeed, or it might fail, but it must NOT crash (HTTP 500)
        boolean isSafe = !getDriver().getPageSource().contains("Internal Server Error") && 
                         !getDriver().getPageSource().contains("Exception");
        
        Assert.assertTrue(isSafe, "Application crashed handling malicious name: " + badName);
    }
}
