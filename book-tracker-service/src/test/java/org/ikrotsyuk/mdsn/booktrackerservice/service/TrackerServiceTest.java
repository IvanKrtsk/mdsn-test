package org.ikrotsyuk.mdsn.booktrackerservice.service;

import org.ikrotsyuk.mdsn.booktrackerservice.dto.AvailableBookDTO;
import org.ikrotsyuk.mdsn.booktrackerservice.entity.AvailableBookEntity;
import org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions.*;
import org.ikrotsyuk.mdsn.booktrackerservice.repository.AvailableBooksRepository;
import org.ikrotsyuk.mdsn.booktrackerservice.service.implementation.TrackerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.lifecycle.Startables;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
class TrackerServiceTest {
    @Container
    public static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("pgvector/pgvector:pg16")
            .withDatabaseName("mdsntest")
            .withUsername("postgrestest")
            .withPassword("postgrestest");
    @Container
    public static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.4.0");

    @Autowired
    private TrackerService trackerService;
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
    }

    @BeforeEach
    void setUp() {
        availableBooksRepository.deleteAll();
    }

    @Test
    void getAvailableBooks_throwsBooksNotFoundException(){
        assertThrows(BooksNotFoundException.class, () -> trackerService.getAvailableBooks());
    }

    @Test
    void getAvailableBooks_returnsAvailableBookDTOList(){
        AvailableBookEntity availableBookEntity1 = new AvailableBookEntity(1, true, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        AvailableBookEntity availableBookEntity2 = new AvailableBookEntity(2, true, LocalDateTime.now(), LocalDateTime.now().plusDays(2));
        availableBooksRepository.saveAll(List.of(availableBookEntity1, availableBookEntity2));
        Assertions.assertEquals(2, trackerService.getAvailableBooks().size());
    }

    @Test
    void takeBook_throwsBookNotFoundByIdException(){
        assertThrows(BookNotFoundByIdException.class, () -> trackerService.takeBook(1, LocalDateTime.now().plusDays(1).toString()));
    }

    @Test
    void takeBook_throwsReturnTimeIsBeforeBorrowException(){
        AvailableBookEntity availableBookEntity = new AvailableBookEntity(1, true, LocalDateTime.now(), null);
        availableBooksRepository.save(availableBookEntity);
        assertThrows(ReturnTimeIsBeforeBorrowException.class, () -> trackerService.takeBook(1, LocalDateTime.now().minusDays(1).toString()));
    }

    @Test
    void takeBook_throwsBookIsNotAvailableException(){
        AvailableBookEntity availableBookEntity = new AvailableBookEntity(1, false, LocalDateTime.now(), null);
        availableBooksRepository.save(availableBookEntity);
        assertThrows(BookIsNotAvailableException.class, () -> trackerService.takeBook(1, LocalDateTime.now().plusDays(1).toString()));
    }

    @Test
    void takeBook_returnsAvailableBookDTO(){
        AvailableBookEntity availableBookEntity = new AvailableBookEntity(1, true, LocalDateTime.now(), null);
        availableBooksRepository.save(availableBookEntity);
        AvailableBookDTO methodResult = trackerService.takeBook(1, LocalDateTime.now().plusDays(1).toString());
        assertNotNull(methodResult);
    }

    @Test
    void returnBook_throwsBookNotFoundByIdException(){
        assertThrows(BookNotFoundByIdException.class, () -> trackerService.returnBook(1));
    }

    @Test
    void returnBook_throwsBookIsAlreadyAvailableException(){
        AvailableBookEntity availableBookEntity = new AvailableBookEntity(1, true, LocalDateTime.now(), null);
        availableBooksRepository.save(availableBookEntity);
        assertThrows(BookIsAlreadyAvailableException.class, () -> trackerService.returnBook(1));
    }

    @Test
    void returnBook_returnsAvailableBookDTO(){
        AvailableBookEntity availableBookEntity = new AvailableBookEntity(1, false, LocalDateTime.now(), null);
        availableBooksRepository.save(availableBookEntity);
        AvailableBookDTO methodResult = trackerService.returnBook(1);
        assertNotNull(methodResult);
    }
}
