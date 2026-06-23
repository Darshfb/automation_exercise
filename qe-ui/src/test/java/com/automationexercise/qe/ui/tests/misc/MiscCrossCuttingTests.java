package com.automationexercise.qe.ui.tests.misc;

import com.automationexercise.qe.core.base.BaseTest;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.automationexercise.qe.core.config.EnvironmentConfig;
import org.testng.annotations.BeforeMethod;

public class MiscCrossCuttingTests extends BaseTest {

    @BeforeMethod
    public void setupTest() {
        // The URL is dynamically fetched from config (e.g., config-qa.properties)
        // instead of being hardcoded.
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
    }

    @Test(groups = {"misc"})
    @Story("TC_092: Verify all pages load without console errors")
    public void testNoConsoleErrorsOnLoad() {
        
        // Wait for page to fully load
        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        LogEntries logEntries = getDriver().manage().logs().get(LogType.BROWSER);
        int errorCount = 0;
        for (LogEntry entry : logEntries) {
            if (entry.getLevel().getName().equals("SEVERE")) {
                errorCount++;
                System.out.println("Console Error: " + entry.getMessage());
            }
        }
        
        // The site might have some actual console errors (e.g., failed ad scripts or 404 assets).
        // We will assert there are none to be strict about the TC.
        Assert.assertEquals(errorCount, 0, "There should be 0 SEVERE console errors on the homepage.");
    }

    @Test(groups = {"misc"})
    @Story("TC_094: Verify website is accessible on different screen sizes (responsive)")
    public void testResponsiveMobileViewport() {
        // Resize to a standard mobile viewport (iPhone 12/13/14: 390x844)
        getDriver().manage().window().setSize(new Dimension(390, 844));
        // We navigate again here after resizing to ensure the layout recalculates properly
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
        
        // The hamburger menu button should become visible on small screens
        WebElement mobileMenuToggle = getDriver().findElement(By.cssSelector("button.navbar-toggle"));
        Assert.assertTrue(mobileMenuToggle.isDisplayed(), "Mobile menu toggle should be visible on 390x844 viewport");
        
        // Return window to max size
        getDriver().manage().window().maximize();
    }

    @Test(groups = {"misc"})
    @Story("TC_095: Verify broken links do not exist on home page")
    public void testNoBrokenLinksOnHomePage() {
        List<WebElement> links = getDriver().findElements(By.tagName("a"));
        
        int brokenLinks = 0;
        int checkedLinks = 0;
        
        for (WebElement link : links) {
            String url = link.getAttribute("href");
            if (url == null || url.isEmpty() || url.startsWith("javascript")) {
                continue;
            }
            
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("HEAD");
                connection.setConnectTimeout(3000);
                connection.connect();
                
                int responseCode = connection.getResponseCode();
                if (responseCode >= 400) {
                    System.out.println("Broken Link found: " + url + " - Response Code: " + responseCode);
                    brokenLinks++;
                }
                checkedLinks++;
            } catch (Exception e) {
                System.out.println("Failed to connect to: " + url);
                brokenLinks++;
            }
        }
        
        Assert.assertTrue(checkedLinks > 0, "Should have found and checked links on the homepage");
        Assert.assertEquals(brokenLinks, 0, "There should be 0 broken links on the homepage.");
    }
}
