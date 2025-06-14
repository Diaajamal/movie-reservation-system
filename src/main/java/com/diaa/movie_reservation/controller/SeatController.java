package com.diaa.movie_reservation.controller;

import com.diaa.movie_reservation.dto.seat.SeatRequest;
import com.diaa.movie_reservation.dto.seat.SeatResponse;
import com.diaa.movie_reservation.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/seats")
@RequiredArgsConstructor
@Tag(name = "Seat", description = "Seat API")
public class SeatController {
    private final SeatService seatService;

    // Add methods for seat operations here, e.g., getSeatsByTheater, reserveSeat, etc.

    @Operation(summary = "Add a new seat")
    @PostMapping("/add")
    public ResponseEntity<SeatResponse> add(@RequestBody @Valid SeatRequest request) {
        SeatResponse response = seatService.addSeat(request);
        return ResponseEntity.ok(response);
    }

    //get all seats by theater id
    @GetMapping("/theater/{theaterId}")
    @Operation(summary = "Get all seats by theater ID")
    public ResponseEntity<Page<SeatResponse>> getSeatsByTheater(@PathVariable Short theaterId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        Page<SeatResponse> response = seatService.getSeatsByTheater(theaterId, PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

    //get available seats by show id
    @GetMapping("/available/{showId}")
    @Operation(summary = "Get available seats by show ID")
    public ResponseEntity<Page<SeatResponse>> getAvailableSeatsByShow(@PathVariable Long showId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        Page<SeatResponse> response = seatService.getAvailableSeatsByShow(showId, PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

}
