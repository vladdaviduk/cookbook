package com.cookbook.exceptions;

public class ValidationException extends ServiceRuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
