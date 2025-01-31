package org.ikrotsyuk.mdsn.bookstorageservice.service;

import org.ikrotsyuk.mdsn.bookstorageservice.dto.BookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.SimpleBookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.entity.BookEntity;
import org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions.*;
import org.ikrotsyuk.mdsn.bookstorageservice.kafka.implementation.KafkaProducer;
import org.ikrotsyuk.mdsn.bookstorageservice.mappers.BookMapper;
import org.ikrotsyuk.mdsn.bookstorageservice.repository.BookRepository;
import org.ikrotsyuk.mdsn.bookstorageservice.service.implementation.StorageService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Testcontainers
class StorageServiceTest {
    @Container
    public static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("pgvector/pgvector:pg16")
            .withDatabaseName("mdsntest")
            .withUsername("postgrestest")
            .withPassword("postgrestest");
    @Container
    public static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.4.0");
    @Autowired
    private StorageService storageService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookMapper bookMapper;
    private final KafkaProducer kafkaProducer = mock(KafkaProducer.class);

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
        bookRepository.deleteAll();
    }

    @Test
    void getBookList_throwsException() {
        assertThrows(BooksNotFoundException.class, () -> storageService.getBookList());
    }

    @Test
    void getBookList_returnsList() {
        BookEntity bookEntity1 = new BookEntity(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false);
        BookEntity bookEntity2 = new BookEntity(null, "978-0-316-76948-8", "The Fault in Our Stars", "Young Adult", "A novel about two teenagers with cancer who fall in love", "John Green", false);
        bookRepository.saveAll(List.of(bookEntity1, bookEntity2));
        assertEquals(2, storageService.getBookList().size());
    }

    @Test
    void getBookById_throwsBookIsDeletedException() {
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", true);
        int id = bookRepository.save(bookEntity).getId();
        assertThrows(BookIsDeletedException.class, () -> storageService.getBookById(id));
    }

    @Test
    void getBookById_throwsBookNotFoundByIdException() {
        assertThrows(BookNotFoundByIdException.class, () -> storageService.getBookById(1));
    }

    @Test
    void getBookById_returnsBookDTO() {
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false);
        int id = bookRepository.save(bookEntity).getId();
        bookEntity.setId(id);
        BookDTO bookDTO = bookMapper.toDTO(bookEntity);
        assertEquals(bookDTO, storageService.getBookById(id));
    }

    @Test
    void getBookByISBN_throwsBookIsDeletedException() {
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", true);
        bookRepository.save(bookEntity);
        assertThrows(BookIsDeletedException.class, () -> storageService.getBookByISBN("978-0-060-93546-7"));
    }

    @Test
    void getBookByISBN_throwsBookNotFoundByISBNException() {
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93286-8", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false);
        bookRepository.save(bookEntity);
        assertThrows(BookNotFoundByISBNException.class, () -> storageService.getBookByISBN("978-0-060-93546-7"));
    }

    @Test
    void getBookByISBN_returnsBookDTO() {
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false);
        int id = bookRepository.save(bookEntity).getId();
        bookEntity.setId(id);
        BookDTO bookDTO = bookMapper.toDTO(bookEntity);
        assertEquals(bookDTO, storageService.getBookByISBN("978-0-060-93546-7"));
    }

    @Test
    void addBook_throwsBookWithSameISBNFoundException() {
        SimpleBookDTO simpleBookDTO = new SimpleBookDTO("978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false);
        bookRepository.save(bookEntity);
        assertThrows(BookWithSameISBNFoundException.class, () -> storageService.addBook(simpleBookDTO));
    }

    @Test
    void addBook_wasDeletedAndReturnsBookDTO() {
        SimpleBookDTO simpleBookDTO = new SimpleBookDTO("978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", true);
        int id = bookRepository.save(bookEntity).getId();
        bookEntity.setId(id);
        BookDTO bookDTO = bookMapper.toDTO(bookEntity);
        assertEquals(bookDTO, storageService.addBook(simpleBookDTO));
    }

    @Test
    void addBook_returnsBookDTO() {
        SimpleBookDTO simpleBookDTO = new SimpleBookDTO("978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        BookDTO bookDTO1 = new BookDTO(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        BookDTO bookDTO2 = storageService.addBook(simpleBookDTO);
        bookDTO1.setId(bookDTO2.getId());
        assertEquals(bookDTO1, bookDTO2);
    }

    @Test
    void deleteBook_throwsBookIsDeletedException() {
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", true);
        int id = bookRepository.save(bookEntity).getId();
        assertThrows(BookIsDeletedException.class, () -> storageService.deleteBook(id));
    }

    @Test
    void deleteBook_throwsBookNotFoundByIdException() {
        assertThrows(BookNotFoundByIdException.class, () -> storageService.deleteBook(5));
    }

    @Test
    void deleteBook_returnsBookDTO() {
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false);
        int id = bookRepository.save(bookEntity).getId();
        BookDTO bookDTO = new BookDTO(id, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        assertEquals(bookDTO, storageService.deleteBook(id));
    }

    @Test
    void updateBookInfo_throwsBookNotFoundByISBNException() {
        assertThrows(BookNotFoundByISBNException.class, () -> storageService.updateBookInfo("978-0-060-93546-7", new SimpleBookDTO("978-0-060-93546-7", "new book name", "new book genre", "new book description", "new book author")));
    }

    @Test
    void updateBookInfo_throwsBookIsDeletedException() {
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", true);
        bookRepository.save(bookEntity);
        assertThrows(BookIsDeletedException.class, () -> storageService.updateBookInfo("978-0-060-93546-7", new SimpleBookDTO("978-0-060-93546-7", "new book name", "new book genre", "new book description", "new book author")));
    }

    @Test
    void updateBookInfo_returnsBookDTO() {
        BookEntity bookEntity = new BookEntity(null, "978-0-060-93546-7", "Not Hobbit", "Not Fantasy", "Not a fantasy novel about the adventures of Bilbo Baggins", "Not a J.R.R. Tolkien", false);
        SimpleBookDTO simpleBookDTO = new SimpleBookDTO("978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        BookDTO bookDTO = bookMapper.toDTO(simpleBookDTO);
        int id = bookRepository.save(bookEntity).getId();
        bookDTO.setId(id);
        assertEquals(bookDTO, storageService.updateBookInfo("978-0-060-93546-7", simpleBookDTO));
    }
}
