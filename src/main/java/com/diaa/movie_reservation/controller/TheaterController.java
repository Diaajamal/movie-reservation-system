package com.diaa.movie_reservation.controller;

import com.diaa.movie_reservation.dto.theater.TheaterRequest;
import com.diaa.movie_reservation.dto.theater.TheaterResponse;
import com.diaa.movie_reservation.service.TheaterService;
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
@RequestMapping("api/v1/theaters")
@RequiredArgsConstructor
@Tag(name = "Theater", description = "Theater API")
public class TheaterController {
    private final TheaterService theaterService;

    @Operation(summary = "Add a new theater")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TheaterResponse> add(@RequestBody @Valid TheaterRequest request){
        TheaterResponse response = theaterService.addTheater(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a theater by id")
    @GetMapping("/get/{id}")
    @PreAuthorize( "hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<TheaterResponse> getTheaterById(@PathVariable Short id) {
        TheaterResponse theater = theaterService.getTheaterById(id);
        return ResponseEntity.ok(theater);
    }

    @Operation(summary = "Get all theaters")
    @GetMapping("/all")
    @PreAuthorize( "hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Page<TheaterResponse>> getAllTheaters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TheaterResponse> theaters = theaterService.getAllTheaters(PageRequest.of(page, size));
        return ResponseEntity.ok(theaters);
    }


    @Operation(summary = "Update a theater by id")
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TheaterResponse> updateTheater(@PathVariable Short id, @RequestBody @Valid TheaterRequest request) {
        TheaterResponse response = theaterService.update(id,request);
        return ResponseEntity.ok(response);
    }
}
