package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.ticket.TicketRequest;
import com.diaa.movie_reservation.dto.ticket.TicketResponse;
import com.diaa.movie_reservation.entity.*;
import com.diaa.movie_reservation.exception.seat.SeatAlreadyBookedException;
import com.diaa.movie_reservation.exception.seat.SeatNotFoundException;
import com.diaa.movie_reservation.exception.ticket.TicketNotFoundException;
import com.diaa.movie_reservation.exception.user.UserNotFoundException;
import com.diaa.movie_reservation.mapper.TicketMapper;
import com.diaa.movie_reservation.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final ShowService showService;
    private final SeatService seatService;
    private final TicketMapper ticketMapper;

    @Transactional
    public TicketResponse bookTicket(TicketRequest request) {
        log.info("Booking ticket for request: {}", request);

        User user = userService.getUser(request.userId());
        Show show = showService.getShow(request.showId());
        Seat seat = seatService.getSeat(request.seatId());

        if (!seat.getTheater().getId().equals(show.getTheater().getId())) {
            throw new SeatNotFoundException("Seat doesn't exist in the given theater");
        }

        if (show.getShowTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot book a ticket for a show that has already started");
        }

        boolean taken = ticketRepository
                .existsByShowIdAndSeatIdAndStatus(show.getId(), seat.getId(), Status.BOOKED);
        if (taken) {
            throw new SeatAlreadyBookedException("Seat "
                    + seat.getRowLabel() + seat.getNumber() + " is already booked for this show");
        }

        // Map request to entity
        Ticket ticket = ticketMapper.toEntity(request);

        ticket.setSeat(seat);
        ticket.setUser(user);
        ticket.setShow(show);
        ticket.setStatus(Status.BOOKED);

        // Save ticket
        Ticket savedTicket = ticketRepository.save(ticket);

        // Return response
        TicketResponse response = ticketMapper.toDTO(savedTicket);
        log.info("Ticket booked successfully: {}", response);
        return response;
    }

    @Transactional
    public TicketResponse cancelTicket(Long ticketId, Authentication authentication) {
        log.info("Cancelling ticket with ID: {} that belongs to the user with email: {}", ticketId, authentication.getName());

        User user = userService.findByEmail(authentication.getName());
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("User does not own this ticket");
        }

        if (ticket.getStatus() != Status.BOOKED) {
            throw new TicketNotFoundException("Ticket cannot be cancelled as it is not booked");
        }

        ticket.setStatus(Status.CANCELLED);
        Ticket updatedTicket = ticketRepository.save(ticket);

        TicketResponse response = ticketMapper.toDTO(updatedTicket);
        log.info("Ticket cancelled successfully: {}", response);
        return response;
    }

    @Transactional(readOnly = true)
    public List<Long> findSeatsByShowIdAndStatus(Long showId, Status status) {
        log.info("Fetching taken seat IDs for show ID: {}", showId);

        // Validate show existence
        if (showId == null) {
            throw new NullPointerException("Show ID must not be null");
        }

        // Fetch taken seat IDs
        List<Long> takenSeatIds = ticketRepository.findSeatsByShowIdAndStatus(showId, status);
        log.info("Found {} taken seat IDs for show ID: {}", takenSeatIds.size(), showId);

        return takenSeatIds;
    }
}
