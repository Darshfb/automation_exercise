package com.automationexercise.qe.ui.tests.smoke;

import com.automationexercise.qe.core.base.BaseTest;
import com.automationexercise.qe.core.config.EnvironmentConfig;
import com.automationexercise.qe.ui.pages.ContactUsPage;
import com.automationexercise.qe.ui.pages.HomePage;
import com.github.javafaker.Faker;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Forms & Support")
@Feature("Contact Us")
public class ContactUsTests extends BaseTest {

    private final Faker faker = new Faker();

    @BeforeMethod
    public void setupTest() {
        getDriver().get(EnvironmentConfig.getInstance().getBaseUrl());
    }

    @Test(groups = {"smoke", "regression"})
    @Story("Submit Contact Us Form Successfully")
    public void testSubmitContactUsForm() {
        HomePage homePage = new HomePage(getDriver());
        ContactUsPage contactPage = homePage.navigateToContactUs();
        
        contactPage.submitContactForm(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                "Help with my order",
                "I ordered a product but it never arrived. Please help."
        );
        
        Assert.assertTrue(contactPage.isSuccessMessageDisplayed(), "Success message should be displayed after form submission");
    }

    @Test(groups = {"regression"})
    @Story("TC_071: Submit form with file attachment")
    public void testSubmitContactFormWithFile() throws Exception {
        HomePage homePage = new HomePage(getDriver());
        ContactUsPage contactPage = homePage.navigateToContactUs();

        // Create a temporary dummy file to upload
        java.io.File tempFile = java.io.File.createTempFile("dummy_upload", ".txt");
        tempFile.deleteOnExit();
        try (java.io.FileWriter writer = new java.io.FileWriter(tempFile)) {
            writer.write("This is a dummy file for testing valid uploads.");
        }

        getDriver().findElement(org.openqa.selenium.By.cssSelector("[data-qa='name']")).sendKeys(faker.name().fullName());
        getDriver().findElement(org.openqa.selenium.By.cssSelector("[data-qa='email']")).sendKeys(faker.internet().emailAddress());
        getDriver().findElement(org.openqa.selenium.By.cssSelector("[data-qa='subject']")).sendKeys("Valid file upload");
        getDriver().findElement(org.openqa.selenium.By.cssSelector("[data-qa='message']")).sendKeys("Check attached file.");

        // Attach file
        getDriver().findElement(org.openqa.selenium.By.cssSelector("input[name='upload_file']")).sendKeys(tempFile.getAbsolutePath());

        // Submit and accept alert
        getDriver().findElement(org.openqa.selenium.By.cssSelector("[data-qa='submit-button']")).click();
        getDriver().switchTo().alert().accept();

        Assert.assertTrue(contactPage.isSuccessMessageDisplayed(), "Success message should be displayed after file upload submission");
    }
}
