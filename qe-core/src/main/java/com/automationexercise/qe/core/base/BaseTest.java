package com.automationexercise.qe.core.base;

import com.automationexercise.qe.core.driver.DriverFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for all UI tests.
 * Manages WebDriver lifecycle via TestNG annotations.
 */
@Slf4j
public class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        log.info("Setting up WebDriver for test thread: {}", Thread.currentThread().getId());
        WebDriver driver = DriverFactory.getDriver();
        String url = com.automationexercise.qe.core.config.EnvironmentConfig.getInstance().getBaseUrl();
        driver.get(url);
        
        // Workaround for Chrome 149 headless CDP mismatch where first navigation is silently ignored
        if (driver.getCurrentUrl().startsWith("data:")) {
            log.warn("First driver.get() failed silently (URL is still data:,). Retrying navigation to {}", url);
            driver.get(url);
        }
        
        driver.manage().deleteAllCookies();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("Tearing down WebDriver for test thread: {}", Thread.currentThread().getId());
        DriverFactory.quitDriver();
    }

    /**
     * Helper method to get the driver instance in test classes.
     * @return WebDriver instance for current thread
     */
    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
}
