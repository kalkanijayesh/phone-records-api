package com.phonerecords.api.exception;

public class DuplicatePhoneNumberException extends RuntimeException {
    private final String phoneNumber;
    private final Long existingId;
    
    public DuplicatePhoneNumberException(String phoneNumber, Long existingId) {
        super(String.format("Phone number '%s' already exists with ID: %d", phoneNumber, existingId));
        this.phoneNumber = phoneNumber;
        this.existingId = existingId;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public Long getExistingId() {
        return existingId;
    }
}