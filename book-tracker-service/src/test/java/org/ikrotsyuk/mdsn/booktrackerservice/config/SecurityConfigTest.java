package org.ikrotsyuk.mdsn.booktrackerservice.config;

import org.ikrotsyuk.mdsn.booktrackerservice.entity.AvailableBookEntity;
import org.ikrotsyuk.mdsn.booktrackerservice.repository.AvailableBooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.lifecycle.Startables;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Testcontainers
public class SecurityConfigTest {
    @Container
    public static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("pgvector/pgvector:pg16")
            .withDatabaseName("mdsntest")
            .withUsername("postgrestest")
            .withPassword("postgrestest");
    @Container
    public static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.4.0");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AvailableBooksRepository availableBooksRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        Startables.deepStart(postgresContainer, kafkaContainer).join();

        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);

        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);

        registry.add("server.port", () -> "8084");
    }

    @BeforeEach
    void setUp(){
        availableBooksRepository.deleteAll();
    }

    @Test
    public void unauthorisedAccessTest() throws Exception {
        mockMvc.perform(get("/book-tracker/all")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void anyRoleMethodAccessWithUser_test() throws Exception {
        AvailableBookEntity availableBookEntity = new AvailableBookEntity(1, true, null, null);
        availableBooksRepository.save(availableBookEntity);
        mockMvc.perform(get("/book-tracker/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void anyRoleMethodAccessWithManager_test() throws Exception {
        AvailableBookEntity availableBookEntity = new AvailableBookEntity(1, true, null, null);
        availableBooksRepository.save(availableBookEntity);
        mockMvc.perform(get("/book-tracker/all"))
                .andExpect(status().isOk());
    }
}
