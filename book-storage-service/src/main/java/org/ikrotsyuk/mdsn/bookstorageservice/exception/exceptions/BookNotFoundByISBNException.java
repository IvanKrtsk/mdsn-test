package org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions;

public class BookNotFoundByISBNException extends RuntimeException{
    public BookNotFoundByISBNException(String isbn){
        super(String.format("Books with isbn: %s does not exist", isbn));
    }
}
