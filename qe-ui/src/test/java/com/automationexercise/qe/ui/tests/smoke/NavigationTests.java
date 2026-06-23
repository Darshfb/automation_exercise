package com.automationexercise.qe.ui.tests.smoke;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.core.config.EnvironmentConfig;
import com.automationexercise.qe.ui.pages.HomePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Forms & Support")
@Feature("Navigation")
public class NavigationTests extends BaseTest {

    @BeforeMethod
    public void setupTest() {
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
    }

    @Test(groups = "regression")
    @Story("Navigate to Test Cases Page")
    public void testNavigateToTestCases() {
        HomePage homePage = new HomePage(getDriver());
        // Since we don't have a specific TestCasesPage object, we can just click and verify URL
        homePage.navigateToTestCases();
        Assert.assertTrue(getDriver().getCurrentUrl().contains("test_cases"), "Did not navigate to Test Cases page");
    }

    @Test(groups = "regression")
    @Story("Navigate to API Testing Page")
    public void testNavigateToApiTesting() {
        HomePage homePage = new HomePage(getDriver());
        homePage.navigateToApiTesting();
        Assert.assertTrue(getDriver().getCurrentUrl().contains("api_list"), "Did not navigate to API List page");
    }
}
