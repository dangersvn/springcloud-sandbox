package com.example.courses_management_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CoursesManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoursesManagementServiceApplication.class, args);
	}

}
