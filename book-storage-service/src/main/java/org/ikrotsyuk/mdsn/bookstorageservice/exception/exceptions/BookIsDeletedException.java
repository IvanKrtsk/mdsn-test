package org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions;

public class BookIsDeletedException extends RuntimeException{
    public BookIsDeletedException(int id){
        super(String.format("Book with id: %d is deleted", id));
    }

    public BookIsDeletedException(String isbn){
        super(String.format("Book with isbn: %s is deleted", isbn));
    }
}
