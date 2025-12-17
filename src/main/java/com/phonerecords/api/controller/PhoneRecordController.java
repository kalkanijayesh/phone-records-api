package com.phonerecords.api.controller;

import com.phonerecords.api.dto.PhoneRecordRequest;
import com.phonerecords.api.dto.PhoneRecordResponse;
import com.phonerecords.api.service.PhoneRecordService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phones")
public class PhoneRecordController {
    
    private static final Logger logger = LoggerFactory.getLogger(PhoneRecordController.class);
    
    private final PhoneRecordService phoneRecordService;
    
    @Autowired
    public PhoneRecordController(PhoneRecordService phoneRecordService) {
        this.phoneRecordService = phoneRecordService;
    }
    
    @PostMapping
    public ResponseEntity<PhoneRecordResponse> createPhoneRecord(
            @Valid @RequestBody PhoneRecordRequest request) {
        logger.info("POST /api/phones - Creating phone record");
        PhoneRecordResponse response = phoneRecordService.createPhoneRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<PhoneRecordResponse>> getAllPhoneRecords() {
        logger.info("GET /api/phones - Fetching all phone records");
        List<PhoneRecordResponse> records = phoneRecordService.getAllPhoneRecords();
        return ResponseEntity.ok(records);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PhoneRecordResponse> getPhoneRecordById(@PathVariable Long id) {
        logger.info("GET /api/phones/{} - Fetching phone record", id);
        PhoneRecordResponse response = phoneRecordService.getPhoneRecordById(id);
        return ResponseEntity.ok(response);
    }
}
