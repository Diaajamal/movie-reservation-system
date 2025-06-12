package com.diaa.movie_reservation.dto.movie;

import com.diaa.movie_reservation.dto.genre.GenreResponse;

import java.time.LocalDate;
import java.util.Set;

public record MovieResponse(
        Long id,
        String title,
        String description,
        String director,
        LocalDate releaseDate,
        int duration,
        String posterUrl,
        Set<GenreResponse> genres) {
}
