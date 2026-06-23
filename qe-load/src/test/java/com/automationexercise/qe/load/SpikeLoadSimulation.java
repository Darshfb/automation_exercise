package com.automationexercise.qe.load;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import com.automationexercise.qe.core.config.EnvironmentConfig;

public class SpikeLoadSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl(EnvironmentConfig.getInstance().getBaseUrl())
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .doNotTrackHeader("1")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader("Gatling/LoadTest");

    ScenarioBuilder scn = scenario("Spike Load Test")
            .exec(http("Home Page")
                    .get("/")
                    .check(status().is(200)))
            .pause(Duration.ofSeconds(1))
            
            .exec(http("Products Page")
                    .get("/products")
                    .check(status().is(200)))
            .pause(Duration.ofSeconds(1))
            
            .exec(http("API Products List")
                    .get("/api/productsList")
                    .check(status().is(200)));

    {
        setUp(
                scn.injectOpen(
                        atOnceUsers(200) // Inject 200 users instantly to simulate a spike
                )
        ).protocols(httpProtocol)
        .assertions(
                global().responseTime().max().lt(5000), // Max response time < 5s
                global().successfulRequests().percent().gt(90.0) // Success rate > 90%
        );
    }
}
