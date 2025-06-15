package com.diaa.movie_reservation.exception.genre;

public class GenreException extends RuntimeException {
    public GenreException(String message) {
        super(message);
    }

    public GenreException(String message, Throwable cause) {
        super(message, cause);
    }
}
