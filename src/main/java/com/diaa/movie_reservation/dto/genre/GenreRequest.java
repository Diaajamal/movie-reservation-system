package com.diaa.movie_reservation.dto.genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreRequest(
        @NotBlank(message = "Genre name must not be blank")
        @Size(max = 50, message = "Genre name must be at most 50 characters")
        String name
) {
}
