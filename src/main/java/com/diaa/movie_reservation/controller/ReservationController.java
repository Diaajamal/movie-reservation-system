package com.diaa.movie_reservation.controller;

import com.diaa.movie_reservation.dto.ticket.TicketRequest;
import com.diaa.movie_reservation.dto.ticket.TicketResponse;
import com.diaa.movie_reservation.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation", description = "Reservation API")
public class ReservationController {
    private final TicketService ticketService;

    @Operation(summary = "Cancel a reservation by ticket ID")
    @PutMapping("/cancel/{ticketId}")
    public ResponseEntity<TicketResponse> cancelTicket(@PathVariable Long ticketId, Authentication authentication) {
        TicketResponse response = ticketService.cancelTicket(ticketId,authentication);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get current user's reservations")
    @GetMapping("/me")
    public ResponseEntity<List<TicketResponse>> getMyReservations(Authentication authentication) {
        return ResponseEntity.ok(ticketService.getMyReservations(authentication));
    }

    @Operation(summary = "Make a reservation")
    @PostMapping("/reserve")
    public ResponseEntity<TicketResponse> bookTicket(@RequestBody @Valid TicketRequest request) {
        TicketResponse response = ticketService.bookTicket(request);
        return ResponseEntity.ok(response);
    }

}
