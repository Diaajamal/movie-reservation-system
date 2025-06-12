package com.diaa.movie_reservation.dto.movie;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

public record MovieRequest(
        @NotBlank(message = "Title must not be blank")
        @Size(max = 200, message = "Title must be at most 200 characters")
        String title,

        @Size(max = 500, message = "Description must be at most 500 characters")
        String description,

        @NotBlank(message = "Director must not be blank")
        String director,

        @NotEmpty(message = "At least one genre must be provided")
        Set<@NotNull @Positive(message = "Genre ID must be positive") Short> genreIds,

        @Min(value = 1, message = "Duration must be at least 1 minute")
        int duration,

        @NotNull(message = "Release date is required")
        @PastOrPresent(message = "Release date cannot be in the future")
        LocalDate releaseDate,

        @NotBlank(message = "Poster URL must not be blank")
        @Size(max = 512, message = "Poster URL must be at most 512 characters")
        String posterUrl
) {}