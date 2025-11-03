# workshop-02-02
Currency Exchange Rates Provider Service

## Overview
This is a Spring Boot application that provides currency exchange rates from external public sources. The application is configured to fetch rates from exchangeratesapi.io and stores them in a PostgreSQL database.

## Technologies
- Java 21
- Spring Boot 3.2.0
- Maven
- PostgreSQL
- Liquibase
- Docker & Docker Compose

## Features
- Scheduled fetching of exchange rates from external APIs
- REST API endpoints to query exchange rates
- PostgreSQL database with Liquibase schema management
- Dockerized application and database

## Building the Application

### Prerequisites
- Java 21
- Maven 3.x
- Docker and Docker Compose

### Build with Maven
```bash
mvn clean package
```

## Running the Application

### Using Docker Compose (Recommended)

First, build the application:
```bash
mvn clean package -DskipTests
```

Then start the containers:
```bash
docker compose up --build
```

This will:
1. Start a PostgreSQL container
2. Build and start the application container
3. Automatically run Liquibase migrations
4. Start fetching exchange rates on a scheduled basis

The application will be available at `http://localhost:8080`

### Running Locally
1. Start PostgreSQL database
2. Update `application.properties` with your database connection details
3. Run:
```bash
mvn spring-boot:run
```

## API Endpoints

### Get All Exchange Rates
```
GET /api/exchange-rates
```

### Get Latest Rate for Currency Pair
```
GET /api/exchange-rates/latest?base=EUR&target=USD
```

### Get Rate History for Currency Pair
```
GET /api/exchange-rates/history?base=EUR&target=USD
```

## Configuration

Key configuration properties in `application.properties`:

- `exchange.api.fixer.url`: URL of the exchange rates API
- `exchange.api.base-currency`: Base currency for fetching rates (default: EUR)
- `exchange.fetch.rate`: Fetch interval in milliseconds (default: 3600000 = 1 hour)

## Database Schema

The application uses Liquibase to manage database schema. The main table is `exchange_rates` which stores:
- Base currency
- Target currency
- Exchange rate
- Rate date
- Source API
- Created timestamp

## Development

### Running Tests
```bash
mvn test
```

### Stopping Docker Containers
```bash
docker compose down
```

### Cleaning Up Docker Volumes
```bash
docker compose down -v
```

