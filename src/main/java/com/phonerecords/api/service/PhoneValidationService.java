package com.phonerecords.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonerecords.api.exception.InvalidPhoneNumberException;
import com.phonerecords.api.exception.PhoneValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PhoneValidationService {
    
    private static final Logger logger = LoggerFactory.getLogger(PhoneValidationService.class);
    
    @Value("${phone.validation.api.key:}")
    private String apiKey;
    
    @Value("${phone.validation.api.url:http://apilayer.net/api/validate}")
    private String apiUrl;
    
    @Value("${phone.validation.enabled:true}")
    private boolean validationEnabled;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public PhoneValidationService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    public ValidationResult validatePhoneNumber(String phoneNumber) {
        if (!validationEnabled) {
            logger.warn("Phone validation is disabled. Skipping validation.");
            return new ValidationResult(true, phoneNumber, "Unknown", "Validation disabled");
        }
        
        if (apiKey == null || apiKey.isEmpty()) {
            logger.warn("API key not configured. Skipping validation.");
            return new ValidationResult(true, phoneNumber, "Unknown", "No API key configured");
        }
        
        try {
            String url = String.format("%s?access_key=%s&number=%s&format=1", 
                apiUrl, apiKey, phoneNumber);
            
            logger.info("Validating phone number: {}", phoneNumber);
            String response = restTemplate.getForObject(url, String.class);
            
            JsonNode jsonNode = objectMapper.readTree(response);
            
            // Check for API errors
            if (jsonNode.has("error")) {
                JsonNode errorNode = jsonNode.get("error");
                String errorInfo = errorNode.get("info").asText();
                logger.error("API error during validation: {}", errorInfo);
                throw new PhoneValidationException(
                    "Phone validation service error: " + errorInfo
                );
            }
            
            boolean isValid = jsonNode.get("valid").asBoolean();
            String countryCode = jsonNode.has("country_code") ? 
                jsonNode.get("country_code").asText() : "Unknown";
            String carrier = jsonNode.has("carrier") ? 
                jsonNode.get("carrier").asText() : "Unknown";
            
            if (!isValid) {
                throw new InvalidPhoneNumberException(phoneNumber, 
                    "Phone number validation failed");
            }
            
            logger.info("Phone number {} is valid. Country: {}", phoneNumber, countryCode);
            return new ValidationResult(true, phoneNumber, countryCode, carrier);
            
        } catch (InvalidPhoneNumberException e) {
            throw e;
        } catch (PhoneValidationException e) {
            throw e;
        } catch (RestClientException e) {
            logger.error("Error communicating with validation API", e);
            throw new PhoneValidationException(
                "Unable to reach phone validation service. Please try again later.", e
            );
        } catch (Exception e) {
            logger.error("Unexpected error during phone validation", e);
            throw new PhoneValidationException(
                "Error validating phone number: " + e.getMessage(), e
            );
        }
    }
    
    public static class ValidationResult {
        private final boolean valid;
        private final String phoneNumber;
        private final String countryCode;
        private final String carrier;
        
        public ValidationResult(boolean valid, String phoneNumber, String countryCode, String carrier) {
            this.valid = valid;
            this.phoneNumber = phoneNumber;
            this.countryCode = countryCode;
            this.carrier = carrier;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getPhoneNumber() {
            return phoneNumber;
        }
        
        public String getCountryCode() {
            return countryCode;
        }
        
        public String getCarrier() {
            return carrier;
        }
    }
}
