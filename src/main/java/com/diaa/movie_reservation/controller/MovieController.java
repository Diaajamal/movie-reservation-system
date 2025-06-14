package com.diaa.movie_reservation.controller;

import com.diaa.movie_reservation.dto.movie.MovieRequest;
import com.diaa.movie_reservation.dto.movie.MovieResponse;
import com.diaa.movie_reservation.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/movies")
@RequiredArgsConstructor
@Tag(name = "Movie", description = "Movie API")
public class MovieController {
    private final MovieService movieService;

    @Operation(summary = "Add a new movie")
    @PreAuthorize("hasAuthority('ADMIN')")  // Only ADMIN can add movies
    @PostMapping("/add")
    public ResponseEntity<MovieResponse> add(@RequestBody @Valid MovieRequest movieRequest) {
        MovieResponse response = movieService.addMovie(movieRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a movie by id")
    @GetMapping("get/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
        MovieResponse movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<MovieResponse>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MovieResponse> movies = movieService.getAllMovies(PageRequest.of(page, size));
        return ResponseEntity.ok(movies);
    }
}
