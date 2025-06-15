A Spring Boot REST API for managing movies, theaters, showtimes and seat reservations. It uses PostgreSQL for persistence, Flyway for database migrations and JWT based authentication.

## Features
- Manage movies, genres, theaters and shows
- Reserve seats and purchase tickets
- Role based access control with `USER` and `ADMIN` roles
- Swagger/OpenAPI documentation available at `/swagger-ui.html`
- Redis caching support

## Build & Run

This project requires **Java 21** and **Maven**. To build the project run:

```bash
mvn package
```

The application expects a running PostgreSQL instance. Default configuration can be found in [`application.yaml`](src/main/resources/application.yaml).

Flyway migrations will automatically create the schema and seed sample data on first run.

To start the application locally:

```bash
mvn spring-boot:run
```

Then open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to explore the API.

## Database Schema

The database schema is defined using Flyway migrations found in [`src/main/resources/db/migration`](src/main/resources/db/migration). Below is an ER diagram generated from the schema.

![Database Diagram](docs/db-schema.png)