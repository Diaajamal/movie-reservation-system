package com.diaa.movie_reservation.controller;

import com.diaa.movie_reservation.dto.ticket.TicketRequest;
import com.diaa.movie_reservation.dto.ticket.TicketResponse;
import com.diaa.movie_reservation.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket", description = "Ticket API")
public class TicketController {
    private final TicketService ticketService;

    @Tag(name = "Ticket", description = "Book a ticket")
    @PostMapping("/book")
    public ResponseEntity<TicketResponse> bookTicket(@RequestBody @Valid TicketRequest request) {
        TicketResponse response = ticketService.bookTicket(request);
        return ResponseEntity.ok(response);
    }

    @Tag(name = "Ticket", description = "Get ticket by ID")
    @PutMapping("/cancel/{ticketId}")
    public ResponseEntity<TicketResponse> cancelTicket(@PathVariable Long ticketId) {
        TicketResponse response = ticketService.cancelTicket(ticketId);
        return ResponseEntity.ok(response);
    }
}
