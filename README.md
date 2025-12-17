# Phone Records REST API

A Spring Boot REST API for managing phone records with external phone number validation.

## Features

- ✅ Create phone records with name and phone number
- ✅ **Duplicate prevention** - Each phone number can only be registered once (409 Conflict)
- ✅ Validate phone numbers using external API (NumVerify)
- ✅ Retrieve all phone records
- ✅ Retrieve phone record by ID
- ✅ H2 in-memory database (default) or PostgreSQL
- ✅ Comprehensive error handling
- ✅ Docker support with Docker Compose
- ✅ RESTful API design

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (default) / **PostgreSQL** (optional)
- **Maven**
- **Docker & Docker Compose**
- **NumVerify API** for phone validation

## Prerequisites

- Docker and Docker Compose installed
- (Optional) Maven 3.9+ and Java 17+ for local development
- (Optional) NumVerify API key from https://numverify.com/ (free tier available)

## Quick Start

### Option 1: Run with H2 (In-Memory Database)

This is the simplest way to run the application:

```bash
docker compose up --build
```

The application will be available at: `http://localhost:8080`

### Option 2: Run with PostgreSQL

For a persistent database:

```bash
docker compose --profile postgres up --build
```

The application will be available at: `http://localhost:8080`

PostgreSQL will be accessible at: `localhost:5432`

## Configuration

### Environment Variables

You can configure the following environment variables:

- `PHONE_VALIDATION_API_KEY`: Your NumVerify API key (optional)
- `PHONE_VALIDATION_ENABLED`: Enable/disable phone validation (default: `true`)

#### Setting up Phone Validation

1. Get a free API key from [NumVerify](https://numverify.com/)
2. Set the environment variable before running:

```bash
export PHONE_VALIDATION_API_KEY=your_api_key_here
docker compose up --build
```

Or pass it inline:

```bash
PHONE_VALIDATION_API_KEY=your_api_key_here docker compose up --build
```

**Note**: Without an API key, the application will run but skip phone validation (validation will always pass).

### Database Configuration

#### H2 Database (Default)
- In-memory database
- Data is lost when the application stops
- H2 Console available at: `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:phonerecordsdb`
    - Username: `sa`
    - Password: (empty)

#### PostgreSQL
- Persistent database
- Data is stored in a Docker volume
- Connection details:
    - Host: `localhost`
    - Port: `5432`
    - Database: `phonerecordsdb`
    - Username: `postgres`
    - Password: `postgres`

## API Endpoints

### Base URL
```
http://localhost:8080/api/phones
```

### Endpoints Overview

| Method | Endpoint           | Description                    |
|--------|-------------------|--------------------------------|
| POST   | `/api/phones`     | Create a new phone record      |
| GET    | `/api/phones`     | Get all phone records          |
| GET    | `/api/phones/{id}`| Get a phone record by ID       |

## API Usage Examples

See [docs/curl-examples.md](docs/curl-examples.md) for detailed curl commands.

### Quick Examples

#### Create a Phone Record (Valid)
```bash
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "phoneNumber": "14158586273"
  }'
```

#### Get All Phone Records
```bash
curl http://localhost:8080/api/phones
```

#### Get Phone Record by ID
```bash
curl http://localhost:8080/api/phones/1
```

## Response Examples

### Success Response (201 Created)
```json
{
  "id": 1,
  "name": "John Doe",
  "phoneNumber": "14158586273",
  "country": "US",
  "createdAt": "2024-12-17T10:30:00",
  "updatedAt": "2024-12-17T10:30:00"
}
```

### Error Response (400 Bad Request - Invalid Phone)
```json
{
  "timestamp": "2024-12-17T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid phone number: 1234567890. Reason: Phone number validation failed",
  "path": "/api/phones"
}
```

### Error Response (409 Conflict - Duplicate Phone)
```json
{
  "timestamp": "2024-12-17T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Phone number '14158586273' already exists with ID: 1",
  "path": "/api/phones"
}
```

### Error Response (404 Not Found)
```json
{
  "timestamp": "2024-12-17T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Phone record not found with id: 999",
  "path": "/api/phones/999"
}
```

## Project Structure

```
phone-records-api/
├── src/
│   ├── main/
│   │   ├── java/com/phonerecords/api/
│   │   │   ├── PhoneRecordsApiApplication.java
│   │   │   ├── controller/
│   │   │   │   └── PhoneRecordController.java
│   │   │   ├── service/
│   │   │   │   ├── PhoneRecordService.java
│   │   │   │   └── PhoneValidationService.java
│   │   │   ├── repository/
│   │   │   │   └── PhoneRecordRepository.java
│   │   │   ├── model/
│   │   │   │   └── PhoneRecord.java
│   │   │   ├── dto/
│   │   │   │   ├── PhoneRecordRequest.java
│   │   │   │   ├── PhoneRecordResponse.java
│   │   │   │   └── ErrorResponse.java
│   │   │   └── exception/
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       ├── PhoneRecordNotFoundException.java
│   │   │       ├── InvalidPhoneNumberException.java
│   │   │       └── PhoneValidationException.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-postgres.properties
│   └── test/
├── docs/
│   └── curl-examples.md
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Development

### Running Locally (Without Docker)

1. Install Maven and Java 17
2. Set environment variables (optional):
   ```bash
   export PHONE_VALIDATION_API_KEY=your_api_key
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Building the Project

```bash
mvn clean package
```

### Running Tests

```bash
mvn test
```

## Stopping the Application

```bash
# Stop and remove containers
docker compose  down

# Or for PostgreSQL
docker compose --profile postgres down

# To also remove volumes (PostgreSQL data)
docker compose --profile postgres down -v
```

## Troubleshooting

### Port Already in Use
If port 8080 is already in use, modify the port in `docker-compose.yml`:
```yaml
ports:
  - "8081:8080"  # Change 8081 to any available port
```

### Phone Validation Not Working
- Ensure you have a valid NumVerify API key
- Check that the API key is properly set in the environment variable
- Verify the API key hasn't exceeded its free tier limit
- Set `PHONE_VALIDATION_ENABLED=false` to disable validation for testing

### Database Connection Issues (PostgreSQL)
- Ensure PostgreSQL container is healthy: `docker compose ps`
- Check logs: `docker compose logs postgres`
- Verify no other service is using port 5432

## API Testing

### Postman Collection

A comprehensive Postman collection is available with 24+ automated tests covering all positive and negative scenarios:

- **Location**: `postman/` directory
- **Files**:
    - `Phone_Records_API_Collection.json` - Complete test suite
    - `Phone_Records_API_Environment.json` - Environment configuration
    - `POSTMAN_GUIDE.md` - Detailed usage guide
    - `QUICK_REFERENCE.md` - Quick start reference

**Quick Start**:
1. Import both JSON files into Postman
2. Select the environment from the dropdown
3. Run the collection

See [postman/POSTMAN_GUIDE.md](postman/POSTMAN_GUIDE.md) for complete instructions.

### cURL Examples

For detailed API documentation with all curl examples, see [docs/curl-examples.md](docs/curl-examples.md).

## License

This project is for interview/challenge purposes.

## Contact

For questions or issues, please refer to the challenge requirements document.