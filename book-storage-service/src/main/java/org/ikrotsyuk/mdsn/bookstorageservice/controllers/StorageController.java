package org.ikrotsyuk.mdsn.bookstorageservice.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.SimpleBookDTO;
import org.ikrotsyuk.mdsn.bookstorageservice.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book-storage")
public class StorageController {
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService){
        this.storageService = storageService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBook(@RequestBody @Valid SimpleBookDTO bookDTO){
        return ResponseEntity.ok(storageService.addBook(bookDTO));
    }

    @GetMapping("/books")
    public ResponseEntity<?> getBookList(){
        return ResponseEntity.ok(storageService.getBookList());
    }

    @GetMapping("/bookid/{id}")
    public ResponseEntity<?> getBookById(@PathVariable @Parameter(description = "book id") int id){
        return ResponseEntity.ok(storageService.getBookById(id));
    }

    @GetMapping("/bookisbn/{isbn}")
    public ResponseEntity<?> getBookByISBN(@PathVariable @Parameter(description = "book isbn", example = "978-0-060-93546-7") String isbn){
        return ResponseEntity.ok(storageService.getBookByISBN(isbn));
    }

    @PatchMapping("/update/{isbn}")
    public ResponseEntity<?> updateBookByISBN(@PathVariable @Parameter(description = "book isbn", example = "978-0-060-93546-7") String isbn, @RequestBody @Valid SimpleBookDTO simpleBookDTO){
        return ResponseEntity.ok(storageService.updateBookInfo(isbn, simpleBookDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBookByISBN(@PathVariable @Parameter(description = "book id") int id){
        storageService.deleteBook(id);
        return ResponseEntity.ok("success");
    }
}
