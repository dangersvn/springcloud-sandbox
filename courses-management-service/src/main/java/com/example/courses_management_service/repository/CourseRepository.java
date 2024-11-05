package com.example.courses_management_service.repository;

import com.example.courses_management_service.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, String> {
}
