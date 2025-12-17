# Phone Records API - Postman Collection Guide

## Overview

This Postman collection provides comprehensive testing for the Phone Records REST API, including all positive and negative scenarios.

## Files Included

1. **Phone_Records_API_Collection.json** - Complete test collection
2. **Phone_Records_API_Environment.json** - Environment configuration

## Collection Statistics

- **Total Requests**: 25+
- **Positive Scenarios**: 6 requests
- **Negative Scenarios**: 12 requests  
- **Edge Cases**: 4 requests
- **Performance Tests**: 2 requests

## Import Instructions

### Method 1: Import Collection File

1. Open Postman
2. Click **Import** button (top left)
3. Select **Choose Files**
4. Navigate to and select:
   - `Phone_Records_API_Collection.json`
   - `Phone_Records_API_Environment.json`
5. Click **Import**

### Method 2: Import via URL (if hosted)

1. Open Postman
2. Click **Import** button
3. Select **Link** tab
4. Paste the collection URL
5. Click **Continue** → **Import**

## Environment Setup

After importing:

1. Click on **Environments** (left sidebar)
2. Select **Phone Records API - Local Environment**
3. Verify variables:
   - `baseUrl`: `http://localhost:8080`
   - `recordId`: (empty, will be set automatically)
4. Click **Save**

### Using the Environment

1. Select the environment from the dropdown (top right)
2. Environment is now active for all requests

## Collection Structure

```
📁 Phone Records API - Complete Test Suite
│
├── 📁 Positive Scenarios (6 requests)
│   ├── Create Phone Record - Valid US Number
│   ├── Create Phone Record - Valid UK Number
│   ├── Create Phone Record - Valid German Number
│   ├── Get All Phone Records
│   ├── Get Phone Record by ID - Existing
│   └── Get Phone Record by ID - Using Environment Variable
│
├── 📁 Negative Scenarios - Validation Errors (9 requests)
│   ├── Create Phone Record - Invalid Phone Number
│   ├── Create Phone Record - Too Short Number
│   ├── Create Phone Record - Missing Name
│   ├── Create Phone Record - Missing Phone Number
│   ├── Create Phone Record - Empty Name
│   ├── Create Phone Record - Empty Phone Number
│   ├── Create Phone Record - Empty JSON Body
│   └── Create Phone Record - Malformed JSON
│
├── 📁 Negative Scenarios - Not Found (3 requests)
│   ├── Get Phone Record by ID - Non-Existing
│   ├── Get Phone Record by ID - Very Large ID
│   └── Get Phone Record by ID - Zero
│
├── 📁 Edge Cases (4 requests)
│   ├── Create Phone Record - Very Long Name
│   ├── Create Phone Record - Special Characters in Name
│   ├── Create Phone Record - Unicode Name
│   └── Get All Records - Check Empty Array When No Data
│
└── 📁 Performance Tests (2 requests)
    ├── Get All Records - Response Time Check
    └── Get by ID - Response Time Check
```

## Running Tests

### Option 1: Run Individual Requests

1. Expand a folder (e.g., "Positive Scenarios")
2. Click on a request
3. Click **Send** button
4. View response in the bottom panel

### Option 2: Run Entire Folder

1. Hover over any folder name
2. Click the **▶** (Run) icon
3. Select **Run** in the popup
4. View results in Collection Runner

### Option 3: Run Entire Collection

1. Click on collection name
2. Click **Run** button
3. Configure run options:
   - Iterations: 1 (or more for load testing)
   - Delay: 0ms
   - Data file: None needed
4. Click **Run Phone Records API**
5. View test results

## Test Scenarios Explained

### 1. Positive Scenarios ✅

**Purpose**: Verify that valid requests work correctly

| Request | Description | Expected Result |
|---------|-------------|-----------------|
| Valid US Number | Create with US phone | 201 Created |
| Valid UK Number | Create with UK phone | 201 Created |
| Valid German Number | Create with DE phone | 201 Created |
| Get All Records | Retrieve all | 200 OK |
| Get by ID (Existing) | Get record ID 1 | 200 OK |
| Get by ID (Env Var) | Get using saved ID | 200 OK |

### 2. Negative Scenarios - Validation Errors ❌

**Purpose**: Verify proper validation and error handling

| Request | Issue | Expected Result |
|---------|-------|-----------------|
| Invalid Phone Number | Wrong format | 400 Bad Request |
| Too Short Number | Only 3 digits | 400 or 503 |
| Missing Name | No name field | 400 Bad Request |
| Missing Phone | No phone field | 400 Bad Request |
| Empty Name | name: "" | 400 Bad Request |
| Empty Phone | phoneNumber: "" | 400 Bad Request |
| Empty JSON | {} | 400 Bad Request |
| Malformed JSON | Invalid syntax | 400 Bad Request |

### 3. Negative Scenarios - Not Found ❌

**Purpose**: Verify 404 error handling

| Request | ID Used | Expected Result |
|---------|---------|-----------------|
| Non-Existing | 999 | 404 Not Found |
| Very Large ID | 999999 | 404 Not Found |
| Zero ID | 0 | 404 Not Found |

### 4. Edge Cases 🔍

**Purpose**: Test boundary conditions

| Request | Test Case | Expected Behavior |
|---------|-----------|-------------------|
| Very Long Name | 200+ characters | Accept or reject gracefully |
| Special Characters | O'Brien, José | Accept and preserve |
| Unicode Name | 山田太郎 | Accept and preserve |
| Empty Database | No records yet | Return empty array |

### 5. Performance Tests ⚡

**Purpose**: Verify response times

| Request | Max Time | Expected |
|---------|----------|----------|
| Get All Records | 500ms | Pass |
| Get by ID | 300ms | Pass |

## Automated Tests

Each request includes automated tests that verify:

### Status Code Tests
```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});
```

### Response Structure Tests
```javascript
pm.test("Response has id", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
});
```

### Response Data Tests
```javascript
pm.test("Response has correct name", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.name).to.eql("John Doe");
});
```

### Performance Tests
```javascript
pm.test("Response time is less than 500ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(500);
});
```

## Environment Variables

### Pre-configured Variables

| Variable | Value | Purpose |
|----------|-------|---------|
| baseUrl | http://localhost:8080 | API base URL |
| recordId | (empty) | Stores created record ID |

### Dynamic Variables

The collection automatically saves the `recordId` from created records:

```javascript
pm.environment.set("recordId", jsonData.id);
```

This allows subsequent requests to use `{{recordId}}` in the URL.

## Running in Different Environments

### Local Development (Default)
```
baseUrl: http://localhost:8080
```

### Docker with Custom Port
```
baseUrl: http://localhost:8081
```

### Remote Server
```
baseUrl: https://api.yourserver.com
```

To change environment:
1. Click on environment name
2. Edit `baseUrl` value
3. Save changes

## Test Execution Order

For best results, run in this order:

1. **Positive Scenarios** (creates test data)
2. **Get All Records** (verifies data created)
3. **Get by ID** (tests retrieval)
4. **Negative Scenarios** (tests error handling)
5. **Edge Cases** (tests boundaries)
6. **Performance Tests** (measures speed)

## Expected Test Results

When running the full collection against a fresh database:

- ✅ Positive Scenarios: **6 passed** (100%)
- ⚠️ Negative - Validation: **9 passed** (100%)
- ⚠️ Negative - Not Found: **3 passed** (100%)
- ✅ Edge Cases: **3-4 passed** (75-100%, depends on validation)
- ✅ Performance: **2 passed** (100%, depends on system)

**Total**: ~23-24 tests passed

## Troubleshooting

### Issue: Connection Refused

**Problem**: Cannot reach API
**Solution**: 
- Ensure API is running: `docker compose --profile h2 ps`
- Check baseUrl in environment matches actual URL
- Verify port 8080 is not blocked

### Issue: Phone Validation Always Passes

**Problem**: Invalid phones are being accepted
**Solution**:
- Set `PHONE_VALIDATION_API_KEY` environment variable
- Check NumVerify API key is valid
- Or disable validation for testing: `PHONE_VALIDATION_ENABLED=false`

### Issue: All Tests Fail

**Problem**: Wrong environment selected
**Solution**:
- Select correct environment from dropdown (top right)
- Verify environment variables are set

### Issue: 404 on Create

**Problem**: Wrong endpoint
**Solution**:
- Verify baseUrl doesn't have trailing slash
- Check endpoint path is `/api/phones`

## Advanced Usage

### Load Testing

Run multiple iterations to test load:

1. Click **Run** on collection
2. Set **Iterations** to 10 (or more)
3. Set **Delay** to 100ms
4. Click **Run**

### Data-Driven Testing

Create a CSV file with test data:

```csv
name,phoneNumber
John Doe,14158586273
Jane Smith,442071838750
Hans Mueller,493012345678
```

1. In Collection Runner
2. Select **Choose Files** under Data
3. Select your CSV file
4. Run collection

### Exporting Test Results

1. Run collection in Collection Runner
2. Click **Export Results**
3. Select format (JSON/HTML)
4. Save file

## Integration with CI/CD

### Using Newman (Postman CLI)

Install Newman:
```bash
npm install -g newman
```

Run collection:
```bash
newman run Phone_Records_API_Collection.json \
  -e Phone_Records_API_Environment.json
```

Generate HTML report:
```bash
newman run Phone_Records_API_Collection.json \
  -e Phone_Records_API_Environment.json \
  -r html
```

## Best Practices

1. **Always select the environment** before running tests
2. **Run positive scenarios first** to create test data
3. **Clear database** between full test runs for consistency
4. **Monitor response times** - slow responses may indicate issues
5. **Review failed tests** - check both request and response tabs

## Support

For issues or questions:
- Review the API documentation in `README.md`
- Check `docs/curl-examples.md` for endpoint details
- Examine request/response in Postman's console

## Version History

- **v1.0** - Initial release with 25+ test scenarios
  - Positive scenarios (6)
  - Negative scenarios (12)
  - Edge cases (4)
  - Performance tests (2)

---

**Happy Testing!** 🚀
