package org.ikrotsyuk.mdsn.bookstorageservice.config;

import lombok.SneakyThrows;
import lombok.With;
import org.ikrotsyuk.mdsn.bookstorageservice.entity.BookEntity;
import org.ikrotsyuk.mdsn.bookstorageservice.repository.BookRepository;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private BookRepository bookRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        Startables.deepStart(postgresContainer, kafkaContainer).join();

        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);

        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);

        registry.add("server.port", () -> "8082");
    }

    @BeforeEach
    void setUp(){
        bookRepository.deleteAll();
    }

    @Test
    public void unauthenticatedAccess_test() throws Exception {
        mockMvc.perform(get("/book-storage/books")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void anyRoleMethodAccessWithManager_test() throws Exception{
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false);
        bookRepository.save(bookEntity);
        mockMvc.perform(get("/book-storage/books"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void anyRoleMethodAccessWithUser_test() throws Exception{
        String isbn = "978-0-060-93546-7";
        BookEntity bookEntity = new BookEntity(null, isbn, "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false);
        int id = bookRepository.save(bookEntity).getId();
        mockMvc.perform(get("/book-storage/book/" + isbn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void managerRoleMethodAccessWithUser_test() throws Exception {
        mockMvc.perform(delete("/book-storage/delete/" + anyInt())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    public void managerRoleMethodAccessWithManager_test() throws Exception{
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false);
        int id = bookRepository.save(bookEntity).getId();
        mockMvc.perform(delete("/book-storage/delete/" + id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }
}
