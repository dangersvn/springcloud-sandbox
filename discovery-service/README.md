# discovery-service

### Start with local profile
```mvn spring-boot:run -Dspring-boot.run.profiles=local```

=> It will look for the config location from the `application-local.yml` file.

For example: file:///home/dang/workspaces/learn/microservices/config-repo

### Start with default configuration
```mvn spring-boot:run```

Profile: `default` => It will look for the config location from the `application.yml` file.
