Okay, this is a classic and very important demonstration for microservices! Using `@RefreshScope` with Spring Cloud Config is a cornerstone of dynamic configuration management.

Let's break down how to achieve this and then discuss common patterns.

**Goal:** Change a configuration property in your Git repository, and have the `course-management-service` pick up that change without restarting, by using `@RefreshScope` and the `/actuator/refresh` endpoint.

**Prerequisites:**
1.  **Spring Cloud Config Server:** Already set up and pointing to your GitHub repository.
2.  **GitHub Repository:** Contains configuration files for your services (e.g., `course-management-service.yml` or `course-management-service-dev.yml`).
3.  **Course Management Service:** A Spring Boot application.

---

**Step 1: Configure your Git Repository**

Let's say your GitHub repository (`my-config-repo`) has a file named:
`course-management-service-dev.yml` (assuming you're running with the `dev` profile)

```yaml
# In my-config-repo/course-management-service-dev.yml
course:
  greeting: "Hello from DEV Config!"
  welcomeMessage: "Welcome to DEV Course Management - Initial Version"
  maxStudents: 100

# IMPORTANT: Expose the refresh endpoint in the config server itself for the client
management:
  endpoints:
    web:
      exposure:
        include: refresh,health,info # Add refresh here
```
Commit and push this to your GitHub repository.

---

**Step 2: Configure Course Management Service (Client)**

1.  **Dependencies (pom.xml):**
    Make sure you have these dependencies:
    ```xml
    <dependencies>
        <!-- Spring Boot Starter Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Cloud Starter Config Client -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>

        <!-- Spring Boot Starter Actuator (for /actuator/refresh) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- For @RefreshScope to work effectively with constructor binding etc. if used
             Not strictly necessary for @Value but good practice -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bootstrap</artifactId>
        </dependency>
        <!-- Other dependencies -->
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version> <!-- e.g., 2023.0.0 or Hoxton.SR12 etc. -->
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    ```

2.  **`bootstrap.yml` (or `bootstrap.properties`):**
    Create `src/main/resources/bootstrap.yml`. This file is loaded *before* `application.yml` and is used to fetch external configuration.
    ```yaml
    spring:
      application:
        name: course-management-service # Must match the name in your Git config file
      cloud:
        config:
          uri: http://localhost:8888 # URL of your Spring Cloud Config Server
          profile: dev # Active profile for this service instance
          # Optional: fail-fast: true # Service will fail to start if Config Server is unreachable
      # If you haven't specified actuator exposure in the Git config, do it here:
    # management:
    #   endpoints:
    #     web:
    #       exposure:
    #         include: refresh,health,info
    ```
    *Note: If you put `management.endpoints.web.exposure.include: refresh` in the configuration file fetched from the Config Server (as shown in Step 1), it will also be refreshed. It's generally good to have it enabled by default either in `bootstrap.yml` or `application.yml` of the client, or in the `application.yml` general config in the Git repo.*

3.  **Create a Bean with `@RefreshScope`:**
    This bean will have its properties re-initialized when a refresh event occurs.
    ```java
    package com.example.coursemanagement.config;

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
    ```

4.  **Create a RestController to display the configuration:**
    ```java
    package com.example.coursemanagement.controller;

    import com.example.coursemanagement.config.CourseConfigProperties;
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
    ```

---

**Step 3: Demonstrate the Refresh**

1.  **Start your Spring Cloud Config Server.** (Make sure it's running on `http://localhost:8888` or adjust URI in client).
2.  **Start your Course Management Service.**
3.  **Access the endpoint:**
    Open your browser or use curl: `curl http://localhost:8080/course-details` (assuming course service runs on 8080).
    You should see:
    ```json
    {
        "greeting": "Hello from DEV Config!",
        "welcomeMessage": "Welcome to DEV Course Management - Initial Version",
        "maxStudents": 100
    }
    ```
4.  **Change Configuration in Git:**
    *   Go to your GitHub repository (`my-config-repo`).
    *   Edit `course-management-service-dev.yml`.
    *   Change a value, for example:
        ```yaml
        # In my-config-repo/course-management-service-dev.yml
        course:
          greeting: "Hello from Updated DEV Config!" # Changed
          welcomeMessage: "Welcome to DEV Course Management - V2" # Changed
          maxStudents: 150 # Changed
        management: # Keep this
          endpoints:
            web:
              exposure:
                include: refresh,health,info
        ```
    *   Commit and push the changes to GitHub.

5.  **Trigger the Refresh (without restarting the service):**
    You need to send an HTTP POST request to the `/actuator/refresh` endpoint of your `course-management-service`.
    Using curl:
    ```bash
    curl -X POST http://localhost:8080/actuator/refresh
    ```
    If successful, you'll get a JSON response listing the properties that were refreshed, e.g.:
    ```json
    ["config.client.version","course.greeting","course.maxStudents","course.welcomeMessage"]
    ```

6.  **Verify the Change:**
    Access the endpoint again: `curl http://localhost:8080/course-details`
    You should now see the updated values:
    ```json
    {
        "greeting": "Hello from Updated DEV Config!",
        "welcomeMessage": "Welcome to DEV Course Management - V2",
        "maxStudents": 150
    }
    ```
    The service picked up the changes **without a restart!**

---

**Common Patterns and More Useful Training Material Suggestions:**

1.  **Spring Cloud Bus (e.g., with RabbitMQ or Kafka):**
    *   **Problem:** Manually hitting `/actuator/refresh` is fine for one instance, but not for a scaled-out microservice with many instances.
    *   **Solution:** Spring Cloud Bus links microservice instances with a message broker (RabbitMQ/Kafka). When you POST to `/actuator/busrefresh` (on any one instance, or a dedicated endpoint on the Config Server if configured), it broadcasts a refresh event to all listening services.
    *   **Training Material:**
        *   Setup RabbitMQ/Kafka.
        *   Add `spring-cloud-starter-bus-amqp` (for RabbitMQ) or `spring-cloud-starter-bus-kafka` to both Config Server and client services.
        *   Configure connection details in `bootstrap.yml`.
        *   Demonstrate by running multiple instances of `course-management-service` (e.g., on different ports) and see them all refresh with a single `/actuator/busrefresh` call.

2.  **Configuration Hierarchies and Profiles:**
    *   Show how `application.yml` (or `.properties`) in Git acts as a default for all services.
    *   Show how `[service-name].yml` overrides `application.yml`.
    *   Demonstrate profile-specific configurations: `[service-name]-[profile].yml` (e.g., `course-management-service-dev.yml`, `course-management-service-prod.yml`).
    *   Explain the order of precedence.

3.  **Sensitive Configuration (Encryption/Decryption):**
    *   **Problem:** Storing database passwords or API keys in plain text in Git is a security risk.
    *   **Solution:** Config Server supports symmetric (shared key) and asymmetric (public/private key pair) encryption.
    *   **Training Material:**
        *   Generating keys using JCE.
        *   Configuring Config Server with the key.
        *   Encrypting values using the `/encrypt` endpoint of Config Server (or `spring encrypt` CLI).
        *   Storing encrypted values in Git (prefixed with `{cipher}`).
        *   Client services automatically decrypt these values upon fetching.

4.  **Config Server High Availability & Resilience:**
    *   Running multiple instances of Config Server behind a load balancer.
    *   Client-side resilience:
        *   `spring.cloud.config.fail-fast=true`: Service fails to start if Config Server is down.
        *   Using discovery service (like Eureka/Consul) for Config Server location (`spring.cloud.config.discovery.enabled=true`).

5.  **Configuration Properties (`@ConfigurationProperties`):**
    *   Show the alternative to `@Value` which is using `@ConfigurationProperties` for type-safe binding of multiple properties to a POJO. This often works better with `@RefreshScope` for complex objects.
    *   Example:
        ```java
        @Component
        @ConfigurationProperties(prefix = "course")
        @RefreshScope
        public class CourseProperties {
            private String greeting;
            private String welcomeMessage;
            private int maxStudents;
            // getters and setters
        }
        ```

6.  **Actuator Endpoints for Configuration Insights:**
    *   `/actuator/env`: Shows all environment properties, including those from Config Server.
    *   `/actuator/configprops`: Shows all `@ConfigurationProperties` beans.
    *   Explain how these can be used for debugging configuration issues.

7.  **Webhooks for Automated Refresh (Advanced):**
    *   Configure GitHub (or other Git providers) to send a webhook notification to the Config Server (or a sidecar service) when a push occurs to the config repository.
    *   This notification can then trigger the `/actuator/busrefresh` automatically. Services like Spring Cloud Config Monitor can help here.

By covering these topics, you provide a comprehensive understanding of dynamic configuration management in Spring Cloud, which is vital for robust microservice architectures. The `@RefreshScope` demo is a great starting point!