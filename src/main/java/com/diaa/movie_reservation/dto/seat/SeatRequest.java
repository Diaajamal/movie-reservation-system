package com.diaa.movie_reservation.dto.seat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SeatRequest(
        @NotNull(message = "Theater ID is required")
        Short theaterId,

        @NotBlank(message = "Row label must not be blank")
        String rowLabel,

        @NotNull(message = "Seat number is required")
        Short number
) {
}
