package com.automationexercise.qe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductsPage extends BasePage {

    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By productList = By.cssSelector(".features_items .col-sm-4");
    private final By addToCartButton = By.cssSelector(".add-to-cart");
    private final By categoryTitle = By.xpath("//h2[@class='title text-center']");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public void searchProduct(String productName) {
        type(searchInput, productName);
        click(searchButton);
    }

    public int getVisibleProductCount() {
        waitForElementVisible(categoryTitle);
        return driver.findElements(productList).size();
    }

    public void addFirstProductToCart() {
        waitForElementVisible(productList);
        List<WebElement> products = driver.findElements(productList);
        if (!products.isEmpty()) {
            click(products.get(0).findElement(addToCartButton));
            click(By.xpath("//button[text()='Continue Shopping']"));
        }
    }

    public ProductDetailsPage viewFirstProductDetails() {
        waitForElementVisible(productList);
        List<WebElement> products = driver.findElements(productList);
        if (!products.isEmpty()) {
            click(products.get(0).findElement(By.xpath(".//a[contains(text(), 'View Product')]")));
        }
        return new ProductDetailsPage(driver);
    }
    
    public void selectCategory(String parentCategory, String childCategory) {
        click(By.xpath("//a[normalize-space()='" + parentCategory + "']"));
        click(waitForElementVisible(By.xpath("//a[contains(text(), '" + childCategory + "')]")));
    }
    
    public void selectBrand(String brandName) {
        click(By.xpath("//a[contains(@href, '/brand_products/" + brandName + "')]"));
    }
    
    public String getCategoryTitle() {
        return waitForElementVisible(categoryTitle).getText();
    }
}
