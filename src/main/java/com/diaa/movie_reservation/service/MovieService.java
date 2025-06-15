package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.movie.MovieRequest;
import com.diaa.movie_reservation.dto.movie.MovieResponse;
import com.diaa.movie_reservation.entity.Genre;
import com.diaa.movie_reservation.entity.Movie;
import com.diaa.movie_reservation.exception.genre.GenreNotFoundException;
import com.diaa.movie_reservation.exception.movie.MovieNotFoundException;
import com.diaa.movie_reservation.mapper.MovieMapper;
import com.diaa.movie_reservation.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final GenreService genreService;

    @Transactional
    public MovieResponse addMovie(MovieRequest movieRequest) {
        if (!genreService.isGenresValid(movieRequest.genreIds())) {
            throw new GenreNotFoundException("Invalid genre IDs");
        }

        Movie movie = movieMapper.toEntity(movieRequest);

        Set<Genre> genres = movieRequest.genreIds().stream()
                .map(genreService::getReference)
                .collect(Collectors.toSet());
        movie.setGenres(genres);

        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDTO(savedMovie);
    }

    @Transactional(readOnly = true)
    public MovieResponse getMovieById(Long id) {
        log.info("Fetching movie with id: {}", id);
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + id));
        return movieMapper.toDTO(movie);
    }

    @Transactional(readOnly = true)
    public Page<MovieResponse> getAllMovies(Pageable pageable) {
        log.info("Fetching all movies");
        Page<Movie> movies = movieRepository.findAll(pageable);
        if (movies.isEmpty()) {
            log.warn("No movies found");
            return Page.empty();
        } else {
            log.info("Found {} movies", movies.getNumberOfElements());
            return movies.map(movieMapper::toDTO);
        }
    }

    public Movie getMovie(Long id){
        return movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + id));
    }
}
