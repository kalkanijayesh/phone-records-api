package com.phonerecords.api.exception;

public class InvalidPhoneNumberException extends RuntimeException {
    public InvalidPhoneNumberException(String phoneNumber) {
        super("Invalid phone number: " + phoneNumber);
    }
    
    public InvalidPhoneNumberException(String phoneNumber, String reason) {
        super("Invalid phone number: " + phoneNumber + ". Reason: " + reason);
    }
}
