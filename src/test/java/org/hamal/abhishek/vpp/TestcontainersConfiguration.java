package org.hamal.abhishek.vpp;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("vpp")
            .withUsername("user")
            .withPassword("password");

    static {
        postgresContainer.start();
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return postgresContainer;
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> String.format(
                "r2dbc:postgresql://%s:%d/%s",
                postgresContainer.getHost(),
                postgresContainer.getFirstMappedPort(),
                postgresContainer.getDatabaseName()
        ));

        registry.add("spring.r2dbc.username", postgresContainer::getUsername);
        registry.add("spring.r2dbc.password", postgresContainer::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
    }

}
