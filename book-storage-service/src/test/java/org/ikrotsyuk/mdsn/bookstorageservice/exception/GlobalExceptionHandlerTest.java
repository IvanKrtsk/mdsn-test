package org.ikrotsyuk.mdsn.bookstorageservice.exception;

import org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {GlobalExceptionHandler.class})
@ExtendWith(SpringExtension.class)
public class GlobalExceptionHandlerTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;
    private final int id = 1;
    private final String isbn = "978-0-060-93546-7";

    @Test
    public void handleBookNotFoundByIdException_test() {
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleBookNotFoundByIdException(new BookNotFoundByIdException(id));
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void handleBookNotFoundByISBNException_test() {
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleBookNotFoundByISBNException(new BookNotFoundByISBNException(isbn));
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void handleBookWithSameISBNFoundException_test() {
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleBookWithSameISBNFoundException(new BookWithSameISBNFoundException(isbn));
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void handleBooksNotFoundException_test() {
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleBooksNotFoundException(new BooksNotFoundException());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void handleBookIsDeletedException_test(){
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleBookIsDeletedException(new BookIsDeletedException(id));
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void handleSendAddOperationException_test(){
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleSendAddOperationException(new SendAddOperationException(id));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void handleSendRemoveOperationException(){
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleSendRemoveOperationException(new SendRemoveOperationException(id));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}
