package org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions;

public class BookIsNotAvailableException extends RuntimeException {
    public BookIsNotAvailableException(int id) {
        super(String.format("Book with id: %d is not available", id));
    }
}
