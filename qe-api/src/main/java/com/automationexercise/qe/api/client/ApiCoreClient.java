package com.automationexercise.qe.api.client;

import com.automationexercise.qe.core.config.EnvironmentConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

public abstract class ApiCoreClient {

    protected RequestSpecification requestSpec;

    public ApiCoreClient() {
        RestAssured.baseURI = EnvironmentConfig.getInstance().getApiBaseUrl();
        
        requestSpec = new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .setContentType("application/x-www-form-urlencoded")
                .build();
    }
}
