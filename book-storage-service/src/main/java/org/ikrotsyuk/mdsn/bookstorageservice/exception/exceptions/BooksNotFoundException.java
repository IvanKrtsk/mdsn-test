package org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions;

public class BooksNotFoundException extends RuntimeException{
    public BooksNotFoundException(){
        super("There are no books in the library yet");
    }
}
