# Car Management & Fuel Tracking System

A Java-based car management and fuel tracking system built with pure Java Servlets. This project demonstrates clean OOP design, proper HTTP handling, and separation of concerns.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Quick Start](#quick-start)
- [API Endpoints](#api-endpoints)
- [CLI Commands](#cli-commands)
- [Running Tests](#running-tests)
- [Error Handling](#error-handling)
- [Configuration](#configuration)

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6** or higher
- **Git** (for cloning the repository)

Verify your installations:

```bash
java -version    # Should show Java 17+
mvn -version     # Should show Maven 3.6+
git --version    # Should show Git version
```

## Project Overview

This is a **Car Management & Fuel Tracking System** that allows users to:
- Register cars with brand, model, and year
- Track fuel entries (liters, price, odometer readings)
- View calculated fuel statistics (total fuel, total cost, average consumption)

The system consists of two main components:
1. **Backend Server**: Java application with in-memory storage (ConcurrentHashMap)
2. **CLI Client**: Standalone Java application (separate module) that communicates with the server via HTTP

### Key Features

- **RESTful API**: Standard HTTP endpoints for all operations
- **Manual Servlet**: Demonstrates servlet lifecycle with explicit request/response handling
- **In-Memory Storage**: Thread-safe storage using ConcurrentHashMap and AtomicLong
- **Service Layer**: Centralized business logic with proper separation of concerns
- **Error Handling**: Comprehensive error handling with appropriate HTTP status codes (400, 404, 500)
- **CLI Interface**: Command-line tool for easy interaction with the API

## Architecture

### Framework Choice

This implementation uses **pure Java Servlets** with embedded Jetty to demonstrate:
- Direct servlet lifecycle management
- Manual HTTP request/response handling
- Explicit servlet configuration
- Core Java servlet knowledge without framework abstractions

### Design Patterns Used

1. **DTO Pattern**: Clean separation between API contracts and domain models
2. **Repository Pattern**: Abstracts in-memory storage with ConcurrentHashMap
3. **Service Layer**: Centralizes business logic
4. **Template Method**: BaseServlet provides common utilities

### Key Design Decisions

- **BaseModel Inheritance**: Common fields (`id`, `createdAt`) in abstract base class
- **Thread Safety**: ConcurrentHashMap and AtomicLong for concurrent access
- **Separation of Concerns**: Servlets handle HTTP only; business logic in services
- **In-Memory Storage**: No database required; uses Maps/Lists as specified

### Manual Servlet Implementation

The `FuelStatsServlet` (GET `/servlet/fuel-stats?carId={id}`) demonstrates servlet knowledge:

- **Extends HttpServlet**: Properly extends `jakarta.servlet.http.HttpServlet`
- **Overrides doGet()**: Implements the GET request handler
- **Manual Query Parameter Parsing**: Uses `req.getParameter("carId")` to extract the car ID
- **Explicit Headers**: Manually sets `Content-Type: application/json` and HTTP status codes
- **Service Reuse**: Uses the same `FuelService` instance as the REST API endpoints

### CLI Implementation

The CLI module is a **separate executable** (different main class) that:
- Uses `java.net.http.HttpClient` (Java 11+) for HTTP communication
- Implements command pattern for extensibility
- Provides user-friendly error messages and validation
- Supports environment variable configuration (`API_URL`)

### Technologies Used

- **Java 17**: Core language and runtime
- **Embedded Jetty 11**: Servlet container and HTTP server
- **Gson**: JSON serialization/deserialization
- **JUnit 5**: Unit testing framework
- **Maven**: Build and dependency management

## Project Structure

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd codehills-techinical
```

### Step 2: Build the Project

```bash
mvn clean package -DskipTests
```

This will build both the backend and CLI modules.

### Step 3: Start the Backend Server

Open a terminal and run:

```bash
cd backend
mvn compile exec:java
```

You should see:

```
========================================
  Car Management Server Started
  Port: 8080
========================================

Available endpoints:
  POST   /api/cars                  - Create a car
  GET    /api/cars                  - List all cars
  POST   /api/cars/{id}/fuel        - Add fuel entry
  GET    /api/cars/{id}/fuel/stats  - Get fuel statistics
  GET    /servlet/fuel-stats?carId={id} - Manual servlet

Press Ctrl+C to stop the server
========================================
```

### Step 4: Use the CLI (in a new terminal)

Open another terminal and navigate to the CLI directory:

```bash
cd cli
```

#### Create a Car

```bash
mvn compile exec:java -q -Dexec.args="create-car --brand Toyota --model Corolla --year 2018"
```

**Output:**
```
Car created successfully!
-------------------------
ID:    1
Brand: Toyota
Model: Corolla
Year:  2018
```

#### Add Fuel Entries

```bash
mvn compile exec:java -q -Dexec.args="add-fuel --carId 1 --liters 40 --price 50 --odometer 40000"
mvn compile exec:java -q -Dexec.args="add-fuel --carId 1 --liters 45 --price 55 --odometer 40500"
mvn compile exec:java -q -Dexec.args="add-fuel --carId 1 --liters 42 --price 52 --odometer 41000"
```

**Output (for each):**
```
Fuel entry added successfully!
------------------------------
Entry ID:  1
Car ID:    1
Liters:    40.00 L
Price:     50.00
Odometer:  40000 km
```

#### Get Fuel Statistics

```bash
mvn compile exec:java -q -Dexec.args="fuel-stats --carId 1"
```

**Output:**
```
Total fuel: 127 L
Total cost: 157.00
Average consumption: 8.7 L/100km
```

## Project Structure

```
```
codehills-techinical/
├── pom.xml                    # Parent Maven POM
├── README.md
├── backend/                   # Backend server module
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/carmanagement/
│       │   ├── Application.java        # Main entry point
│       │   ├── model/                  # Domain models
│       │   │   ├── BaseModel.java      # Abstract base (id, createdAt)
│       │   │   ├── Car.java
│       │   │   └── FuelEntry.java
│       │   ├── dto/                    # Data Transfer Objects
│       │   │   ├── request/            # Request DTOs
│       │   │   └── response/           # Response DTOs
│       │   ├── repository/             # In-memory storage
│       │   │   ├── CarRepository.java
│       │   │   └── FuelRepository.java
│       │   ├── service/                # Business logic
│       │   │   ├── CarService.java
│       │   │   └── FuelService.java
│       │   ├── servlet/                # HTTP handlers
│       │   │   ├── BaseServlet.java
│       │   │   ├── CarsApiServlet.java
│       │   │   └── FuelStatsServlet.java  # Manual servlet
│       │   ├── exception/              # Custom exceptions
│       │   └── util/                   # Utilities
│       └── test/                       # Unit tests
└── cli/                       # CLI client module (separate executable)
    ├── pom.xml
    └── src/main/java/com/carmanagement/cli/
        ├── Main.java                   # CLI entry point
        ├── commands/                   # Command handlers
        │   ├── Command.java
        │   ├── CreateCarCommand.java
        │   ├── AddFuelCommand.java
        │   └── FuelStatsCommand.java
        ├── dto/                        # Client DTOs
        └── http/                       # HTTP client (java.net.http.HttpClient)
            └── ApiClient.java
```

## Quick Start

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/cars` | Create a new car | `{"brand": "...", "model": "...", "year": ...}` |
| GET | `/api/cars` | List all cars | - |
| POST | `/api/cars/{id}/fuel` | Add a fuel entry | `{"liters": ..., "price": ..., "odometer": ...}` |
| GET | `/api/cars/{id}/fuel/stats` | Get fuel statistics | - |
| GET | `/servlet/fuel-stats?carId={id}` | Manual servlet (same as above) | - |

### Example API Responses

**Create Car (POST /api/cars):**
```json
{
  "id": 1,
  "brand": "Toyota",
  "model": "Corolla",
  "year": 2018,
  "createdAt": "Dec 29, 2025 08:30 PM"
}
```

**List Cars (GET /api/cars):**
```json
[
  {
    "id": 1,
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2018,
    "createdAt": "Dec 29, 2025 08:30 PM"
  }
]
```

**Get Fuel Stats (GET /api/cars/1/fuel/stats):**
```json
{
  "totalFuel": 127.0,
  "totalCost": 157.0,
  "averageConsumption": 8.7,
  "entryCount": 3
}
```

## CLI Commands

### 1. Create a Car

```bash
create-car --brand <brand> --model <model> --year <year>
```

**Example:**
```bash
mvn compile exec:java -q -Dexec.args="create-car --brand Toyota --model Corolla --year 2018"
```

### 2. Add a Fuel Entry

```bash
add-fuel --carId <id> --liters <liters> --price <price> --odometer <km>
```

**Example:**
```bash
mvn compile exec:java -q -Dexec.args="add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000"
```

### 3. Get Fuel Statistics

```bash
fuel-stats --carId <id>
```

**Example:**
```bash
mvn compile exec:java -q -Dexec.args="fuel-stats --carId 1"
```

**Output:**
```
Total fuel: 120 L
Total cost: 155.00
Average consumption: 6.4 L/100km
```

## Running Tests

### Run All Tests

```bash
# From project root
mvn test
```

### Run Backend Tests Only

```bash
cd backend
mvn test
```

### Run a Specific Test Class

```bash
cd backend
mvn test -Dtest=CarServiceTest
mvn test -Dtest=FuelServiceTest
mvn test -Dtest=CarRepositoryTest
```

### Test Coverage

The tests cover:
- **CarServiceTest**: Car creation, validation, retrieval
- **FuelServiceTest**: Fuel entry creation, stats calculation, validation
- **CarRepositoryTest**: CRUD operations, thread safety

## Error Handling

### HTTP Status Codes

| Status Code | Description |
|-------------|-------------|
| 200 OK | Successful GET request |
| 201 Created | Successful POST request |
| 400 Bad Request | Validation error or malformed JSON |
| 404 Not Found | Resource not found |
| 500 Internal Server Error | Unexpected error |

### Error Response Format

```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Validation failed for 2 field(s)",
  "timestamp": "Dec 29, 2025 08:30 PM",
  "fieldErrors": {
    "brand": "Brand is required",
    "year": "Year must be 1900 or later"
  }
}
```

### CLI Error Examples

**Missing required parameter:**
```bash
$ mvn compile exec:java -q -Dexec.args="create-car --brand Toyota"
Error: --model is required

Usage: create-car --brand <brand> --model <model> --year <year>
```

**Car not found:**
```bash
$ mvn compile exec:java -q -Dexec.args="fuel-stats --carId 999"
API Error: Car with ID '999' not found
```

## Configuration

### Custom Server Port

```bash
# Via command line argument
cd backend
mvn compile exec:java -Dexec.args="9090"

# Via environment variable
PORT=9090 mvn compile exec:java
```

### Custom API URL (CLI)

```bash
API_URL=http://localhost:9090 mvn compile exec:java -q -Dexec.args="fuel-stats --carId 1"
```

## License

This project is created for the CodeHills Academy Technical Assignment.
