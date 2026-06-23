package com.automationexercise.qe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ContactUsPage extends BasePage {

    private final By nameInput = By.cssSelector("[data-qa='name']");
    private final By emailInput = By.cssSelector("[data-qa='email']");
    private final By subjectInput = By.cssSelector("[data-qa='subject']");
    private final By messageInput = By.cssSelector("[data-qa='message']");
    private final By submitButton = By.cssSelector("[data-qa='submit-button']");
    private final By successMessage = By.cssSelector(".status.alert.alert-success");

    public ContactUsPage(WebDriver driver) {
        super(driver);
    }

    public void submitContactForm(String name, String email, String subject, String message) {
        type(nameInput, name);
        type(emailInput, email);
        type(subjectInput, subject);
        type(messageInput, message);
        click(submitButton);
        try {
            driver.switchTo().alert().accept();
        } catch (org.openqa.selenium.NoAlertPresentException e) {
            // No alert present, which is expected for empty fields due to HTML5 validation
        }
    }

    public boolean isSuccessMessageDisplayed() {
        return waitForElementVisible(successMessage).isDisplayed();
    }
}
