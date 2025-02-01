package org.ikrotsyuk.mdsn.bookstorageservice.exception;

import org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotFoundByIdException.class)
    public ResponseEntity<String> handleBookNotFoundByIdException(BookNotFoundByIdException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotFoundByISBNException.class)
    public ResponseEntity<String> handleBookNotFoundByISBNException(BookNotFoundByISBNException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookWithSameISBNFoundException.class)
    public ResponseEntity<String> handleBookWithSameISBNFoundException(BookWithSameISBNFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BooksNotFoundException.class)
    public ResponseEntity<String> handleBooksNotFoundException(BooksNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookIsDeletedException.class)
    public ResponseEntity<String> handleBookIsDeletedException(BookIsDeletedException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SendAddOperationException.class)
    public ResponseEntity<String> handleSendAddOperationException(SendAddOperationException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SendRemoveOperationException.class)
    public ResponseEntity<String> handleSendRemoveOperationException(SendRemoveOperationException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}