package com.automationexercise.qe.load;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import com.automationexercise.qe.core.config.EnvironmentConfig;

public class UserJourneySimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl(EnvironmentConfig.getInstance().getBaseUrl())
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .doNotTrackHeader("1")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader("Gatling/LoadTest");

    ScenarioBuilder scn = scenario("Full User Journey Load Test")
            .exec(http("Visit Home Page")
                    .get("/")
                    .check(status().is(200)))
            .pause(Duration.ofSeconds(3))
            
            .exec(http("View All Products")
                    .get("/products")
                    .check(status().is(200)))
            .pause(Duration.ofSeconds(5))
            
            .exec(http("Search Product API")
                    .post("/api/searchProduct")
                    .formParam("search_product", "top")
                    .check(status().is(200)))
            .pause(Duration.ofSeconds(3))
            
            .exec(http("View Product Details")
                    .get("/product_details/1")
                    .check(status().is(200)))
            .pause(Duration.ofSeconds(3))
            
            .exec(http("Add Product to Cart")
                    .post("/add_to_cart/1")
                    .check(status().is(200)))
            .pause(Duration.ofSeconds(2))
            
            .exec(http("View Cart")
                    .get("/view_cart")
                    .check(status().is(200)));

    {
        setUp(
                scn.injectOpen(
                        constantUsersPerSec(5).during(Duration.ofMinutes(1)) // 5 users every second for 1 minute
                )
        ).protocols(httpProtocol)
        .assertions(
                global().responseTime().max().lt(4000), // Max response time < 4s
                global().successfulRequests().percent().gt(95.0) // Success rate > 95%
        );
    }
}
