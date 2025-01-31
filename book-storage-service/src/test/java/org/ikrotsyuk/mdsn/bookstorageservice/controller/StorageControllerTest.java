package org.ikrotsyuk.mdsn.bookstorageservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ikrotsyuk.mdsn.bookstorageservice.controller.implementation.StorageController;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.BookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.SimpleBookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.service.implementation.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StorageControllerTest {
    @InjectMocks
    private StorageController storageController;
    @Mock
    private StorageService storageService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(storageController).build();
    }

    @Test
    void getBookList() throws Exception {
        BookDTO bookDTO1 = new BookDTO(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        BookDTO bookDTO2 = new BookDTO(2, "978-0-316-76948-8", "The Fault in Our Stars", "Young Adult", "A novel about two teenagers with cancer who fall in love", "John Green");
        when(storageService.getBookList()).thenReturn(List.of(bookDTO1, bookDTO2));
        mockMvc.perform(get("/book-storage/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(storageService, times(1)).getBookList();
    }

    @Test
    void getBookById() throws Exception {
        BookDTO bookDTO = new BookDTO(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        when(storageService.getBookById(anyInt())).thenReturn(bookDTO);
        mockMvc.perform(get("/book-storage/books/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("978-0-060-93546-7"));
        verify(storageService, times(1)).getBookById(1);
    }

    @Test
    void addBook() throws Exception {
        SimpleBookDTO simpleBookDTO = new SimpleBookDTO("978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        BookDTO bookDTO = new BookDTO(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        String bookJson = objectMapper.writeValueAsString(simpleBookDTO);
        when(storageService.addBook(simpleBookDTO)).thenReturn(bookDTO);
        mockMvc.perform(post("/book-storage/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("978-0-060-93546-7"));
        verify(storageService, times(1)).addBook(simpleBookDTO);
    }

    @Test
    void getBookByISBN() throws Exception {
        BookDTO bookDTO = new BookDTO(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        when(storageService.getBookByISBN(anyString())).thenReturn(bookDTO);
        mockMvc.perform(get("/book-storage/book/{isbn}", "978-0-060-93546-7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("978-0-060-93546-7"));
        verify(storageService, times(1)).getBookByISBN(anyString());
    }

    @Test
    void updateBookByISBN() throws Exception{
        SimpleBookDTO simpleBookDTO = new SimpleBookDTO("978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        BookDTO bookDTO = new BookDTO(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        String bookJson = objectMapper.writeValueAsString(simpleBookDTO);
        when(storageService.updateBookInfo(anyString(), eq(simpleBookDTO))).thenReturn(bookDTO);
        mockMvc.perform(patch("/book-storage/update/{isbn}", "978-0-060-93546-7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("978-0-060-93546-7"));
        verify(storageService, times(1)).updateBookInfo("978-0-060-93546-7", simpleBookDTO);
    }

    @Test
    void deleteBookByISBN() throws Exception{
        BookDTO bookDTO = new BookDTO(1, "978-0-060-93546-7", "The Hobbit", "Fantasy", "A fantasy novel about the adventures of Bilbo Baggins", "J.R.R. Tolkien");
        when(storageService.deleteBook(anyInt())).thenReturn(bookDTO);
        mockMvc.perform(delete("/book-storage/delete/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("978-0-060-93546-7"));
        verify(storageService, times(1)).deleteBook(anyInt());
    }
}
