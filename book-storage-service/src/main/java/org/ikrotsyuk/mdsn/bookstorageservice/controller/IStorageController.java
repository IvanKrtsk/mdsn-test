package org.ikrotsyuk.mdsn.bookstorageservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.ikrotsyuk.mdsn.bookstorageservice.dto.SimpleBookDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface IStorageController {
    @Operation(summary = "Create new book and add it to database")
    @SecurityRequirement(name = "JWT with {MANAGER} role")
    ResponseEntity<?> addBook(@Valid SimpleBookDTO bookDTO);

    @Operation(summary = "Get all books that are not deleted")
    @SecurityRequirement(name = "JWT with any role")
    ResponseEntity<?> getBookList();

    @Operation(summary = "Get book by id")
    @SecurityRequirement(name = "JWT with any role")
    ResponseEntity<?> getBookById(int id);

    @Operation(summary = "Get book by isbn")
    @SecurityRequirement(name = "JWT with any role")
    ResponseEntity<?> getBookByISBN(String isbn);

    @Operation(summary = "Update book by isbn")
    @SecurityRequirement(name = "JWT with {MANAGER} role")
    ResponseEntity<?> updateBookByISBN(String isbn, @Valid SimpleBookDTO simpleBookDTO);

    @Operation(summary = "Delete book by id")
    @SecurityRequirement(name = "JWT with {MANAGER} role")
    ResponseEntity<?> deleteBookByISBN(int id);
}
