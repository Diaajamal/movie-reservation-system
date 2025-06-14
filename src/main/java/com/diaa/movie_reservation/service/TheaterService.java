package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.theater.TheaterRequest;
import com.diaa.movie_reservation.dto.theater.TheaterResponse;
import com.diaa.movie_reservation.entity.Theater;
import com.diaa.movie_reservation.mapper.TheaterMapper;
import com.diaa.movie_reservation.repository.TheaterRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TheaterService {
    private final TheaterRepository theaterRepository;
    private final TheaterMapper theaterMapper;

    @Transactional
    public TheaterResponse addTheater( TheaterRequest request) {
        log.info("Adding new theater: {}", request);
        Theater theater = theaterMapper.toEntity(request);
        Theater savedTheater = theaterRepository.save(theater);
        log.info("Theater added successfully with ID: {}", savedTheater.getId());
        return theaterMapper.toDTO(savedTheater);
    }

    @Transactional(readOnly = true)
    public TheaterResponse getTheaterById(Short id) {
        log.info("Fetching theater with ID: {}", id);
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Theater with id " + id + " does not exist."));
        return theaterMapper.toDTO(theater);
    }

    @Transactional(readOnly = true)
    public Theater getTheater(Short id){
        return theaterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Theater with id " + id + " does not exist."));
    }

    @Transactional(readOnly = true)
    public Page<TheaterResponse> getAllTheaters(PageRequest of) {
        log.info("Fetching all theaters with pagination: page={}, size={}", of.getPageNumber(), of.getPageSize());
        Page<Theater> theaters = theaterRepository.findAll(of);
        if(theaters.isEmpty()) {
            log.warn("No theaters found");
        } else {
            log.info("Found {} theaters", theaters.getNumberOfElements());
        }
        return theaters.map(theaterMapper::toDTO);
    }

    @Transactional
    public TheaterResponse update(Short id, TheaterRequest request) {
        log.info("Updating theater with ID: {}", id);
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Theater with id " + id + " does not exist."));
        theater.setName(request.name());
        theater.setLocation(request.location());
        theater.setTotalSeats(request.totalSeats());
        Theater updatedTheater = theaterRepository.save(theater);
        log.info("Theater updated successfully with ID: {}", updatedTheater.getId());
        return theaterMapper.toDTO(updatedTheater);
    }
}
