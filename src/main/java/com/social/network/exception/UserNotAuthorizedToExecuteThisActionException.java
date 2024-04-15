package com.social.network.exception;

public class UserNotAuthorizedToExecuteThisActionException extends RuntimeException {

    public UserNotAuthorizedToExecuteThisActionException(String message) {
        super(message);
    }
}
