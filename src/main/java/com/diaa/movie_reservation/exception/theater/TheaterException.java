package com.diaa.movie_reservation.exception.theater;

public class TheaterException extends RuntimeException {
    public TheaterException(String message) {
        super(message);
    }
    public TheaterException(String message, Throwable cause) {
        super(message, cause);
    }
}
