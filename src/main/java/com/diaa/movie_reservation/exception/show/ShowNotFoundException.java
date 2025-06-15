package com.diaa.movie_reservation.exception.show;

public class ShowNotFoundException extends ShowException {
    public ShowNotFoundException(String message) {
        super(message);
    }
    public ShowNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
