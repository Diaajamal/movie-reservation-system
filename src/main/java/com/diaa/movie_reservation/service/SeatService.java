package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.seat.SeatRequest;
import com.diaa.movie_reservation.dto.seat.SeatResponse;
import com.diaa.movie_reservation.entity.Seat;
import com.diaa.movie_reservation.entity.Show;
import com.diaa.movie_reservation.entity.Status;
import com.diaa.movie_reservation.entity.Theater;
import com.diaa.movie_reservation.mapper.SeatMapper;
import com.diaa.movie_reservation.repository.SeatRepository;
import com.diaa.movie_reservation.repository.TicketRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final TheaterService theaterService;
    private final ShowService showService;
    private final TicketRepository ticketRepository;
    private final SeatMapper seatMapper;


    @Transactional
    public SeatResponse addSeat(SeatRequest request) {
        log.info("Adding new seat: {}", request);
        if(isSeatExists(request.theaterId(), request.rowLabel(), request.number())) {
            log.error("Seat already exists in theater ID: {}, row: {}, number: {}", request.theaterId(), request.rowLabel(), request.number());
            throw new EntityExistsException("Seat already exists in the specified theater.");
        }
        Seat seat = seatMapper.toEntity(request);
        Theater theater = theaterService.getTheater(request.theaterId());
        seat.setTheater(theater);
        Seat savedSeat = seatRepository.save(seat);
        return seatMapper.toDTO(savedSeat);
    }

    @Transactional(readOnly = true)
    public Page<SeatResponse> getSeatsByTheater(Short theaterId, Pageable pageable) {
        log.info("Fetching seats for theater ID: {}", theaterId);
        theaterService.getTheater(theaterId);
        Page<Seat> seats = seatRepository.findAllByTheater_Id(theaterId, pageable);
        log.info("Found {} seats for theater ID: {}", seats.getTotalElements(), theaterId);
        return seats.map(seatMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Seat getSeat(Long id) {
        log.info("Fetching seat with ID: {}", id);
        return seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found with ID: " + id));
    }

    public Page<SeatResponse> getAvailableSeatsByShow(Long showId,Pageable pageable) {
        log.info("Fetching available seats for show ID: {}", showId);
        // Validate show existence
        Show show = showService.getShow(showId);

        List<Long> taken = ticketRepository.findSeatsByShowIdAndStatus(showId, Status.BOOKED);
        log.info("Found {} taken seat IDs for show ID: {}", taken.size(), showId);

        Page<Seat> freeSeats = seatRepository.findByTheater_IdAndIdNotIn(show.getTheater().getId(), taken.isEmpty() ? List.of(-1L) : taken,pageable);

        log.info("Found {} available seats for show ID: {}", freeSeats.getTotalElements(), showId);
        return freeSeats.map(seatMapper::toDTO);
    }

    private boolean isSeatExists(Short theaterId, String rowLabel, short number) {
        log.info("Checking if seat already exists in theater ID: {}, row: {}, number: {}", theaterId, rowLabel, number);
        return seatRepository.existsByTheater_IdAndRowLabelAndNumber(theaterId, rowLabel, number);
    }
}
