1. config-repo if it's dirty it will not use the local copy. It will clone the config server repository every time the application starts.

2. if the service is config to integrate with config service then it will fetch the configuration from config service and merge it with your local configuration. 

- For example:
  - if you have a property in application.yml and the same property in config service then the value from config service will be used. 
  - if you have a property in application.yml and not in config service then the value from application.yml will be used.
3. if you want to use local configuration then you need to disable the config service integration by commented out the starter dependency for config service in pom.xml
4. bootstrap.yml. This file is loaded before application.yml and is used to fetch external configuration.


--------
Common Patterns and More Useful Training Material Suggestions:

Spring Cloud Bus (e.g., with RabbitMQ or Kafka):

Problem: Manually hitting /actuator/refresh is fine for one instance, but not for a scaled-out microservice with many instances.

Solution: Spring Cloud Bus links microservice instances with a message broker (RabbitMQ/Kafka). When you POST to /actuator/busrefresh (on any one instance, or a dedicated endpoint on the Config Server if configured), it broadcasts a refresh event to all listening services.

Training Material:

Setup RabbitMQ/Kafka.

Add spring-cloud-starter-bus-amqp (for RabbitMQ) or spring-cloud-starter-bus-kafka to both Config Server and client services.

Configure connection details in bootstrap.yml.

Demonstrate by running multiple instances of course-management-service (e.g., on different ports) and see them all refresh with a single /actuator/busrefresh call.

Configuration Hierarchies and Profiles:

Show how application.yml (or .properties) in Git acts as a default for all services.

Show how [service-name].yml overrides application.yml.

Demonstrate profile-specific configurations: [service-name]-[profile].yml (e.g., course-management-service-dev.yml, course-management-service-prod.yml).

Explain the order of precedence.

Sensitive Configuration (Encryption/Decryption):

Problem: Storing database passwords or API keys in plain text in Git is a security risk.

Solution: Config Server supports symmetric (shared key) and asymmetric (public/private key pair) encryption.

Training Material:

Generating keys using JCE.

Configuring Config Server with the key.

Encrypting values using the /encrypt endpoint of Config Server (or spring encrypt CLI).

Storing encrypted values in Git (prefixed with {cipher}).

Client services automatically decrypt these values upon fetching.

Config Server High Availability & Resilience:

Running multiple instances of Config Server behind a load balancer.

Client-side resilience:

spring.cloud.config.fail-fast=true: Service fails to start if Config Server is down.

Using discovery service (like Eureka/Consul) for Config Server location (spring.cloud.config.discovery.enabled=true).

Configuration Properties (@ConfigurationProperties):

Show the alternative to @Value which is using @ConfigurationProperties for type-safe binding of multiple properties to a POJO. This often works better with @RefreshScope for complex objects.

Example:

@Component
@ConfigurationProperties(prefix = "course")
@RefreshScope
public class CourseProperties {
private String greeting;
private String welcomeMessage;
private int maxStudents;
// getters and setters
}
