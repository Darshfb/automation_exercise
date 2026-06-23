package com.automationexercise.qe.ui.tests.misc;

import com.automationexercise.qe.ui.pages.HomePage;
import com.automationexercise.qe.ui.pages.LoginPage;
import com.automationexercise.qe.core.base.BaseTest;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

public class AccountUpdateTests extends BaseTest {

    @Test(groups = {"misc"})
    @Story("TC_087-089: Update Account Information")
    public void testUpdateAccountInformation() {
        String testEmail = "update_test_" + UUID.randomUUID() + "@example.com";
        
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();
        
        loginPage.signup("Update User", testEmail);
        
        // At this point we expect to land on an Account Update or Edit Profile page.
        // The Automation Exercise app does not possess this functionality. 
        // We will attempt to find a link or button that says "Edit Profile", "Update Account", or "My Account".
        
        boolean isEditProfileAvailable = !getDriver().findElements(By.partialLinkText("Edit Profile")).isEmpty() ||
                                       !getDriver().findElements(By.partialLinkText("Update Account")).isEmpty();
                                       
        // Assert that the site supports account updates, which will intentionally FAIL, properly indicating 
        // that the application is missing the feature outlined in the testcase.md checklist.
        Assert.assertTrue(isEditProfileAvailable, "The application does not have an 'Edit Profile' or 'Update Account' feature to satisfy TC_087-089.");
    }
}
