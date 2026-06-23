package com.automationexercise.qe.ui.tests.smoke;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.ui.pages.HomePage;
import com.automationexercise.qe.ui.pages.LoginPage;
import com.automationexercise.qe.ui.pages.SignupPage;
import com.github.javafaker.Faker;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Authentication")
@Feature("Account Lifecycle")
public class AccountLifecycleTests extends BaseTest {

    private final Faker faker = new Faker();

    @Test(groups = {"regression"})
    @Story("User can delete their account successfully")
    public void testDeleteAccount() {
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();
        
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();
        
        SignupPage signupPage = loginPage.signup(name, email);
        signupPage.fillAccountInformation(
                "Pass123!", name, faker.name().lastName(), 
                faker.address().streetAddress(), faker.address().state(), 
                faker.address().city(), faker.address().zipCode(), 
                faker.phoneNumber().cellPhone()
        );
        signupPage.submitAccount();
        HomePage loggedInHome = signupPage.continueToHome();
        
        // Delete Account
        loggedInHome.deleteAccount();
        // Assume deleteAccount handles the confirmation screen and returns to home
        Assert.assertFalse(homePage.isUserLoggedIn(), "User should be logged out after deletion!");
    }

    @Test(groups = {"regression"})
    @Story("User can logout successfully")
    public void testLogout() {
        String name = faker.name().firstName();
        String email = "logout_user_" + System.currentTimeMillis() + "@testmail.com";
        String password = "Pass123!";
        
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();
        
        // 1. Register a user dynamically first
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
        
        Assert.assertTrue(homePage.isUserLoggedIn(), "User failed to login initially!");
        
        // 2. Perform logout
        LoginPage returnedLoginPage = homePage.logout();
        Assert.assertFalse(homePage.isUserLoggedIn(), "User should be logged out!");
    }
}
