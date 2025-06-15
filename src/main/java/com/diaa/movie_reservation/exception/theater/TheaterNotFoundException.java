package com.diaa.movie_reservation.exception.theater;

public class TheaterNotFoundException extends TheaterException {
    public TheaterNotFoundException(String message) {
        super(message);
    }
    public TheaterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
