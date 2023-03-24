package com.car.leasing;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = AbstractIT.DockerMySqlDataSourceInitializer.class)
@Testcontainers
public abstract class AbstractIT {

    public static MySQLContainer<?> mySQLDBContainer = new MySQLContainer<>("mysql:8.0.32");

    @BeforeAll
    static void init() {
        mySQLDBContainer.withDatabaseName("test");
        mySQLDBContainer.withUsername("user");
        mySQLDBContainer.withPassword("password");
        mySQLDBContainer.start();
    }

    @AfterAll
    static void cleanUp() {
        mySQLDBContainer.stop();
    }

    public static class DockerMySqlDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.datasource.url=" + mySQLDBContainer.getJdbcUrl(),
                    "spring.datasource.username=" + mySQLDBContainer.getUsername(),
                    "spring.datasource.password=" + mySQLDBContainer.getPassword()
            );
        }
    }
}
