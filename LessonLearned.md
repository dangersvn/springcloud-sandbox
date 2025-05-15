1. config-repo if it's dirty it will not use the local copy. It will clone the config server repository every time the application starts.

2. if the service is config to integrate with config service then it will fetch the configuration from config service and ignore your local configuration.
3. if you want to use local configuration then you need to disable the config service integration by commented out the starter dependency for config service in pom.xml