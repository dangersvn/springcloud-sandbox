package com.example.courses_management_service.controller;

import com.example.courses_management_service.configuration.CourseConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;


@RestController
public class ConfigDisplayController {

    private final CourseConfigProperties courseConfig;

    @Autowired
    public ConfigDisplayController(CourseConfigProperties courseConfig) {
        this.courseConfig = courseConfig;
    }

    @GetMapping("/course-details")
    public Map<String, Object> getCourseDetails() {
        Map<String, Object> details = new HashMap<>();
        details.put("greeting", courseConfig.getGreeting());
        details.put("welcomeMessage", courseConfig.getWelcomeMessage());
        details.put("maxStudents", courseConfig.getMaxStudents());
        return details;
    }
}