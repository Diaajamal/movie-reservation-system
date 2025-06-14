package com.diaa.movie_reservation.dto.seat;

public record SeatResponse(
        long id,
        String rowLabel,
        Short number
) {
}
