package org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions;

public class BookWithSameISBNFoundException extends RuntimeException {
    public BookWithSameISBNFoundException(String isbn) {
        super(String.format("Book with entered isbn: %s already exists", isbn));
    }
}
