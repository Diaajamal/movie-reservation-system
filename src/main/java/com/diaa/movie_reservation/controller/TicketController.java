package com.diaa.movie_reservation.controller;

import com.diaa.movie_reservation.dto.ticket.TicketRequest;
import com.diaa.movie_reservation.dto.ticket.TicketResponse;
import com.diaa.movie_reservation.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket", description = "Ticket API")
public class TicketController {
    private final TicketService ticketService;

    @Operation(summary = "Get ticket by ID")
    @PostMapping("/book")
    @PreAuthorize( "hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<TicketResponse> bookTicket(@RequestBody @Valid TicketRequest request) {
        TicketResponse response = ticketService.bookTicket(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancel ticket by ID")
    @PutMapping("/cancel/{ticketId}")
    @PreAuthorize( "hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<TicketResponse> cancelTicket(@PathVariable Long ticketId, Authentication authentication) {
        TicketResponse response = ticketService.cancelTicket(ticketId,authentication);
        return ResponseEntity.ok(response);
    }
}
