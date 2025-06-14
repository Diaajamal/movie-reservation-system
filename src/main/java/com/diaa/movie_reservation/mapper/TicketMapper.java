package com.diaa.movie_reservation.mapper;

import com.diaa.movie_reservation.dto.ticket.TicketRequest;
import com.diaa.movie_reservation.dto.ticket.TicketResponse;
import com.diaa.movie_reservation.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {ShowMapper.class, SeatMapper.class})
public interface TicketMapper {
    Ticket toEntity(TicketRequest ticketRequest);

    TicketResponse toDTO(Ticket ticket);
}
