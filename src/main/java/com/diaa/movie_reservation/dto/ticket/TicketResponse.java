package com.diaa.movie_reservation.dto.ticket;

import com.diaa.movie_reservation.dto.seat.SeatResponse;
import com.diaa.movie_reservation.dto.show.ShowResponseExtended;

import java.math.BigDecimal;
import java.time.Instant;

public record TicketResponse(
        long id,
        ShowResponseExtended show,
        SeatResponse seat,
        String status,
        BigDecimal pricePaid,
        Instant createdAt,
        Instant updatedAt) {
}
