package com.phonerecords.api.repository;

import com.phonerecords.api.model.PhoneRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneRecordRepository extends JpaRepository<PhoneRecord, Long> {

    /**
     * Find a phone record by phone number
     * @param phoneNumber the phone number to search for
     * @return Optional containing the phone record if found
     */
    Optional<PhoneRecord> findByPhoneNumber(String phoneNumber);

    /**
     * Check if a phone number already exists
     * @param phoneNumber the phone number to check
     * @return true if exists, false otherwise
     */
    boolean existsByPhoneNumber(String phoneNumber);
}
