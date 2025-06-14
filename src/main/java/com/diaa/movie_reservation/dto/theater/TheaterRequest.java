package com.diaa.movie_reservation.dto.theater;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TheaterRequest(
        @NotBlank(message = "Theater name must not be blank")
        @Size(max = 100, message = "Theater name must be at most 100 characters")
        String name,

        @Min(value = 1, message = "Total seats must be at least 1")
        Integer totalSeats,

        @NotBlank(message = "Theater location must not be blank")
        @Size(max = 255, message = "Theater location must be at most 255 characters")
        String location
) {
}
