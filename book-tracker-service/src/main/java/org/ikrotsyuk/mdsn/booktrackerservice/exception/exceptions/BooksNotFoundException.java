package org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions;

public class BooksNotFoundException extends RuntimeException{
    public BooksNotFoundException(){
        super("There are no books in the library yet");
    }
}
