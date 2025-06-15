package com.diaa.movie_reservation.exception.ticket;

public class TicketNotFoundException extends TicketException {
    public TicketNotFoundException(String message) {
        super(message);
    }

    public TicketNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
