package com.diaa.movie_reservation.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    AVAILABLE("AVAILABLE"),
    BOOKED("BOOKED"),
    CANCELLED("CANCELLED");

    private final String name;
}
