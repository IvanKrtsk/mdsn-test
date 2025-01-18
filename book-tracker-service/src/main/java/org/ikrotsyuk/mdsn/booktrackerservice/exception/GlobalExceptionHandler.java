package org.ikrotsyuk.mdsn.booktrackerservice.exception;

import org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BookIsAlreadyAvailableException.class)
    public ResponseEntity<String> handleBookIsAlreadyAvailableException(BookIsAlreadyAvailableException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookIsNotAvailableException.class)
    public ResponseEntity<String> handleBookIsNotAvailableException(BookIsNotAvailableException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookNotFoundByIdException.class)
    public ResponseEntity<String> handleBookNotFoundByIdException(BookNotFoundByIdException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BooksNotFoundException.class)
    public ResponseEntity<String> handleBooksNotFoundException(BooksNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReturnTimeIsBeforeBorrowException.class)
    public ResponseEntity<String> handleReturnTimeIsBeforeBorrowException(ReturnTimeIsBeforeBorrowException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
