package com.automationexercise.qe.load;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import com.automationexercise.qe.core.config.EnvironmentConfig;

public class StressLoadSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl(EnvironmentConfig.getInstance().getBaseUrl())
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .doNotTrackHeader("1")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader("Gatling/LoadTest");

    ScenarioBuilder scn = scenario("Stress Load Test")
            .exec(http("Home Page")
                    .get("/")
                    .check(status().is(200)))
            .pause(Duration.ofSeconds(2))
            
            .exec(http("Search Product API")
                    .post("/api/searchProduct")
                    .formParam("search_product", "tshirt")
                    .check(status().is(200)))
            .pause(Duration.ofSeconds(2))
            
            .exec(http("Products Catalog")
                    .get("/products")
                    .check(status().is(200)));

    {
        setUp(
                scn.injectOpen(
                        rampUsers(500).during(Duration.ofMinutes(2)) // Ramp up to 500 users over 2 minutes
                )
        ).protocols(httpProtocol)
        .assertions(
                global().responseTime().percentile4().lt(4000), // 99th percentile response time < 4s
                global().successfulRequests().percent().gt(95.0) // Success rate > 95%
        );
    }
}
