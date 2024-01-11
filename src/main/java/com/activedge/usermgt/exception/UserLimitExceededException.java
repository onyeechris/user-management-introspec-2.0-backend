package com.activedge.usermgt.exception;

public class UserLimitExceededException extends RuntimeException {

    public UserLimitExceededException(String message) {
        super(message);
    }

}
