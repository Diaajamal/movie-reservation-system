package com.diaa.movie_reservation.dto.theater;

public record TheaterResponse(
        short id,
        String name,
        String location,
        int totalSeats) {
}
