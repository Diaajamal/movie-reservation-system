package com.diaa.movie_reservation.exception.user;

public class InvalidCredentialsException extends UserException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
