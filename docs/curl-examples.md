# Phone Records API - cURL Examples

This document contains comprehensive cURL commands for testing all API endpoints.

## Prerequisites

- Ensure the application is running: `docker compose up --build`
- Base URL: `http://localhost:8080`
- All examples use JSON format

## Table of Contents

1. [Create Phone Record (Valid)](#1-create-phone-record-valid)
2. [Create Phone Record (Invalid)](#2-create-phone-record-invalid)
3. [Create Phone Record (Missing Fields)](#3-create-phone-record-missing-fields)
4. [Get All Phone Records](#4-get-all-phone-records)
5. [Get Phone Record by ID (Existing)](#5-get-phone-record-by-id-existing)
6. [Get Phone Record by ID (Non-Existing)](#6-get-phone-record-by-id-non-existing)

---

## 1. Create Phone Record (Valid)

Create a phone record with a valid US phone number.

### Request

```bash
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "phoneNumber": "14158586273"
  }'
```

### Expected Response (201 Created)

```json
{
   "id": 1,
   "name": "John Doe",
   "phoneNumber": "14158586273",
   "country": "US",
   "createdAt": "2024-12-17T10:30:00.123456",
   "updatedAt": "2024-12-17T10:30:00.123456"
}
```

### Notes
- Phone number format: Country code + number (e.g., 1 for US + 4158586273)
- The `country` field is automatically populated from the validation API
- **Important**: This requires a valid NumVerify API key. Without it, validation is skipped.

### Alternative Valid Phone Numbers for Testing

```bash
# UK Phone Number
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "phoneNumber": "442071838750"
  }'

# German Phone Number
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Hans Mueller",
    "phoneNumber": "493012345678"
  }'
```

---

## 2. Create Phone Record (Invalid)

Attempt to create a phone record with an invalid phone number.

### Request

```bash
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Invalid User",
    "phoneNumber": "1234567890"
  }'
```

### Expected Response (400 Bad Request)

```json
{
   "timestamp": "2024-12-17T10:30:00.123456",
   "status": 400,
   "error": "Bad Request",
   "message": "Invalid phone number: 1234567890. Reason: Phone number validation failed",
   "path": "/api/phones"
}
```

### Notes
- The validation service checks if the phone number is real and properly formatted
- Common invalid patterns: too short, incorrect country code, non-existent number

### More Invalid Examples

```bash
# Too short
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "phoneNumber": "123"
  }'

# Invalid format
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "phoneNumber": "999999999999999"
  }'
```

---

## 2.5. Create Phone Record (Duplicate Phone Number)

Attempt to create a phone record with a phone number that already exists in the database.

### Request

```bash
# First, create a record (this should succeed)
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "phoneNumber": "14158586273"
  }'

# Now try to create another record with the same phone number
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "phoneNumber": "14158586273"
  }'
```

### Expected Response (409 Conflict)

```json
{
  "timestamp": "2024-12-17T10:30:00.123456",
  "status": 409,
  "error": "Conflict",
  "message": "Phone number '14158586273' already exists with ID: 1",
  "path": "/api/phones"
}
```

### Notes
- Each phone number must be unique in the database
- The error response includes the ID of the existing record
- This prevents duplicate entries for the same phone number
- The check happens before phone validation to save API calls

---

## 3. Create Phone Record (Missing Fields)

Attempt to create a phone record with missing required fields.

### Request - Missing Phone Number

```bash
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe"
  }'
```

### Expected Response (400 Bad Request)

```json
{
  "timestamp": "2024-12-17T10:30:00.123456",
  "status": 400,
  "error": "Validation Failed",
  "message": "Phone number is required",
  "path": "/api/phones"
}
```

### Request - Missing Name

```bash
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "14158586273"
  }'
```

### Expected Response (400 Bad Request)

```json
{
  "timestamp": "2024-12-17T10:30:00.123456",
  "status": 400,
  "error": "Validation Failed",
  "message": "Name is required",
  "path": "/api/phones"
}
```

### Request - Empty JSON

```bash
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{}'
```

### Expected Response (400 Bad Request)

```json
{
  "timestamp": "2024-12-17T10:30:00.123456",
  "status": 400,
  "error": "Validation Failed",
  "message": "Name is required, Phone number is required",
  "path": "/api/phones"
}
```

---

## 4. Get All Phone Records

Retrieve all phone records from the database.

### Request

```bash
curl http://localhost:8080/api/phones
```

Or with verbose output:

```bash
curl -v http://localhost:8080/api/phones
```

### Expected Response (200 OK) - With Data

```json
[
  {
    "id": 1,
    "name": "John Doe",
    "phoneNumber": "14158586273",
    "country": "US",
    "createdAt": "2024-12-17T10:30:00.123456",
    "updatedAt": "2024-12-17T10:30:00.123456"
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "phoneNumber": "442071838750",
    "country": "GB",
    "createdAt": "2024-12-17T10:31:00.123456",
    "updatedAt": "2024-12-17T10:31:00.123456"
  }
]
```

### Expected Response (200 OK) - Empty Database

```json
[]
```

### Notes
- Returns an empty array if no records exist
- Records are returned in the order they were created

---

## 5. Get Phone Record by ID (Existing)

Retrieve a specific phone record by its ID.

### Request

```bash
curl http://localhost:8080/api/phones/1
```

With formatted output (requires jq):

```bash
curl http://localhost:8080/api/phones/1 | jq
```

### Expected Response (200 OK)

```json
{
  "id": 1,
  "name": "John Doe",
  "phoneNumber": "14158586273",
  "country": "US",
  "createdAt": "2024-12-17T10:30:00.123456",
  "updatedAt": "2024-12-17T10:30:00.123456"
}
```

### Multiple ID Examples

```bash
# Get record with ID 2
curl http://localhost:8080/api/phones/2

# Get record with ID 3
curl http://localhost:8080/api/phones/3
```

---

## 6. Get Phone Record by ID (Non-Existing)

Attempt to retrieve a phone record that doesn't exist.

### Request

```bash
curl http://localhost:8080/api/phones/999
```

With verbose output to see status code:

```bash
curl -v http://localhost:8080/api/phones/999
```

### Expected Response (404 Not Found)

```json
{
  "timestamp": "2024-12-17T10:30:00.123456",
  "status": 404,
  "error": "Not Found",
  "message": "Phone record not found with id: 999",
  "path": "/api/phones/999"
}
```

### More Non-Existing ID Examples

```bash
# Large ID
curl http://localhost:8080/api/phones/99999

# ID 0 (if no records exist)
curl http://localhost:8080/api/phones/0
```

---

## Complete Test Scenario

Here's a complete test scenario that exercises all endpoints:

```bash
# 1. Check initial state (should be empty)
echo "=== Getting all records (should be empty) ==="
curl http://localhost:8080/api/phones
echo -e "\n"

# 2. Create first record (valid)
echo "=== Creating first record ==="
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "phoneNumber": "14158586273"
  }'
echo -e "\n"

# 3. Create second record (valid)
echo "=== Creating second record ==="
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "phoneNumber": "442071838750"
  }'
echo -e "\n"

# 4. Try to create invalid record
echo "=== Attempting to create invalid record ==="
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Invalid User",
    "phoneNumber": "1234567890"
  }'
echo -e "\n"

# 5. Get all records (should have 2)
echo "=== Getting all records ==="
curl http://localhost:8080/api/phones
echo -e "\n"

# 6. Get specific record by ID
echo "=== Getting record with ID 1 ==="
curl http://localhost:8080/api/phones/1
echo -e "\n"

# 7. Try to get non-existing record
echo "=== Attempting to get non-existing record ==="
curl http://localhost:8080/api/phones/999
echo -e "\n"
```

Save this as `test-api.sh` and run with:

```bash
chmod +x test-api.sh
./test-api.sh
```

---

## Tips and Tricks

### Pretty Print JSON (with jq)

```bash
curl http://localhost:8080/api/phones | jq
```

### Save Response to File

```bash
curl http://localhost:8080/api/phones > response.json
```

### Include Response Headers

```bash
curl -i http://localhost:8080/api/phones
```

### Follow Redirects

```bash
curl -L http://localhost:8080/api/phones
```

### Set Custom Headers

```bash
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -H "X-Custom-Header: value" \
  -d '{"name": "Test", "phoneNumber": "14158586273"}'
```

### Measure Response Time

```bash
curl -w "@-" -o /dev/null -s http://localhost:8080/api/phones <<'EOF'
    time_namelookup:  %{time_namelookup}\n
       time_connect:  %{time_connect}\n
    time_appconnect:  %{time_appconnect}\n
   time_pretransfer:  %{time_pretransfer}\n
      time_redirect:  %{time_redirect}\n
 time_starttransfer:  %{time_starttransfer}\n
                    ----------\n
         time_total:  %{time_total}\n
EOF
```

---

## Troubleshooting

### Connection Refused

If you get "Connection refused":

```bash
# Check if the application is running
docker compose ps

# Check application logs
docker compose logs app-h2
```

### Invalid JSON

If you get a 400 error with "invalid JSON":

- Ensure proper JSON formatting
- Check for missing quotes or commas
- Use a JSON validator: https://jsonlint.com/

### Phone Validation Always Passes

If validation is not working:

1. Check if API key is set:
   ```bash
   docker compose logs app-h2 | grep "API key"
   ```

2. Set the API key:
   ```bash
   PHONE_VALIDATION_API_KEY=your_key docker compose up --build
   ```

3. Disable validation for testing:
   ```bash
   PHONE_VALIDATION_ENABLED=false docker compose up --build
   ```

---

## Additional Resources

- [NumVerify API Documentation](https://numverify.com/documentation)
- [Spring Boot REST API Best Practices](https://spring.io/guides/tutorials/rest/)
- [HTTP Status Codes](https://httpstatuses.com/)