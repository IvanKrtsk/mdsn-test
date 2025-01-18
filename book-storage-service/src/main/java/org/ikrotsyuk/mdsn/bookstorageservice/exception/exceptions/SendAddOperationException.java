package org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions;

public class SendAddOperationException extends RuntimeException{
    public SendAddOperationException(int id){
        super(String.format("Catch an exception while mapping {add} operation object with book id: %d", id));
    }
}
