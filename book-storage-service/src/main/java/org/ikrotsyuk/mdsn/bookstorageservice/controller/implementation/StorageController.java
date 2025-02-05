package org.ikrotsyuk.mdsn.bookstorageservice.controller.implementation;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.BookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.SimpleBookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.service.implementation.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book-storage")
public class StorageController implements org.ikrotsyuk.mdsn.bookstorageservice.controller.StorageController {
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService){
        this.storageService = storageService;
    }

    @PostMapping("/add")
    public ResponseEntity<BookDTO> addBook(@RequestBody @Valid SimpleBookDTO bookDTO){
        return new ResponseEntity<>(storageService.addBook(bookDTO), HttpStatus.CREATED);
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getBookList(){
        return ResponseEntity.ok(storageService.getBookList());
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable @Parameter(description = "book id") int id){
        return ResponseEntity.ok(storageService.getBookById(id));
    }

    @GetMapping("/book/{isbn}")
    public ResponseEntity<BookDTO> getBookByISBN(@PathVariable @Pattern(regexp = "^978-[0-9]-[0-9]{3}-[0-9]{5}-[0-9]$", message = "Incorrect format (use ISBN-13)") @Parameter(description = "book isbn", example = "978-0-060-93546-7") String isbn){
        return ResponseEntity.ok(storageService.getBookByISBN(isbn));
    }

    @PatchMapping("/update/{isbn}")
    public ResponseEntity<BookDTO> updateBookByISBN(@PathVariable @Pattern(regexp = "^978-[0-9]-[0-9]{3}-[0-9]{5}-[0-9]$", message = "Incorrect format (use ISBN-13)") @Parameter(description = "book isbn", example = "978-0-060-93546-7") String isbn, @RequestBody @Valid SimpleBookDTO simpleBookDTO){
        return ResponseEntity.ok(storageService.updateBookInfo(isbn, simpleBookDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BookDTO> deleteBookByISBN(@PathVariable @Parameter(description = "book id") int id){
        return ResponseEntity.ok(storageService.deleteBook(id));
    }
}
