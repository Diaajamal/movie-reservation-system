package com.diaa.movie_reservation.exception.seat;

public class SeatNotFoundException extends SeatException {
    public SeatNotFoundException(String message) {
        super(message);
    }
    public SeatNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
