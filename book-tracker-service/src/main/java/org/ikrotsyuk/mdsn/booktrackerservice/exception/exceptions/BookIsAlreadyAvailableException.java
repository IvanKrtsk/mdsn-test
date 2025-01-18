package org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions;

public class BookIsAlreadyAvailableException extends RuntimeException {
    public BookIsAlreadyAvailableException(int id) {
        super(String.format("Book with id: %d is already available", id));
    }
}
