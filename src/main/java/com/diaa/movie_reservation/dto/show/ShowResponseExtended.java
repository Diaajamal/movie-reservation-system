package com.diaa.movie_reservation.dto.show;

import com.diaa.movie_reservation.dto.movie.MovieResponse;
import com.diaa.movie_reservation.dto.theater.TheaterResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShowResponseExtended(
        long id,
        MovieResponse movie,
        TheaterResponse theater,
        LocalDateTime showTime,
        BigDecimal price
        ) {
}
