package com.diaa.movie_reservation.exception.show;

public class ShowException extends RuntimeException {
    public ShowException(String message) {
        super(message);
    }
    public ShowException(String message, Throwable cause) {
        super(message, cause);
    }
}
