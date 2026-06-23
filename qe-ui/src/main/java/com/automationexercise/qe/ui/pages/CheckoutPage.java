package com.automationexercise.qe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private final By placeOrderButton = By.cssSelector("[href='/payment']");
    private final By deliveryAddress = By.id("address_delivery");
    private final By billingAddress = By.id("address_invoice");
    private final By commentTextarea = By.name("message");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDeliveryAddressDisplayed() {
        return waitForElementVisible(deliveryAddress).isDisplayed();
    }

    public boolean isBillingAddressDisplayed() {
        return waitForElementVisible(billingAddress).isDisplayed();
    }

    public void enterComment(String comment) {
        type(commentTextarea, comment);
    }

    public PaymentPage placeOrder() {
        click(placeOrderButton);
        return new PaymentPage(driver);
    }
}
