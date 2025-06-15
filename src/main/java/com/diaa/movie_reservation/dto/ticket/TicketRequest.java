package com.diaa.movie_reservation.dto.ticket;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TicketRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Show ID is required")
        Long showId,

        @NotNull(message = "Seat ID is required")
        Long seatId,

        @NotNull(message = "Price paid is required")
        BigDecimal pricePaid
) {
}
