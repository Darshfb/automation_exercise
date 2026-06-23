package com.automationexercise.qe.api.tests.smoke;

import com.automationexercise.qe.api.client.AuthApiClient;
import com.automationexercise.qe.api.client.AccountApiClient;
import com.automationexercise.qe.core.config.EnvironmentConfig;
import com.automationexercise.qe.core.utils.UserDataGenerator;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

@Feature("Authentication API")
public class AuthApiSmokeTests {

    private AuthApiClient authClient;
    private AccountApiClient accountClient;
    private Map<String, String> testUser;

    @BeforeClass
    public void setup() {
        authClient = new AuthApiClient();
        accountClient = new AccountApiClient();
        testUser = UserDataGenerator.generateRandomUser();
        
        // Ensure test user exists
        accountClient.createAccount(testUser);
    }

    @Test(groups = "smoke")
    @Story("Valid Login")
    public void testValidLoginApi() {
        Response response = authClient.verifyLogin(testUser.get("email"), testUser.get("password"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("User exists"));
    }

    @Test(groups = "smoke")
    @Story("Invalid Login - Wrong Password")
    public void testInvalidLoginWrongPasswordApi() {
        Response response = authClient.verifyLogin(testUser.get("email"), "WrongPass123!");
        Assert.assertEquals(response.getStatusCode(), 200); // Note: AutomationExercise API returns 200 with error message
        Assert.assertTrue(response.getBody().asString().contains("User not found"));
    }

    @Test(groups = "smoke")
    @Story("Invalid Login - Unregistered Email")
    public void testInvalidLoginUnregisteredEmailApi() {
        Response response = authClient.verifyLogin("not_exist@testmail.com", "Pass123!");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("User not found"));
    }

    @Test(groups = "smoke")
    @Story("Missing Parameters Login")
    public void testLoginWithoutEmailApi() {
        Response response = authClient.verifyLogin("", testUser.get("password"));
        String body = response.getBody().asString();
        // The API returns a 200 status code with a 404 responseCode in the JSON body for this specific endpoint.
        Assert.assertTrue(body.contains("404") || body.contains("User not found"), "API did not return User not found. Body: " + body);
    }

    @Test(groups = "smoke")
    @Story("Delete Account")
    public void testDeleteAccountApi() {
        Map<String, String> tempUser = UserDataGenerator.generateRandomUser();
        accountClient.createAccount(tempUser);
        
        Response response = accountClient.deleteAccount(tempUser.get("email"), tempUser.get("password"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("Account deleted"));
    }
    
    @Test(groups = "smoke")
    @Story("Get User Detail")
    public void testGetUserDetailApi() {
        Response response = accountClient.getUserDetailByEmail(testUser.get("email"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains(testUser.get("name")));
    }
    
    @Test(groups = "smoke")
    @Story("Update Account")
    public void testUpdateAccountApi() {
        Map<String, String> userToUpdate = UserDataGenerator.generateRandomUser();
        accountClient.createAccount(userToUpdate);
        
        userToUpdate.put("name", "Updated Name");
        Response response = accountClient.updateAccount(userToUpdate);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("User updated"));
        
        accountClient.deleteAccount(userToUpdate.get("email"), userToUpdate.get("password"));
    }
    
    @Test(groups = "smoke")
    @Story("Create Existing Account")
    public void testCreateExistingAccountApi() {
        Response response = accountClient.createAccount(testUser);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("Email already exists"));
    }

    @Test(groups = "smoke")
    @Story("API 9: DELETE To Verify Login")
    public void testDeleteToVerifyLoginApi() {
        Response response = authClient.deleteVerifyLogin();
        Assert.assertEquals(response.getStatusCode(), 200);
        String body = response.getBody().asString();
        Assert.assertTrue(body.contains("405") || body.contains("This request method is not supported."), "Expected 405 error, got: " + body);
    }
}
