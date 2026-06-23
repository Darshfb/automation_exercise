package com.automationexercise.qe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private final By cartItems = By.cssSelector("tr[id^='product-']");
    private final By checkoutButton = By.xpath("//a[contains(text(), 'Proceed To Checkout')]");
    private final By subscribeEmailInput = By.id("susbscribe_email");
    private final By subscribeButton = By.id("subscribe");
    private final By subscribeSuccessMsg = By.id("success-subscribe");
    private final By cartQuantity = By.cssSelector("td.cart_quantity button");
    private final By cartDeleteBtn = By.cssSelector("a.cart_quantity_delete");
    
    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public void proceedToCheckout() {
        click(checkoutButton);
    }
    
    public void submitSubscription(String email) {
        type(subscribeEmailInput, email);
        click(subscribeButton);
    }
    
    public boolean isSubscriptionSuccessful() {
        return waitForElementVisible(subscribeSuccessMsg).isDisplayed();
    }
    
    public String getFirstItemQuantity() {
        return waitForElementVisible(cartQuantity).getText();
    }
    
    public void removeFirstItem() {
        click(cartDeleteBtn);
    }
}
