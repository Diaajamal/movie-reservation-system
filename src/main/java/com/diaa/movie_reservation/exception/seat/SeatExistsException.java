package com.diaa.movie_reservation.exception.seat;

public class SeatExistsException extends SeatException {
    public SeatExistsException(String message) {
        super(message);
    }

    public SeatExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
