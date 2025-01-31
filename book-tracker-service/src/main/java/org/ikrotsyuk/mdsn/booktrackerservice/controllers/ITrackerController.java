package org.ikrotsyuk.mdsn.booktrackerservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;

public interface ITrackerController {
    @Operation(summary = "Take book by id")
    ResponseEntity<?> takeBook(@Parameter(description = "book id") int id, @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}$",
            message = "Incorrect data format (use YYYY-MM-DDTHH:MM:SS") String returnBy);

    @Operation(summary = "Return book by id")
    ResponseEntity<?> returnBook(@Parameter(description = "book id") int id);

    @Operation(summary = "Get all available books")
    ResponseEntity<?> getAvailableBooks();
}
