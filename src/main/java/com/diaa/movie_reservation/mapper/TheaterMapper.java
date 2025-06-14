package com.diaa.movie_reservation.mapper;

import com.diaa.movie_reservation.dto.theater.TheaterRequest;
import com.diaa.movie_reservation.dto.theater.TheaterResponse;
import com.diaa.movie_reservation.entity.Theater;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TheaterMapper {
    @Mapping(source = "totalSeats", target = "totalSeats")
    Theater toEntity(TheaterRequest theaterRequest);

    TheaterResponse toDTO(Theater theater);
}
