package com.diaa.movie_reservation.dto.genre;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serial;
import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public record GenreResponse(short id, String name) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

}
