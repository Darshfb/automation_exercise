package com.automationexercise.qe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignupPage extends BasePage {

    private final By passwordInput = By.cssSelector("[data-qa='password']");
    private final By firstNameInput = By.cssSelector("[data-qa='first_name']");
    private final By lastNameInput = By.cssSelector("[data-qa='last_name']");
    private final By addressInput = By.cssSelector("[data-qa='address']");
    private final By countrySelect = By.cssSelector("[data-qa='country']");
    private final By stateInput = By.cssSelector("[data-qa='state']");
    private final By cityInput = By.cssSelector("[data-qa='city']");
    private final By zipcodeInput = By.cssSelector("[data-qa='zipcode']");
    private final By mobileNumberInput = By.cssSelector("[data-qa='mobile_number']");
    private final By createAccountButton = By.cssSelector("[data-qa='create-account']");
    
    private final By accountCreatedTitle = By.xpath("//b[contains(text(), 'Account Created!')]");
    private final By continueButton = By.cssSelector("[data-qa='continue-button']");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    public void fillAccountInformation(String password, String firstName, String lastName, 
                                     String address, String state, String city, 
                                     String zipcode, String mobile) {
        type(passwordInput, password);
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(addressInput, address);
        type(stateInput, state);
        type(cityInput, city);
        type(zipcodeInput, zipcode);
        type(mobileNumberInput, mobile);
    }

    public void submitAccount() {
        click(createAccountButton);
    }

    public boolean isAccountCreated() {
        return waitForElementVisible(accountCreatedTitle).isDisplayed();
    }

    public HomePage continueToHome() {
        click(continueButton);
        return new HomePage(driver);
    }
}
