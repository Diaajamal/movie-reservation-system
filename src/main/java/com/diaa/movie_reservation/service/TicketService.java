package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.ticket.TicketRequest;
import com.diaa.movie_reservation.dto.ticket.TicketResponse;
import com.diaa.movie_reservation.entity.*;
import com.diaa.movie_reservation.mapper.TicketMapper;
import com.diaa.movie_reservation.repository.TicketRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // Validate user and show
        User user = userService.getUser(request.userID());
        Show show = showService.getShow(request.showId());
        Seat seat = seatService.getSeat(request.seatId());

        // 2) Verify the seat is in this show’s theater
        if (!seat.getTheater().getId().equals(show.getTheater().getId())) {
            throw new EntityNotFoundException("Seat doesn't exist in the given theater");
        }

        boolean taken = ticketRepository
                .existsByShowIdAndSeatIdAndStatus(show.getId(), seat.getId(), Status.BOOKED);
        if (taken) {
            throw new EntityExistsException("Seat "
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
    public TicketResponse cancelTicket(Long ticketId) {
        log.info("Cancelling ticket with ID: {}", ticketId);

        // Fetch the ticket
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        // Check if the ticket is already canceled or not booked
        if (ticket.getStatus() != Status.BOOKED) {
            throw new IllegalStateException("Ticket cannot be cancelled as it is not booked");
        }

        // Update status to CANCELLED
        ticket.setStatus(Status.CANCELLED);
        Ticket updatedTicket = ticketRepository.save(ticket);

        // Map to response DTO
        TicketResponse response = ticketMapper.toDTO(updatedTicket);
        log.info("Ticket cancelled successfully: {}", response);
        return response;
    }

    @Transactional(readOnly = true)
    public List<Long> findSeatsByShowIdAndStatus(Long showId,Status status) {
        log.info("Fetching taken seat IDs for show ID: {}", showId);

        // Validate show existence
        if (showId == null) {
            throw new IllegalArgumentException("Show ID must not be null");
        }

        // Fetch taken seat IDs
        List<Long> takenSeatIds = ticketRepository.findSeatsByShowIdAndStatus(showId, status);
        log.info("Found {} taken seat IDs for show ID: {}", takenSeatIds.size(), showId);

        return takenSeatIds;
    }
}
