package org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions;

public class BookNotFoundByIdException extends RuntimeException{
    public BookNotFoundByIdException(int id){
        super(String.format("Book with id: %d does not exist", id));
    }
}
