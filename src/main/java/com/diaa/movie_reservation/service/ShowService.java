package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.show.ShowRequest;
import com.diaa.movie_reservation.dto.show.ShowResponse;
import com.diaa.movie_reservation.dto.show.ShowResponseExtended;
import com.diaa.movie_reservation.entity.Movie;
import com.diaa.movie_reservation.entity.Show;
import com.diaa.movie_reservation.entity.Theater;
import com.diaa.movie_reservation.exception.show.ShowNotFoundException;
import com.diaa.movie_reservation.mapper.ShowMapper;
import com.diaa.movie_reservation.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieService movieService;
    private final TheaterService theaterService;
    private final ShowMapper showMapper;

    @Transactional
    public ShowResponse addShow(ShowRequest request) {
        log.info("Adding new show: {}", request);
        Movie movie = movieService.getMovie(request.movieId());
        Theater theater = theaterService.getTheater(request.theaterId());

        Show show = showMapper.toEntity(request);
        show.setMovie(movie);
        show.setTheater(theater);

        Show savedShow = showRepository.save(show);
        log.info("Show added successfully with ID: {}", savedShow.getId());
        return showMapper.toDTO(savedShow);
    }

    @Transactional(readOnly = true)
    public ShowResponseExtended getShowById(Long id) {
        log.info("Fetching show with ID: {}", id);
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ShowNotFoundException("Show with id " + id + " does not exist."));
        return showMapper.toExtendedDTO(show);
    }

    @Transactional(readOnly = true)
    public Page<ShowResponse> getAllShows(Pageable pageable) {
        log.info("Fetching all shows with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Show> shows = showRepository.findAll(pageable);
        if (shows.isEmpty()) {
            log.warn("No shows found");
        } else {
            log.info("Found {} shows", shows.getNumberOfElements());
        }
        return shows.map(showMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<ShowResponse> getUpcomingShows(LocalDateTime showTimeAfter, Pageable pageable) {
        log.info("Fetching upcoming shows with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Show> shows = showRepository.findAllByShowTimeAfter(showTimeAfter,pageable);
        if (shows.isEmpty()) {
            log.warn("No upcoming shows found");
        } else {
            log.info("Found {} upcoming shows", shows.getNumberOfElements());
        }
        return shows.map(showMapper::toDTO);
    }

    @Transactional
    public  ShowResponse update(Long id, ShowRequest request) {
        log.info("Updating show with ID: {}", id);
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ShowNotFoundException("Show with id " + id + " does not exist."));

        Movie movie = movieService.getMovie(request.movieId());
        Theater theater = theaterService.getTheater(request.theaterId());

        show.setMovie(movie);
        show.setTheater(theater);
        show.setShowTime(request.showTime());
        show.setPrice(request.price());

        Show updatedShow = showRepository.save(show);
        log.info("Show updated successfully with ID: {}", updatedShow.getId());
        return showMapper.toDTO(updatedShow);
    }

    public Show getShow(Long id) {
        log.info("Fetching show with ID: {}", id);
        return showRepository.findById(id)
                .orElseThrow(() -> new ShowNotFoundException("Show with id " + id + " does not exist."));
    }

}
