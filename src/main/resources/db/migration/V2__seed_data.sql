-- =====================
-- Sample Data Seeding
-- =====================

-- Seed genres
INSERT INTO genres (name)
VALUES ('Action'),
       ('Comedy'),
       ('Drama'),
       ('Horror'),
       ('Sci-Fi');

-- Seed theaters
INSERT INTO theaters (name, total_seats, location)
VALUES ('Downtown Cinema', 120, 'Downtown'),
       ('Cityplex', 200, 'Uptown');

-- Seed a movie
INSERT INTO movies (title, description, director, duration, release_date, poster_url)
VALUES ('The Matrix', 'A hacker discovers reality is a simulation.', 'Lana Wachowski', 136, '1999-03-31',
        'https://example.com/matrix.jpg'),
       ('Inception', 'A thief enters dreams to steal secrets.', 'Christopher Nolan', 148, '2010-07-16',
        'https://example.com/inception.jpg');

-- Link movies to genres
INSERT INTO movie_genres (movie_id, genre_id)
SELECT m.id, g.id
FROM movies m,
     genres g
WHERE m.title = 'The Matrix'
  AND g.name = 'Sci-Fi';

INSERT INTO movie_genres (movie_id, genre_id)
SELECT m.id, g.id
FROM movies m,
     genres g
WHERE m.title = 'Inception'
  AND g.name IN ('Sci-Fi', 'Action');

-- Seed seats
INSERT INTO seats (theater_id, row_label, number)
SELECT t.id, 'A', gs
FROM generate_series(1, 10) AS gs,
     theaters t
WHERE t.name = 'Downtown Cinema';

-- Seed a show
INSERT INTO shows (movie_id, theater_id, show_time, price, created_at)
SELECT m.id, t.id, NOW() + INTERVAL '1 day', 150.00, NOW()
FROM movies m,
     theaters t
WHERE m.title = 'Inception'
  AND t.name = 'Downtown Cinema';

-- Seed a user
INSERT INTO users (username, password_hash, email, created_at)
VALUES ('admin', '$2a$10$iE8IkUwMsePmGPqRej7.Bex/.unUieSaK74RFu/KNpnSrz2MrALGe', 'admin@example.com', NOW());

-- Assign ADMIN role
INSERT INTO user_roles (user_id, role)
SELECT id, 'ADMIN'
FROM users
WHERE email = 'admin@example.com';
