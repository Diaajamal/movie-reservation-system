package com.diaa.movie_reservation.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    BOOKED("BOOKED"),
    CANCELLED("CANCELLED");

    private final String name;
}
