package com.example.courses_management_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "courses")
public class Course {
    @Id
    private String id;
    private String title;
    private String description;
    private String instructor;

    // Constructors, getters, and setters
}
