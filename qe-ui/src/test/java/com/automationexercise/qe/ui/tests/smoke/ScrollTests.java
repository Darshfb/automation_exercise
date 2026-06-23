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

@Epic("Subscriptions & Scrolling")
@Feature("Scrolling")
public class ScrollTests extends BaseTest {

    @BeforeMethod
    public void setupTest() {
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
    }

    @Test(groups = "regression")
    @Story("Verify Scroll Up using 'Arrow' button")
    public void testScrollUpWithArrow() {
        HomePage homePage = new HomePage(getDriver());
        
        homePage.scrollToBottom();
        // Assume footer is visible at bottom
        
        homePage.clickScrollUp();
        
        Assert.assertTrue(homePage.isTopHeaderVisible(), "Top header is not visible after scrolling up with arrow");
    }

    @Test(groups = "regression")
    @Story("Verify Scroll Up without 'Arrow' button")
    public void testScrollUpWithoutArrow() {
        HomePage homePage = new HomePage(getDriver());
        
        homePage.scrollToBottom();
        // Wait a moment before scrolling back up
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        homePage.scrollToTop();
        
        Assert.assertTrue(homePage.isTopHeaderVisible(), "Top header is not visible after scrolling up via JS");
    }
}
