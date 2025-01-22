package org.ikrotsyuk.mdsn.bookstorageservice.service;

import org.ikrotsyuk.mdsn.bookstorageservice.dto.BookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.SimpleBookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.entity.BookEntity;
import org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions.*;
import org.ikrotsyuk.mdsn.bookstorageservice.kafka.KafkaProducer;
import org.ikrotsyuk.mdsn.bookstorageservice.mappers.BookMapper;
import org.ikrotsyuk.mdsn.bookstorageservice.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {
    @InjectMocks
    private StorageService storageService;
    @Mock
    private BookRepository bookRepository;
    @Spy
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);
    private final KafkaProducer kafkaProducer = mock(KafkaProducer.class);

    @Test
    void getBookList_throwsException(){
        Mockito.when(bookRepository.findAllByIsDeletedFalse()).thenReturn(Collections.emptyList());
        Assertions.assertThrows(BooksNotFoundException.class, () -> storageService.getBookList());
    }

    @Test
    void getBookList_returnsList(){
        BookEntity bookEntity1 = new BookEntity(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false);
        BookEntity bookEntity2 = new BookEntity(2, "978-0-316-76948-8", "The Fault in Our Stars", "Young Adult", "A novel about two teenagers with cancer who fall in love", "John Green", false);
        Mockito.when(bookRepository.findAllByIsDeletedFalse()).thenReturn(List.of(bookEntity1, bookEntity2));
        Assertions.assertEquals(2, storageService.getBookList().size());
    }

    @Test
    void getBookById_throwsBookIsDeletedException(){
        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(new BookEntity(anyInt(), "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", true)));
        Assertions.assertThrows(BookIsDeletedException.class, () -> storageService.getBookById(1));
    }

    @Test
    void getBookById_throwsBookNotFoundByIdException(){
        Mockito.when(bookRepository.findById(5)).thenReturn(Optional.empty());
        Assertions.assertThrows(BookNotFoundByIdException.class, () -> storageService.getBookById(5));
    }

    @Test
    void getBookById_returnsBookDTO(){
        BookEntity bookEntity = new BookEntity(3, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false);
        BookDTO bookDTO = bookMapper.toDTO(bookEntity);
        Mockito.when(bookRepository.findById(3)).thenReturn(Optional.of(bookEntity));
        Assertions.assertEquals(bookDTO, storageService.getBookById(3));
    }

    @Test
    void getBookByISBN_throwsBookIsDeletedException(){
        Mockito.when(bookRepository.findFirstByIsbn("978-0-060-93546-7")).thenReturn(new BookEntity(anyInt(), "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", true));
        Assertions.assertThrows(BookIsDeletedException.class, () -> storageService.getBookByISBN("978-0-060-93546-7"));
    }

    @Test
    void getBookByISBN_throwsBookNotFoundByISBNException(){
        Mockito.when(bookRepository.findFirstByIsbn("978-0-060-93546-7")).thenReturn(null);
        Assertions.assertThrows(BookNotFoundByISBNException.class, () -> storageService.getBookByISBN("978-0-060-93546-7"));
    }

    @Test
    void getBookByISBN_returnsBookDTO(){
        BookEntity bookEntity = new BookEntity(3, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false);
        BookDTO bookDTO = bookMapper.toDTO(bookEntity);
        Mockito.when(bookRepository.findFirstByIsbn("978-0-060-93546-7")).thenReturn(bookEntity);
        Assertions.assertEquals(bookDTO, storageService.getBookByISBN("978-0-060-93546-7"));
    }

    @Test
    void addBook_throwsBookWithSameISBNFoundException(){
        SimpleBookDTO simpleBookDTO = new SimpleBookDTO("978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        Mockito.when(bookRepository.existsByIsbn(simpleBookDTO.getIsbn())).thenReturn(true);
        Mockito.when(bookRepository.findFirstByIsbn("978-0-060-93546-7")).thenReturn(new BookEntity(anyInt(), "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false));
        Assertions.assertThrows(BookWithSameISBNFoundException.class, () -> storageService.addBook(simpleBookDTO));
    }

    @Test
    void addBook_wasDeletedAndReturnsBookDTO(){
        SimpleBookDTO simpleBookDTO = new SimpleBookDTO("978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        BookEntity bookEntity = new BookEntity(anyInt(), "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", true);
        BookDTO bookDTO = bookMapper.toDTO(bookEntity);
        Mockito.when(bookRepository.existsByIsbn(simpleBookDTO.getIsbn())).thenReturn(true);
        Mockito.when(bookRepository.findFirstByIsbn("978-0-060-93546-7")).thenReturn(bookEntity);
        Assertions.assertEquals(bookDTO, storageService.addBook(simpleBookDTO));
    }

    @Test
    void addBook_returnsBookDTO(){
        SimpleBookDTO simpleBookDTO = new SimpleBookDTO("978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        BookDTO bookDTO = new BookDTO(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        Mockito.when(bookRepository.existsByIsbn("978-0-060-93546-7")).thenReturn(false);
        Mockito.when(bookRepository.save(any(BookEntity.class))).thenReturn(new BookEntity(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false));
        Assertions.assertEquals(bookDTO, storageService.addBook(simpleBookDTO));
    }

    @Test
    void deleteBook_throwsBookIsDeletedException(){
        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(new BookEntity(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", true)));
        Assertions.assertThrows(BookIsDeletedException.class, () -> storageService.deleteBook(1));
    }

    @Test
    void deleteBook_throwsBookNotFoundByIdException(){
        Mockito.when(bookRepository.findById(5)).thenReturn(Optional.empty());
        Assertions.assertThrows(BookNotFoundByIdException.class, () -> storageService.deleteBook(5));
    }

    @Test
    void deleteBook_returnsBookDTO(){
        BookDTO bookDTO = new BookDTO(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(new BookEntity(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false)));
        Assertions.assertEquals(bookDTO, storageService.deleteBook(1));
    }

    @Test
    void updateBookInfo_throwsBookNotFoundByISBNException(){
        Mockito.when(bookRepository.findFirstByIsbn(anyString())).thenReturn(null);
        Assertions.assertThrows(BookNotFoundByISBNException.class, () -> storageService.updateBookInfo("978-0-060-93546-7", new SimpleBookDTO("978-0-060-93546-7", "new book name", "new book genre", "new book description", "new book author")));
    }

    @Test
    void updateBookInfo_throwsBookIsDeletedException(){
        Mockito.when(bookRepository.findFirstByIsbn(anyString())).thenReturn(new BookEntity(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", true));
        Assertions.assertThrows(BookIsDeletedException.class, () -> storageService.updateBookInfo("978-0-060-93546-7", new SimpleBookDTO("978-0-060-93546-7", "new book name", "new book genre", "new book description", "new book author")));
    }

    @Test
    void updateBookInfo_returnsBookDTO(){
        SimpleBookDTO simpleBookDTO = new SimpleBookDTO("978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        BookDTO bookDTO = bookMapper.toDTO(simpleBookDTO);
        bookDTO.setId(1);
        Mockito.when(bookRepository.findFirstByIsbn(anyString())).thenReturn(new BookEntity(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false));
        Mockito.when(bookRepository.save(any(BookEntity.class))).thenReturn(new BookEntity(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien", false));
        Assertions.assertEquals(bookDTO, storageService.updateBookInfo("978-0-060-93546-7", simpleBookDTO));
    }
}
