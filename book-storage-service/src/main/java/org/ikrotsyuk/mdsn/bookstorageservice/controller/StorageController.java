package org.ikrotsyuk.mdsn.bookstorageservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.SimpleBookDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface StorageController {
    @Operation(summary = "Create new book and add it to database")
    ResponseEntity<?> addBook(@Valid SimpleBookDTO bookDTO);

    @Operation(summary = "Get all books that are not deleted")
    ResponseEntity<?> getBookList();

    @Operation(summary = "Get book by id")
    ResponseEntity<?> getBookById(@Parameter(description = "book id") int id);

    @Operation(summary = "Get book by isbn")
    ResponseEntity<?> getBookByISBN(@Pattern(regexp = "^978-[0-9]-[0-9]{3}-[0-9]{5}-[0-9]$", message = "Incorrect format (use ISBN-13)")
                                    @Parameter(description = "book isbn", example = "978-0-060-93546-7") String isbn);

    @Operation(summary = "Update book by isbn")
    ResponseEntity<?> updateBookByISBN(@Pattern(regexp = "^978-[0-9]-[0-9]{3}-[0-9]{5}-[0-9]$", message = "Incorrect format (use ISBN-13)")
                                       @Parameter(description = "book isbn", example = "978-0-060-93546-7") String isbn, @Valid SimpleBookDTO simpleBookDTO);

    @Operation(summary = "Delete book by id")
    ResponseEntity<?> deleteBookByISBN(@Parameter(description = "book id") int id);
}
