package com.diaa.movie_reservation.controller;

import com.diaa.movie_reservation.dto.genre.GenreListResponse;
import com.diaa.movie_reservation.dto.genre.GenreRequest;
import com.diaa.movie_reservation.dto.genre.GenreResponse;
import com.diaa.movie_reservation.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/genres")
@RequiredArgsConstructor
@Tag(name = "Genre", description = "Genre API")
public class GenreController {
    private final GenreService genreService;


    @Operation(summary = "Add a new genre")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenreResponse> addGenre(@Valid @RequestBody GenreRequest request) {
        GenreResponse response = genreService.addGenre(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update a genre by id")
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenreResponse> updateGenre(@PathVariable("id") short id, @Valid @RequestBody GenreRequest request) {
        GenreResponse response = genreService.updateGenre(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a genre by id")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteGenre(@PathVariable("id") short id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Get all genres")
    @GetMapping("/all")
    @PreAuthorize( "hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<GenreListResponse> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }
}
