package com.diaa.movie_reservation.dto.reservation;

import com.diaa.movie_reservation.entity.Status;

import java.math.BigDecimal;
import java.time.Instant;

public record ReservationResponse(
        Long id,
        String movieTitle,
        String seatName,
        Status status,
        BigDecimal pricePaid,
        Instant createdAt,
        Instant updatedAt
) {

}