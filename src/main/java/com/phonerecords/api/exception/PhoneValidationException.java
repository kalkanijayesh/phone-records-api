package com.phonerecords.api.exception;

public class PhoneValidationException extends RuntimeException {
    public PhoneValidationException(String message) {
        super(message);
    }
    
    public PhoneValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
