# Config Service

A Spring Cloud Config Server that provides centralized configuration for microservices.

## Starting the Config Service

### Start with local profile
```mvn spring-boot:run -Dspring-boot.run.profiles=local```

Profile: `local` => It will look for the config location from the `application-local.yml` file. 

For example: file:///home/dang/workspaces/learn/microservices/config-repo

### Start with default configuration
```mvn spring-boot:run```

Profile: `default` => It will look for the config location from the `application.yml` file.

## Using the Config Service

Once your config server is running, you can access configurations using the following HTTP endpoints:

### Retrieve Configuration for a Service
```GET /SERVICE-NAME/PROFILE```

For example, to get the configuration for the order-service in the development profile:

### Retrieve Specific Properties

For example, to get only the database properties for the product-service:
