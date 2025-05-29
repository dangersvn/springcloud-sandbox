The goal is to get both the `authentication-service` and `course-management-service` integrated and running. You can assign one service to each trainee.
**Common Pre-requisites for Both Trainees (Ensure they have this):**
- Access to the codebase for both `authentication-service` and `course-management-service`.
- The URLs for the running Config Server and Discovery Service.
- Understanding of which configuration files they will need to create/modify in the Config Server's repository (e.g., `authentication-service.yml`, `course-management-service.yml`, and potentially `application.yml` for shared properties).
- Basic understanding of Maven/Gradle for adding dependencies.

**Task Breakdown:**
**Trainee 1: Integrate the `authentication-service`**
- **Objective:** Get the `authentication-service` to register with the Discovery Service and fetch its configuration from the Config Server.
- **Steps:**
    1. **Add Dependencies:**
        - Add `spring-cloud-starter-netflix-eureka-client` (or your discovery client) to `authentication-service`'s `pom.xml`.
        - Add `spring-cloud-starter-config` and `spring-cloud-starter-bootstrap` (if needed) to `authentication-service`'s `pom.xml`.

    2. **Configure Discovery Client:**
        - In `authentication-service`'s `src/main/resources/application.yml` (or `.properties`):
            - Set `spring.application.name=authentication-service`.
            - Configure `eureka.client.serviceUrl.defaultZone` to point to the Discovery Service.
            - Ensure a unique `server.port` is defined (e.g., 8081).

    3. **Enable Discovery Client:**
        - Add `@EnableDiscoveryClient` (or `@EnableEurekaClient`) to the main `AuthenticationServiceApplication.java` class.

    4. **Configure Config Client:**
        - Create/Modify `authentication-service`'s `src/main/resources/bootstrap.yml` (or `.properties`):
            - Set `spring.application.name=authentication-service` (this tells the Config Server which file to load).
            - Set `spring.cloud.config.uri` to point to the Config Server.

    5. **Verify Configuration in Config Server:**
        - Ensure a configuration file named `authentication-service.yml` (or `.properties`) exists in the Config Server's backend (e.g., Git repository). This file should contain specific configurations for the authentication service.
        - Example: It might contain JWT secrets, token expiration times, or database connection details if they are not already in a shared `application.yml` in the config repo.

    6. **Run and Verify:**
        - Start the `authentication-service`.
        - Check the Discovery Service dashboard to see if `AUTHENTICATION-SERVICE` is registered.
        - Check the logs of `authentication-service` to confirm it's fetching configuration from the Config Server (you should see lines like "Located property source..." or "Fetching config from server at...").
        - Verify that any configurations defined in `authentication-service.yml` (from the Config Server) are correctly applied (e.g., server port, custom properties).

**Trainee 2: Integrate the `course-management-service`**
- **Objective:** Get the `course-management-service` to register with the Discovery Service and fetch its configuration from the Config Server.
- **Steps:**
    1. **Add Dependencies:**
        - Add `spring-cloud-starter-netflix-eureka-client` (or your discovery client) to `course-management-service`'s `pom.xml`.
        - Add `spring-cloud-starter-config` and `spring-cloud-starter-bootstrap` (if needed) to `course-management-service`'s `pom.xml`.

    2. **Configure Discovery Client:**
        - In `course-management-service`'s `src/main/resources/application.yml` (or `.properties`):
            - Set `spring.application.name=course-management-service`.
            - Configure `eureka.client.serviceUrl.defaultZone` to point to the Discovery Service.
            - Ensure a unique `server.port` is defined (e.g., 8082, different from authentication-service).

    3. **Enable Discovery Client:**
        - Add `@EnableDiscoveryClient` (or `@EnableEurekaClient`) to the main `CourseManagementServiceApplication.java` class.

    4. **Configure Config Client:**
        - Create/Modify `course-management-service`'s `src/main/resources/bootstrap.yml` (or `.properties`):
            - Set `spring.application.name=course-management-service`.
            - Set `spring.cloud.config.uri` to point to the Config Server.

    5. **Verify Configuration in Config Server:**
        - Ensure a configuration file named `course-management-service.yml` (or `.properties`) exists in the Config Server's backend. This file should contain specific configurations for the course management service.
        - Example: Database connection details for course data, any specific API keys it might use, etc.

    6. **Run and Verify:**
        - Start the `course-management-service`.
        - Check the Discovery Service dashboard to see if `COURSE-MANAGEMENT-SERVICE` is registered.
        - Check the logs of `course-management-service` to confirm it's fetching configuration from the Config Server.
        - Verify that any configurations defined in `course-management-service.yml` (from the Config Server) are correctly applied.

**Collaborative Goal / Next Steps (If time permits or for a follow-up session):**
- **Verify Both Services:** Once both trainees have their services running and integrated, they should collectively verify:
    - Both services are listed in the Discovery Service.
    - Both services have successfully loaded their externalized configurations.

- **Inter-Service Communication (Stretch Goal):**
    - Task one of the trainees (or both collaboratively) to implement a call from `course-management-service` to `authentication-service`. For example, the course management service might need to validate a token before allowing course creation.
    - This would involve:
        - Choosing a method: `@LoadBalanced RestTemplate` or Feign Client.
        - Adding `spring-cloud-starter-openfeign` if using Feign.
        - Enabling Feign clients (`@EnableFeignClients`).
        - Defining the Feign client interface or configuring the RestTemplate.
        - Making a test call using the service name (e.g., `http://authentication-service/...`).

**Tips for the Trainees:**
- **Start Simple:** Get the basic registration and config loading working first.
- **Check Logs:** Logs are their best friend for troubleshooting.
- **Understand `spring.application.name`:** Emphasize its importance for both service discovery naming and config file naming.
- **Refresh Scope (Optional Mention):** Briefly mention that for some properties, `@RefreshScope` can be used to update them without restarting the service, but that's a more advanced topic for later.
- **Profiles:** If you use profiles (dev, qa, prod) in your Config Server, ensure they understand how to specify the active profile for their services (e.g., via `spring.profiles.active` in `bootstrap.yml` or as an environment variable).

This breakdown allows each trainee to take ownership of a service, learn the integration steps thoroughly, and then see the whole system come together. Good luck to your trainees!
