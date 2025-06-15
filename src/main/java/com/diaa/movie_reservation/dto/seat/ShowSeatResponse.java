package com.diaa.movie_reservation.dto.seat;

import com.diaa.movie_reservation.entity.Status;

public record ShowSeatResponse(Long seatId, String rowLabel, short number, Status status) {
}
