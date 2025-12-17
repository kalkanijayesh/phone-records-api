package com.phonerecords.api.exception;

public class PhoneRecordNotFoundException extends RuntimeException {
    public PhoneRecordNotFoundException(Long id) {
        super("Phone record not found with id: " + id);
    }
}
