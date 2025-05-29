package com.example.courses_management_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope // THIS IS THE KEY ANNOTATION!
public class CourseConfigProperties {

    @Value("${course.greeting:Default Greeting}") // Default value if property is not found
    private String greeting;

    @Value("${course.welcomeMessage:Default Welcome Message}")
    private String welcomeMessage;

    @Value("${course.maxStudents:50}")
    private int maxStudents;

    // Getters
    public String getGreeting() {
        return greeting;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    // Optional: Setters if you need them, but for config properties, getters are primary
}