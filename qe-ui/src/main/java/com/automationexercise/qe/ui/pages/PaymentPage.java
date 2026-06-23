package com.automationexercise.qe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentPage extends BasePage {

    private final By nameOnCardInput = By.cssSelector("[data-qa='name-on-card']");
    private final By cardNumberInput = By.cssSelector("[data-qa='card-number']");
    private final By cvcInput = By.cssSelector("[data-qa='cvc']");
    private final By expiryMonthInput = By.cssSelector("[data-qa='expiry-month']");
    private final By expiryYearInput = By.cssSelector("[data-qa='expiry-year']");
    private final By payAndConfirmButton = By.cssSelector("[data-qa='pay-button']");
    private final By successMessage = By.xpath("//p[contains(text(), 'Congratulations! Your order has been confirmed!')]");
    private final By downloadInvoiceBtn = By.xpath("//a[contains(@href, 'download_invoice')]");
    private final By continueBtn = By.cssSelector("[data-qa='continue-button']");

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public void fillPaymentDetails(String name, String card, String cvc, String month, String year) {
        type(nameOnCardInput, name);
        type(cardNumberInput, card);
        type(cvcInput, cvc);
        type(expiryMonthInput, month);
        type(expiryYearInput, year);
    }

    public void payAndConfirm() {
        click(payAndConfirmButton);
    }

    public boolean isPaymentSuccessful() {
        return waitForElementVisible(successMessage).isDisplayed();
    }
    
    public void downloadInvoice() {
        click(downloadInvoiceBtn);
    }
    
    public HomePage clickContinue() {
        click(continueBtn);
        return new HomePage(driver);
    }
}
