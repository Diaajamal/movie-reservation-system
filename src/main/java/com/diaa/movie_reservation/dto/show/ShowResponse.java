package com.diaa.movie_reservation.dto.show;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShowResponse(
        long id,
        long movieID,
        short theaterID,
        LocalDateTime showTime,
        BigDecimal price,
        int availableSeats) {
}
