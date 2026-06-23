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
@Feature("Registration")
public class SignupSmokeTests extends BaseTest {

    private final Faker faker = new Faker();

    @Test(groups = {"smoke", "regression"})
    @Story("User can register a new account successfully")
    public void testValidRegistration() {
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();
        
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();
        
        SignupPage signupPage = loginPage.signup(name, email);
        signupPage.fillAccountInformation(
                "Pass123!", 
                name, 
                faker.name().lastName(), 
                faker.address().streetAddress(), 
                faker.address().state(), 
                faker.address().city(), 
                faker.address().zipCode(), 
                faker.phoneNumber().cellPhone()
        );
        signupPage.submitAccount();
        
        Assert.assertTrue(signupPage.isAccountCreated(), "Account created success message not displayed!");
        
        HomePage loggedInHome = signupPage.continueToHome();
        Assert.assertEquals(loggedInHome.getLoggedUsername(), name, "Logged in username does not match!");
    }

    @Test(groups = {"regression"})
    @Story("User cannot register with an already existing email")
    public void testExistingEmailRegistration() {
        String name = faker.name().firstName();
        String email = "existing_" + System.currentTimeMillis() + "@testmail.com";
        
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();
        
        // 1. Register the email first
        SignupPage signupPage = loginPage.signup(name, email);
        signupPage.fillAccountInformation(
                "Pass123!", 
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
        
        // 2. Try to register with the same email again
        loginPage = homePage.navigateToLogin();
        loginPage.signup(name, email);
        
        String errorMsg = loginPage.getSignupError();
        Assert.assertEquals(errorMsg, "Email Address already exist!", "Existing email error message is incorrect or missing!");
    }
}
