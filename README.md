# Virtual Power Plant Battery Management REST API

## Overview
This project is a scalable and robust REST API that manages registration and aggregation of distributed battery power sources. 
It is built using Spring Boot with a reactive programming model (Spring WebFlux & R2DBC), it employs Flyway for 
database migrations, and uses Testcontainers for integration testing. The project follows the Clean Architecture 
pattern to ensure maintainability and testability.


## Features

- **Battery Registration:** Register a list of batteries (name, postcode, watt capacity) via a REST endpoint.
- **Postcode Range Query:** Retrieve battery names (sorted alphabetically) along with total and average watt capacity for a specified postcode range.
- **Reactive & Non-Blocking:** Built using Spring WebFlux and R2DBC for high-concurrency, non-blocking request handling.
- **Clean Architecture:** Well-separated concerns for domain, application, infrastructure, and adapter layers.
- **Database Migrations:** Managed with Flyway (SQL scripts located under `src/main/resources/db/migration`).
- **Integration Testing:** Uses Testcontainers to spin up a PostgreSQL instance with dynamic property configuration.
  
## Architectural Decisions

- Made use of Clean Architecture(domain, application, adapter, infrastructure)
- Opted for Reactive Programming and Reactive Database for resource usage efficiency and future scalability considerations
- Added DDL migration support via FlyWay for maintainability
- Used Testcontainers for Integration Tests


## Setup Instructions

### Prerequisites
- **Java 11+**
- **Gradle**
- **Docker** (for Testcontainers)
- **PostgreSQL** (for local development)

### Build and Run
```shell

./gradlew bootRun
```

**Or, if you already have gradle installed**

```shell

gradle bootRun
```