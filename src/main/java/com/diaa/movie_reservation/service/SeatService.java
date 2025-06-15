package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.seat.SeatRequest;
import com.diaa.movie_reservation.dto.seat.SeatResponse;
import com.diaa.movie_reservation.entity.Seat;
import com.diaa.movie_reservation.entity.Theater;
import com.diaa.movie_reservation.exception.seat.SeatExistsException;
import com.diaa.movie_reservation.exception.seat.SeatNotFoundException;
import com.diaa.movie_reservation.mapper.SeatMapper;
import com.diaa.movie_reservation.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final TheaterService theaterService;
    private final ShowService showService;
    private final SeatMapper seatMapper;


    @Transactional
    public SeatResponse addSeat(SeatRequest request) {
        log.info("Adding new seat: {}", request);
        if(isSeatExists(request.theaterId(), request.rowLabel(), request.number())) {
            log.error("Seat already exists in theater ID: {}, row: {}, number: {}", request.theaterId(), request.rowLabel(), request.number());
            throw new SeatExistsException("Seat already exists in the specified theater.");
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
    public Page<SeatResponse> findAvailableSeatsByShow(Long showId, Pageable pageable) {
       showService.getShow(showId);

        Page<Seat> availableSeats = seatRepository.findAvailableSeats(showId, pageable);

        if (availableSeats.isEmpty()) {
            log.warn("No available seats found for show ID {}", showId);
            throw new SeatNotFoundException("No available seats found for show ID " + showId);
        }

        return availableSeats.map(seatMapper::toDTO);
    }

    public Seat getSeatForUpdate(Long id) {
        return seatRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new SeatNotFoundException("Seat not found with id: " + id));
    }

    private boolean isSeatExists(Short theaterId, String rowLabel, short number) {
        log.info("Checking if seat already exists in theater ID: {}, row: {}, number: {}", theaterId, rowLabel, number);
        return seatRepository.existsByTheater_IdAndRowLabelAndNumber(theaterId, rowLabel, number);
    }
}
