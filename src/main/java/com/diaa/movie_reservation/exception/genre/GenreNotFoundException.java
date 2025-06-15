package com.diaa.movie_reservation.exception.genre;

public class GenreNotFoundException extends GenreException {
    public GenreNotFoundException(String message) {
        super(message);
    }
    public GenreNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
