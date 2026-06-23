package com.automationexercise.qe.core.config;

import lombok.extern.slf4j.Slf4j;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration manager to handle environment-specific settings.
 * Supports overriding via System properties (e.g., -Denv=staging).
 */
@Slf4j
public class EnvironmentConfig {

    private static EnvironmentConfig instance;
    private Properties properties;

    private EnvironmentConfig() {
        properties = new Properties();
        loadProperties();
    }

    public static synchronized EnvironmentConfig getInstance() {
        if (instance == null) {
            instance = new EnvironmentConfig();
        }
        return instance;
    }

    private void loadProperties() {
        String env = System.getProperty("env", "qa").toLowerCase();
        String propFileName = "application-" + env + ".properties";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            if (inputStream != null) {
                properties.load(inputStream);
                log.info("Loaded configuration for environment: {}", env);
            } else {
                log.warn("Property file '{}' not found in the classpath. Falling back to default application.properties", propFileName);
                loadDefaultProperties();
            }
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            throw new RuntimeException("Failed to load environment configuration", e);
        }
    }

    private void loadDefaultProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                log.error("Default application.properties not found.");
            }
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }
    }

    public String getProperty(String key) {
        // System properties override file properties
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isEmpty()) {
            return sysProp;
        }
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    public String getBaseUrl() {
        return getProperty("base.url", "https://automationexercise.com");
    }

    public String getApiBaseUrl() {
        return getProperty("api.base.url", "https://automationexercise.com/api");
    }

    public String getBrowser() {
        return getProperty("browser", "chrome").toLowerCase();
    }
    
    public boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "true"));
    }
}
