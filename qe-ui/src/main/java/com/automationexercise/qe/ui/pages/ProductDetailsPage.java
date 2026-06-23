package com.automationexercise.qe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {

    private final By productName = By.xpath("//div[@class='product-information']/h2");
    private final By productCategory = By.xpath("//div[@class='product-information']/p");
    private final By productPrice = By.xpath("//div[@class='product-information']/span/span");
    private final By productAvailability = By.xpath("//div[@class='product-information']//b[text()='Availability:']/..");
    private final By productCondition = By.xpath("//div[@class='product-information']//b[text()='Condition:']/..");
    private final By productBrand = By.xpath("//div[@class='product-information']//b[text()='Brand:']/..");
    private final By quantityInput = By.id("quantity");
    private final By addToCartBtn = By.cssSelector("button.cart");
    private final By reviewNameInput = By.id("name");
    private final By reviewEmailInput = By.id("email");
    private final By reviewTextInput = By.id("review");
    private final By submitReviewBtn = By.id("button-review");
    private final By reviewSuccessMsg = By.xpath("//div[@class='alert-success alert']/span");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        return waitForElementVisible(productName).getText();
    }

    public String getProductCategory() {
        return waitForElementVisible(productCategory).getText();
    }

    public String getProductPrice() {
        return waitForElementVisible(productPrice).getText();
    }

    public String getProductAvailability() {
        return waitForElementVisible(productAvailability).getText();
    }

    public String getProductCondition() {
        return waitForElementVisible(productCondition).getText();
    }

    public String getProductBrand() {
        return waitForElementVisible(productBrand).getText();
    }
    
    public void setQuantity(String quantity) {
        type(quantityInput, quantity);
    }
    
    public void addToCart() {
        click(addToCartBtn);
        click(By.xpath("//button[text()='Continue Shopping']"));
    }
    
    public void submitReview(String name, String email, String review) {
        type(reviewNameInput, name);
        type(reviewEmailInput, email);
        type(reviewTextInput, review);
        click(submitReviewBtn);
    }
    
    public boolean isReviewSuccessful() {
        return waitForElementVisible(reviewSuccessMsg).isDisplayed();
    }
}
