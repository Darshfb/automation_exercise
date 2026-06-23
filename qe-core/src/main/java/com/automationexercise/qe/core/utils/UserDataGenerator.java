package com.automationexercise.qe.core.utils;

import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserDataGenerator {

    private static final Faker faker = new Faker();

    public static Map<String, String> generateRandomUser() {
        Map<String, String> user = new HashMap<>();
        
        // Generate unique email to prevent collision in parallel runs
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String uniqueEmail = "qe_test_" + uuid + "@testmail.com";
        
        user.put("name", faker.name().fullName());
        user.put("email", uniqueEmail);
        user.put("password", faker.internet().password(8, 20, true, true, true));
        user.put("title", "Mr");
        
        // Date of birth
        user.put("birth_date", "15");
        user.put("birth_month", "January");
        user.put("birth_year", "1990");
        
        user.put("firstname", faker.name().firstName());
        user.put("lastname", faker.name().lastName());
        user.put("company", faker.company().name());
        user.put("address1", faker.address().streetAddress());
        user.put("address2", faker.address().secondaryAddress());
        user.put("country", "United States");
        user.put("zipcode", faker.address().zipCode());
        user.put("state", faker.address().state());
        user.put("city", faker.address().city());
        user.put("mobile_number", faker.phoneNumber().cellPhone());
        
        return user;
    }

    public static String generateUniqueEmail() {
        return "qe_test_" + UUID.randomUUID().toString().substring(0, 8) + "@testmail.com";
    }
}
