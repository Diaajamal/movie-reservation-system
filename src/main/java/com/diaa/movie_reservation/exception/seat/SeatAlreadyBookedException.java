package com.diaa.movie_reservation.exception.seat;

public class SeatAlreadyBookedException extends SeatException {
    public SeatAlreadyBookedException(String message) {
        super(message);
    }

    public SeatAlreadyBookedException(String message, Throwable cause) {
        super(message, cause);
    }
}
