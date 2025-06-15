-- =====================
-- Users
-- =====================
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255),
                       password_hash VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       created_at TIMESTAMP NOT NULL
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role VARCHAR(50) NOT NULL,
                            PRIMARY KEY (user_id, role),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =====================
-- Genres & Movies
-- =====================
CREATE TABLE genres (
                        id SMALLSERIAL PRIMARY KEY,
                        name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE movies (
                        id BIGSERIAL PRIMARY KEY,
                        title VARCHAR(200) NOT NULL,
                        description VARCHAR(500),
                        director VARCHAR(255) NOT NULL,
                        duration INTEGER NOT NULL,
                        release_date DATE NOT NULL,
                        poster_url VARCHAR(255)
);

CREATE TABLE movie_genres (
                              movie_id BIGINT NOT NULL,
                              genre_id SMALLINT NOT NULL,
                              PRIMARY KEY (movie_id, genre_id),
                              FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
                              FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

-- =====================
-- Theaters & Seats
-- =====================
CREATE TABLE theaters (
                          id SMALLSERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          total_seats INTEGER NOT NULL,
                          location VARCHAR(255) NOT NULL
);

CREATE TABLE seats (
                       id BIGSERIAL PRIMARY KEY,
                       theater_id SMALLINT NOT NULL,
                       row_label VARCHAR(10) NOT NULL,
                       number SMALLINT NOT NULL,
                       FOREIGN KEY (theater_id) REFERENCES theaters(id) ON DELETE CASCADE,
                       CONSTRAINT uq_seat_unique UNIQUE (theater_id, row_label, number),
                       CONSTRAINT chk_positive_seat_number CHECK (number > 0)
);

-- =====================
-- Shows
-- =====================
CREATE TABLE shows (
                       id BIGSERIAL PRIMARY KEY,
                       movie_id BIGINT NOT NULL,
                       theater_id SMALLINT NOT NULL,
                       show_time TIMESTAMP NOT NULL,
                       price NUMERIC(10,2) NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP,
                       FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
                       FOREIGN KEY (theater_id) REFERENCES theaters(id) ON DELETE CASCADE,
                       CONSTRAINT uq_show_unique UNIQUE (movie_id, theater_id, show_time),
                       CONSTRAINT chk_positive_price CHECK (price > 0)
);

-- =====================
-- Tickets
-- =====================
CREATE TABLE tickets (
                         id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         show_id BIGINT NOT NULL,
                         seat_id BIGINT NOT NULL,
                         status VARCHAR(20) NOT NULL,
                         price_paid NUMERIC(10,2) NOT NULL,
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                         FOREIGN KEY (show_id) REFERENCES shows(id) ON DELETE CASCADE,
                         FOREIGN KEY (seat_id) REFERENCES seats(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX unique_booked_ticket
    ON tickets (show_id, seat_id)
    WHERE status = 'BOOKED';
