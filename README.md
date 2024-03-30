# Spring-Boot-Microservices

## Setup

### Setting up Keycloak

1. **Pull the Keycloak Docker Image**: If you haven't already done so, you need to pull the Keycloak Docker image. Run the following command in your terminal:

    ```bash
    docker pull quay.io/keycloak/keycloak:24.0.2
    ```

2. **Start Keycloak Container**: Once the image is downloaded, you can start a Keycloak container. Run the following command in your terminal:

    ```bash
    docker run -p 8181:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:24.0.2 start-dev
    ```

   This command starts Keycloak with the admin credentials set to "admin/admin". You can adjust the credentials as needed.

3. **Access Keycloak Admin Console**: Open your web browser and navigate to [http://localhost:8181/auth](http://localhost:8181/auth). You can log in using the admin credentials specified in the previous step.

## Further Information

For more information on how to use Keycloak with Spring Boot, refer to the [official documentation](https://www.keycloak.org/docs/latest/securing_apps/#_spring_boot_adapter).

For instructions on setting up Keycloak using Docker, visit [https://www.keycloak.org/getting-started/getting-started-docker](https://www.keycloak.org/getting-started/getting-started-docker).