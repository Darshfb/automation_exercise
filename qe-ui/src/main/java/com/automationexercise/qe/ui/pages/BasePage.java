package com.automationexercise.qe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement waitForElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (org.openqa.selenium.TimeoutException e) {
            try {
                java.nio.file.Files.writeString(
                    java.nio.file.Paths.get("target", "timeout_source_visible_" + System.currentTimeMillis() + ".html"),
                    "URL: " + driver.getCurrentUrl() + "\nTITLE: " + driver.getTitle() + "\n\n" + driver.getPageSource()
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw e;
        }
    }

    protected WebElement waitForElementClickable(By locator) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (org.openqa.selenium.TimeoutException e) {
            try {
                java.nio.file.Files.writeString(
                    java.nio.file.Paths.get("target", "timeout_source_clickable_" + System.currentTimeMillis() + ".html"),
                    "URL: " + driver.getCurrentUrl() + "\nTITLE: " + driver.getTitle() + "\n\n" + driver.getPageSource()
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw e;
        }
    }

    protected void click(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, retrying with JavaScript executor.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected void click(By locator) {
        try {
            waitForElementClickable(locator).click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            System.out.println("Element click intercepted, retrying with JavaScript executor: " + locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(locator));
        }
    }

    protected void type(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForElementVisible(locator).getText();
    }
    
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }
    
    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
    }
}
