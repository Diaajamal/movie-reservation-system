package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.reservation.ReservationResponse;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final ShowService showService;
    private final SeatService seatService;
    private final TicketMapper ticketMapper;
    private final DistributedLockService distributedLockService;


    @Transactional
    public TicketResponse bookTicket(TicketRequest request) {
        log.info("Booking ticket for request: {}", request);

        User user = userService.getUser(request.userId());
        Show show = showService.getShow(request.showId());
        Seat seat = seatService.getSeatForUpdate(request.seatId());
        verifySeatBelongsToTheater(seat, show);
        verifyShowDidNotStart(show);
        String lockKey = show.getId() + ":" + seat.getId();
        if (!distributedLockService.acquireLock(lockKey, 5, TimeUnit.SECONDS)) {
            throw new SeatAlreadyBookedException("Seat "
                    + seat.getRowLabel() + seat.getNumber() + " is being booked. Please try again");
        }
        verifySeatIsAvailable(show.getId(), seat.getId());
        Ticket ticket = ticketMapper.toEntity(request);
        ticket.setSeat(seat);
        ticket.setUser(user);
        ticket.setShow(show);
        ticket.setStatus(Status.BOOKED);

        try {
            Ticket savedTicket = ticketRepository.save(ticket);
            TicketResponse response = ticketMapper.toDTO(savedTicket);
            log.info("Ticket booked successfully: {}", response);
            return response;
        } catch (ObjectOptimisticLockingFailureException ex) {
            log.error("Optimistic lock error booking seat {}{} for show {}", seat.getRowLabel(), seat.getNumber(), show.getId());
            throw new SeatAlreadyBookedException("Seat "
                    + seat.getRowLabel() + seat.getNumber() + " is being booked", ex);
        } catch (DataIntegrityViolationException ex) {
            log.error("Seat {}{} for show {} already booked", seat.getRowLabel(), seat.getNumber(), show.getId());
            throw new SeatAlreadyBookedException("Seat "
                    + seat.getRowLabel() + seat.getNumber() + " is already booked for this show", ex);
        } finally {
            distributedLockService.releaseLock(lockKey);
        }
    }

    private void verifySeatBelongsToTheater(Seat seat, Show show) {
        if (!seat.getTheater().getId().equals(show.getTheater().getId()))
            throw new SeatNotFoundException("Seat doesn't exist in the given theater");
    }

    private void verifyShowDidNotStart(Show show) {
        if (show.getShowTime().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Cannot book a ticket for a show that has already started");
    }

    private void verifySeatIsAvailable(Long showId, Long seatId) {
        if (ticketRepository.existsByShowIdAndSeatIdAndStatus(showId, seatId, Status.BOOKED)) {
            throw new SeatAlreadyBookedException("Seat is Already Booked for this show");
        }
    }

    @Transactional
    public TicketResponse cancelTicket(Long ticketId, Authentication authentication) {
        log.info("Cancelling ticket with ID: {} that belongs to the user with email: {}", ticketId, authentication.getName());

        User user = (User) authentication.getPrincipal();
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        if (!isUserTicketOwner(ticket, user)) {
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

    private boolean isUserTicketOwner(Ticket ticket, User user) {
        return ticket.getUser().getId().equals(user.getId());
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getMyReservations(Authentication authentication) {
        log.info("Fetching reservations for user: {}", authentication.getName());

        User user = (User) authentication.getPrincipal();
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        var tickets = ticketRepository.findByUser_IdAndShow_ShowTimeAfterAndStatus(user.getId(), LocalDateTime.now(), Status.BOOKED);
        if (tickets.isEmpty()) {
            log.info("No reservations found for user: {}", user.getEmail());
        }

        var responses = tickets.stream()
                .map(ticketMapper::toDTO)
                .toList();

        log.info("Found {} reservations for user: {}", responses.size(), user.getEmail());
        return responses;
    }

    @Transactional(readOnly = true)
    public Page<ReservationResponse> getAllReservations(Long showId, Status status, Pageable pageable) {
        log.info("Fetching all reservations for show ID: {} with status: {}", showId, status);

        Page<ReservationResponse> reservations = ticketRepository.findReservationsByShowAndStatus(showId, status, pageable);
        if (reservations.isEmpty()) {
            log.info("No reservations found for show ID: {}", showId);
        } else {
            log.info("Found {} reservations for show ID: {}", reservations.getTotalElements(), showId);
        }

        return reservations;
    }
}
