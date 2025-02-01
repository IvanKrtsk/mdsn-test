package org.ikrotsyuk.mdsn.booktrackerservice.exception;

import org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {GlobalExceptionHandler.class})
@ExtendWith(SpringExtension.class)
public class GlobalExceptionHandlerTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    private final int id = 1;

    @Test
    public void handleBookIsAlreadyAvailableException_test(){
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleBookIsAlreadyAvailableException(new BookIsAlreadyAvailableException(id));
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void handleBookIsNotAvailableException_test(){
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleBookIsNotAvailableException(new BookIsNotAvailableException(id));
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void handleBookNotFoundByIdException_test(){
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleBookNotFoundByIdException(new BookNotFoundByIdException(id));
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void handleBooksNotFoundException_test(){
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleBooksNotFoundException(new BooksNotFoundException());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void handleReturnTimeIsBeforeBorrowException_test(){
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleReturnTimeIsBeforeBorrowException(new ReturnTimeIsBeforeBorrowException(localDateTimeNow.minusDays(1), localDateTimeNow));
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
