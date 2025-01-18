package org.ikrotsyuk.mdsn.booktrackerservice.exception.exceptions;

import java.time.LocalDateTime;

public class ReturnTimeIsBeforeBorrowException extends RuntimeException {
    public ReturnTimeIsBeforeBorrowException(LocalDateTime returnBy, LocalDateTime borrowedAt) {
        super("Return time: " + returnBy.toString() + " is before borrow time: " + borrowedAt.toString());
    }
}
