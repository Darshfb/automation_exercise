package com.automationexercise.qe.contracts;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.PactSpecVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Consumer-driven contract test for the Automation Exercise Product API using JUnit5.
 */
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "automation_exercise_api", pactVersion = PactSpecVersion.V3)
public class ProductApiContractTest {

    @Pact(consumer = "qe_platform_consumer")
    public RequestResponsePact createProductsListPact(PactDslWithProvider builder) {
        return builder
                .given("Products exist in the database")
                .uponReceiving("A request to get all products")
                .path("/api/productsList")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body("{\"responseCode\": 200, \"products\": [{\"id\": 1, \"name\": \"Blue Top\", \"price\": \"Rs. 500\"}]}")
                .toPact();
    }

    @Test
    public void testGetProductsList(MockServer mockServer) throws Exception {
        URL url = new URL(mockServer.getUrl() + "/api/productsList");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        int responseCode = connection.getResponseCode();
        Assertions.assertEquals(200, responseCode);
    }
}
