# Movie Reservation System

A Spring Boot application for managing movie theaters, shows, seat reservations, and ticketing with JWT security and PostgreSQL/Redis backend.

## Table of Contents

* [Database Schema](#database-schema)
* [ER Diagram](#er-diagram)
* [Features](#features)
* [Tech Stack](#tech-stack)
* [Prerequisites](#prerequisites)
* [Installation](#installation)
* [Configuration](#configuration)
* [API Endpoints](#api-endpoints)
* [Usage](#usage)
* [License](#license)

## Database Schema

The database schema includes the following tables:

| Table          | Description                                        |
| -------------- | -------------------------------------------------- |
| `users`        | Registered users                                   |
| `user_roles`   | Roles assigned to users                            |
| `genres`       | Movie genres                                       |
| `movies`       | Movie details                                      |
| `movie_genres` | Many-to-many relation between movies and genres    |
| `theaters`     | Theater halls                                      |
| `seats`        | Seats in each theater                              |
| `shows`        | Screenings of movies in theaters at specific times |
| `tickets`      | Booked tickets for shows with seat assignments     |

## ER Diagram

```mermaid
erDiagram
    users {
        BIGINT id PK
        VARCHAR username
        VARCHAR password_hash
        VARCHAR email UK
        TIMESTAMP created_at
    }
    
    user_roles {
        BIGINT user_id PK,FK
        VARCHAR role PK
    }
    
    genres {
        SMALLINT id PK
        VARCHAR name UK
    }
    
    movies {
        BIGINT id PK
        VARCHAR title
        VARCHAR description
        VARCHAR director
        INTEGER duration
        DATE release_date
        VARCHAR poster_url
    }
    
    movie_genres {
        BIGINT movie_id PK,FK
        SMALLINT genre_id PK,FK
    }
    
    theaters {
        SMALLINT id PK
        VARCHAR name
        INTEGER total_seats
        VARCHAR location
    }
    
    seats {
        BIGINT id PK
        SMALLINT theater_id FK
        VARCHAR row_label
        SMALLINT number
    }
    
    shows {
        BIGINT id PK
        BIGINT movie_id FK
        SMALLINT theater_id FK
        TIMESTAMP show_time
        NUMERIC price
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }
    
    tickets {
        BIGINT id PK
        BIGINT user_id FK
        BIGINT show_id FK
        BIGINT seat_id FK
        VARCHAR status
        NUMERIC price_paid
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    %% Relationships
    users ||--o{ user_roles : has
    users ||--o{ tickets : books
    
    genres ||--o{ movie_genres : categorizes
    movies ||--o{ movie_genres : belongs_to
    movies ||--o{ shows : scheduled_in
    
    theaters ||--o{ seats : contains
    theaters ||--o{ shows : hosts
    
    shows ||--o{ tickets : generates
    seats ||--o{ tickets : reserved_for
```

## Features

* User registration & role-based authentication (JWT)
* CRUD operations for genres, movies, theaters, shows, seats
* Bulk seat booking with pessimistic & Redis distributed locks
* Ticket management with status tracking and unique booking constraints
* Reporting and analytics

## Tech Stack

* Java 17 & Spring Boot
* Spring Security & JWT
* PostgreSQL
* Redis
* Docker & Docker Compose
* Maven
* Lombok & MapStruct

## Prerequisites

* Java 17+
* Maven 3.6+
* PostgreSQL 12+
* Redis
* Docker & Docker Compose (optional)

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/<username>/movie-reservation-system.git
   cd movie-reservation-system
   ```
2. Configure database & redis in `src/main/resources/application.yml`.
3. Build the project:

   ```bash
   mvn clean install
   ```
4. Run the application:

   ```bash
   mvn spring-boot:run
   ```

## Configuration

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/moviereservation
    username: your_db_user
    password: your_db_password
  redis:
    host: localhost
    port: 6379
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```
## API Endpoints

### Authentication

- `POST /api/auth/register` – Register user
- `POST /api/auth/login` – Authenticate user

### Movie Management

- `POST /api/v1/movies/add` – Add a movie
- `GET /api/v1/movies/get/{id}` – Get movie by ID
- `GET /api/v1/movies/all` – List movies with pagination
- `PUT /api/v1/movies/update/{id}` – Update movie details
- `DELETE /api/v1/movies/delete/{id}` – Delete movie

### Show Management

- `POST /api/v1/shows/add` – Add show
- `GET /api/v1/shows/get/{id}` – Get show details
- `GET /api/v1/shows/all` – List shows with filters
- `GET /api/v1/shows/upcoming` – List upcoming shows
- `PUT /api/v1/shows/update/{id}` – Update show details
- `GET /api/v1/shows/{showId}/seats` – Get seat details for a show

### Reservations

- `POST /api/v1/reservations/reserve` – Book ticket
- `PUT /api/v1/reservations/cancel/{ticketId}` – Cancel ticket
- `GET /api/v1/reservations/me` – Get current user's reservations

### Reporting

- `GET /api/v1/reports/reservations` – List reservations with filters

## Usage

Use Postman or curl to test endpoints. Include JWT in the header:

```
Authorization: Bearer <token>
```

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

