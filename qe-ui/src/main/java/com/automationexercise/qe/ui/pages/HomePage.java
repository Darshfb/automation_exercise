package com.automationexercise.qe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final By signupLoginLink = By.xpath("//a[contains(@href, '/login')]");
    private final By productsLink = By.xpath("//a[contains(@href, '/products')]");
    private final By cartLink = By.xpath("//a[contains(@href, '/view_cart')]");
    private final By contactUsLink = By.xpath("//a[contains(@href, '/contact_us')]");
    private final By loggedInUser = By.xpath("//a[contains(text(), 'Logged in as')]");
    private final By deleteAccountLink = By.xpath("//a[contains(@href, '/delete_account')]");
    private final By logoutLink = By.xpath("//a[contains(@href, '/logout')]");
    private final By testCasesLink = By.xpath("//a[contains(text(), 'Test Cases')]");
    private final By apiTestingLink = By.xpath("//a[contains(text(), 'API Testing')]");
    private final By subscribeEmailInput = By.id("susbscribe_email");
    private final By subscribeButton = By.id("subscribe");
    private final By subscribeSuccessMsg = By.id("success-subscribe");
    private final By scrollUpButton = By.id("scrollUp");
    private final By topHeader = By.xpath("//h2[contains(text(), 'Full-Fledged practice website for Automation Engineers')]");
    private final By recommendedItemAddToCart = By.xpath("//div[@class='recommended_items']//a[contains(@class, 'add-to-cart')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public LoginPage navigateToLogin() {
        click(signupLoginLink);
        return new LoginPage(driver);
    }

    public ProductsPage navigateToProducts() {
        click(productsLink);
        return new ProductsPage(driver);
    }

    public CartPage navigateToCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    public ContactUsPage navigateToContactUs() {
        click(contactUsLink);
        return new ContactUsPage(driver);
    }

    public boolean isUserLoggedIn() {
        try {
            return waitForElementVisible(loggedInUser).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getLoggedUsername() {
        return waitForElementVisible(loggedInUser).findElement(By.xpath("./b")).getText();
    }
    
    public void deleteAccount() {
        click(deleteAccountLink);
    }
    
    public LoginPage logout() {
        click(logoutLink);
        return new LoginPage(driver);
    }
    
    public void navigateToTestCases() {
        click(testCasesLink);
    }
    
    public void navigateToApiTesting() {
        click(apiTestingLink);
    }
    
    public void submitSubscription(String email) {
        type(subscribeEmailInput, email);
        click(subscribeButton);
    }
    
    public boolean isSubscriptionSuccessful() {
        return waitForElementVisible(subscribeSuccessMsg).isDisplayed();
    }
    
    public void clickScrollUp() {
        click(scrollUpButton);
    }
    
    public boolean isTopHeaderVisible() {
        return waitForElementVisible(topHeader).isDisplayed();
    }
    
    public void addRecommendedItemToCart() {
        click(recommendedItemAddToCart);
        click(By.xpath("//button[text()='Continue Shopping']"));
    }
}
