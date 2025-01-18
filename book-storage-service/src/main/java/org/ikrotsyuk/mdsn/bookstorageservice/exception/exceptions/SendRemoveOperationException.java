package org.ikrotsyuk.mdsn.bookstorageservice.exception.exceptions;

public class SendRemoveOperationException extends RuntimeException {
    public SendRemoveOperationException(int id) {
        super(String.format("Catch an exception while mapping {remove} operation object with book id: %d", id));
    }
}
