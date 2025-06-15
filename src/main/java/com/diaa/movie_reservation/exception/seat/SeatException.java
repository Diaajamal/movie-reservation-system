package com.diaa.movie_reservation.exception.seat;

public class SeatException extends RuntimeException {
    public SeatException(String message) {
        super(message);
    }
    public SeatException(String message, Throwable cause) {
        super(message, cause);
    }
}
