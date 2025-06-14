package com.diaa.movie_reservation.dto.show;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShowRequest(
        @NotNull(message = "Movie ID is required")
        Long movieId,

        @NotNull(message = "Theater ID is required")
        Short theaterId,

        @NotNull(message = "Show time is required")
        @FutureOrPresent(message = "Show time must be in the future or present")
        LocalDateTime showTime,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0",inclusive = false, message = "Price must be at least 0")
        BigDecimal price) {
}
