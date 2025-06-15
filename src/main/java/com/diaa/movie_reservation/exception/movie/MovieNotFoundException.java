package com.diaa.movie_reservation.exception.movie;

public class MovieNotFoundException extends MovieException {
    public MovieNotFoundException(String message) {
        super(message);
    }
    public MovieNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
