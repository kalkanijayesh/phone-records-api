# Quick Start Guide

Get the Phone Records API up and running in under 2 minutes!

## Prerequisites

- Docker and Docker Compose installed
- Terminal/Command Line access

## Step 1: Run the Application

Choose one of the following options:

### Option A: H2 Database (Recommended for testing)

```bash
docker compose up --build
```

### Option B: PostgreSQL (For persistent storage)

```bash
docker compose --profile postgres up --build
```

Wait for the application to start. You should see:
```
Started PhoneRecordsApiApplication in X seconds
```

## Step 2: Test the API

Open a new terminal and run:

```bash
# Create a phone record
curl -X POST http://localhost:8080/api/phones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "phoneNumber": "14158586273"
  }'

# Get all records
curl http://localhost:8080/api/phones

# Get specific record
curl http://localhost:8080/api/phones/1
```

## Step 3: Explore More

- To test all curl examples: [Check postman directory](postman/POSTMAN_GUIDE.md)

## Optional: Phone Validation Setup

To enable real phone number validation:

1. Get a free API key from [NumVerify](https://numverify.com/)
2. Stop the application (Ctrl+C)
3. Run with API key:
   ```bash
   PHONE_VALIDATION_API_KEY=your_key_here docker compose up --build
   ```

## Stopping the Application

Press `Ctrl+C` in the terminal where the application is running, then:

```bash
docker compose  down
```

## Troubleshooting

### Port 8080 already in use?
- Change the port in `docker-compose.yml`
- Or stop the service using port 8080

### Docker not installed?
- Install from: https://docs.docker.com/get-docker/

### API not responding?
- Ensure application started successfully
- Check logs: `docker compose logs app-h2`

## Next Steps

- Review the [complete README](README.md)
- Explore [API documentation](docs/curl-examples.md)
- Examine the source code structure
- Run the automated test script: `./test-api.sh`

Enjoy testing the Phone Records API! 🚀
