package com.automationexercise.qe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By loginEmailInput = By.cssSelector("[data-qa='login-email']");
    private final By loginPasswordInput = By.cssSelector("[data-qa='login-password']");
    private final By loginButton = By.cssSelector("[data-qa='login-button']");
    
    private final By signupNameInput = By.cssSelector("[data-qa='signup-name']");
    private final By signupEmailInput = By.cssSelector("[data-qa='signup-email']");
    private final By signupButton = By.cssSelector("[data-qa='signup-button']");
    private final By signupErrorLabel = By.xpath("//form[@action='/signup']//p");
    private final By loginErrorLabel = By.xpath("//form[@action='/login']//p");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public HomePage login(String email, String password) {
        attemptLogin(email, password);
        return new HomePage(driver);
    }
    
    public void attemptLogin(String email, String password) {
        type(loginEmailInput, email);
        type(loginPasswordInput, password);
        click(loginButton);
    }

    public SignupPage signup(String name, String email) {
        type(signupNameInput, name);
        type(signupEmailInput, email);
        click(signupButton);
        return new SignupPage(driver);
    }
    
    public String getSignupError() {
        return waitForElementVisible(signupErrorLabel).getText();
    }
    
    public String getLoginError() {
        return waitForElementVisible(loginErrorLabel).getText();
    }
}
