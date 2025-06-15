package com.diaa.movie_reservation.controller;

import com.diaa.movie_reservation.dto.reservation.ReservationResponse;
import com.diaa.movie_reservation.entity.Status;
import com.diaa.movie_reservation.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Report", description = "Report API")
public class ReportController {
    private final TicketService ticketService;


    @Operation(summary = "Get all reservations (filterable)")
    @GetMapping("/reservations")
    public ResponseEntity<Page<ReservationResponse>> getAllReservations(@RequestParam @NotNull Long showID,
                                                                        @RequestParam(required = false) Status status,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        Page<ReservationResponse> reservations = ticketService.getAllReservations(showID, status, PageRequest.of(page, size));
        return ResponseEntity.ok(reservations);
    }
}
