package com.diaa.movie_reservation.mapper;

import com.diaa.movie_reservation.dto.seat.SeatRequest;
import com.diaa.movie_reservation.dto.seat.SeatResponse;
import com.diaa.movie_reservation.entity.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    Seat toEntity(SeatRequest seatRequest);

    SeatResponse toDTO(Seat seat);
}
