package com.automationexercise.qe.ui.tests.negative;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.ui.pages.HomePage;
import com.automationexercise.qe.ui.pages.LoginPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Authentication UI - Negative Scenarios")
public class NegativeAuthTests extends BaseTest {

    @Test(groups = {"smoke", "regression", "negative"})
    @Story("Test Case 3: Login User with incorrect email and password")
    public void testLoginIncorrectUser() {
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();
        
        loginPage.attemptLogin("invalid_fake_" + System.currentTimeMillis() + "@testmail.com", "wrongpassword");
        
        String errorMsg = loginPage.getLoginError();
        Assert.assertEquals(errorMsg, "Your email or password is incorrect!", "Incorrect login error message mismatch");
    }

    @Test(groups = {"regression", "negative"})
    @Story("Login with empty email and password")
    public void testLoginEmptyFields() {
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();
        
        loginPage.attemptLogin("", "");
        
        // Since HTML5 'required' attribute might block submission, we just verify we are still on the login page
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/login"), "User should remain on login page");
    }

    @Test(groups = {"regression", "negative"})
    @Story("Signup with invalid email format")
    public void testSignupInvalidEmail() {
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();
        
        loginPage.signup("Test User", "invalidemail.com");
        
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/login"), "User should not be redirected on invalid email format");
    }
}
