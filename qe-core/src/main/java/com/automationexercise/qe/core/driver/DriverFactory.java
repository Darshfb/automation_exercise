package com.automationexercise.qe.core.driver;

import com.automationexercise.qe.core.config.EnvironmentConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

/**
 * Thread-safe WebDriver factory for parallel test execution.
 */
@Slf4j
public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {
        // Private constructor to hide implicit public one
    }

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            driverThreadLocal.set(createDriver());
        }
        return driverThreadLocal.get();
    }

    private static WebDriver createDriver() {
        String browser = EnvironmentConfig.getInstance().getBrowser();
        boolean headless = EnvironmentConfig.getInstance().isHeadless();
        
        WebDriver driver;
        log.info("Initializing {} driver. Headless: {}", browser, headless);

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("--headless");
                firefoxOptions.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.NORMAL);
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless");
                edgeOptions.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.NORMAL);
                driver = new EdgeDriver(edgeOptions);
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.NORMAL);
                chromeOptions.addArguments("--host-rules=MAP *doubleclick.net 127.0.0.1, MAP *googlesyndication.com 127.0.0.1, MAP *google-analytics.com 127.0.0.1, MAP *googletagservices.com 127.0.0.1, MAP *googletagmanager.com 127.0.0.1, MAP *adservice.google.com 127.0.0.1, MAP *analytics.google.com 127.0.0.1, MAP *googleadservices.com 127.0.0.1, MAP *facebook.net 127.0.0.1, MAP *facebook.com 127.0.0.1");
                
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("download.default_directory", new File("target/downloads").getAbsolutePath());
                prefs.put("download.prompt_for_download", false);
                prefs.put("download.directory_upgrade", true);
                prefs.put("safebrowsing.enabled", true);
                prefs.put("profile.managed_default_content_settings.images", 2);
                chromeOptions.setExperimentalOption("prefs", prefs);
                
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().window().maximize();
        
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            log.info("WebDriver quit successfully.");
        }
    }
}
