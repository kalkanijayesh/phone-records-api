package com.phonerecords.api.service;

import com.phonerecords.api.dto.PhoneRecordRequest;
import com.phonerecords.api.dto.PhoneRecordResponse;
import com.phonerecords.api.exception.DuplicatePhoneNumberException;
import com.phonerecords.api.exception.PhoneRecordNotFoundException;
import com.phonerecords.api.model.PhoneRecord;
import com.phonerecords.api.repository.PhoneRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhoneRecordService {
    
    private static final Logger logger = LoggerFactory.getLogger(PhoneRecordService.class);
    
    private final PhoneRecordRepository repository;
    private final PhoneValidationService validationService;
    
    @Autowired
    public PhoneRecordService(PhoneRecordRepository repository, 
                             PhoneValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }
    
    @Transactional
    public PhoneRecordResponse createPhoneRecord(PhoneRecordRequest request) {
        logger.info("Creating phone record for: {}", request.getName());

        // Check if phone number already exists
        Optional<PhoneRecord> existingRecord = repository.findByPhoneNumber(request.getPhoneNumber());
        if (existingRecord.isPresent()) {
            logger.warn("Phone number {} already exists with ID: {}",
                    request.getPhoneNumber(), existingRecord.get().getId());
            throw new DuplicatePhoneNumberException(
                    request.getPhoneNumber(),
                    existingRecord.get().getId()
            );
        }

        // Validate phone number using external API
        PhoneValidationService.ValidationResult validationResult = 
            validationService.validatePhoneNumber(request.getPhoneNumber());
        
        // Create and save the phone record
        PhoneRecord phoneRecord = new PhoneRecord(
            request.getName(),
            request.getPhoneNumber(),
            validationResult.getCountryCode()
        );
        
        PhoneRecord savedRecord = repository.save(phoneRecord);
        logger.info("Phone record created successfully with id: {}", savedRecord.getId());
        
        return PhoneRecordResponse.fromEntity(savedRecord);
    }
    
    @Transactional(readOnly = true)
    public List<PhoneRecordResponse> getAllPhoneRecords() {
        logger.info("Fetching all phone records");
        return repository.findAll()
            .stream()
            .map(PhoneRecordResponse::fromEntity)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PhoneRecordResponse getPhoneRecordById(Long id) {
        logger.info("Fetching phone record with id: {}", id);
        PhoneRecord phoneRecord = repository.findById(id)
            .orElseThrow(() -> new PhoneRecordNotFoundException(id));
        return PhoneRecordResponse.fromEntity(phoneRecord);
    }
}
