package com.phonerecords.api.dto;

import com.phonerecords.api.model.PhoneRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneRecordResponse {
    
    private Long id;
    private String name;
    private String phoneNumber;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static PhoneRecordResponse fromEntity(PhoneRecord entity) {
        return new PhoneRecordResponse(
            entity.getId(),
            entity.getName(),
            entity.getPhoneNumber(),
            entity.getCountry(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
