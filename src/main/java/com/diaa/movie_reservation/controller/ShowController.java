package com.diaa.movie_reservation.controller;

import com.diaa.movie_reservation.dto.show.ShowRequest;
import com.diaa.movie_reservation.dto.show.ShowResponse;
import com.diaa.movie_reservation.dto.show.ShowResponseExtended;
import com.diaa.movie_reservation.service.ShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("api/v1/shows")
@RequiredArgsConstructor
@Tag(name = "Show", description = "Show API")
public class ShowController {
    private final ShowService showService;

    @Operation(summary = "Add a new show")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ShowResponse> add(@RequestBody @Valid ShowRequest request) {
        ShowResponse response = showService.addShow(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a show by ID with extended details")
    @GetMapping("/get/{id}")
    public ResponseEntity<ShowResponseExtended> getShowById(@PathVariable Long id) {
        ShowResponseExtended show = showService.getShowById(id);
        return ResponseEntity.ok(show);
    }

    @Operation(summary = "Get all shows with pagination")
    @GetMapping("/all")
    public ResponseEntity<Page<ShowResponse>> getAllShows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ShowResponse> shows = showService.getAllShows(PageRequest.of(page, size));
        return ResponseEntity.ok(shows);
    }

    @Operation(summary = "Get upcoming shows with pagination")
    @GetMapping("/upcoming")
    public ResponseEntity<Page<ShowResponse>> getUpcomingShows(@RequestParam LocalDateTime showTimeAfter,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Page<ShowResponse> shows = showService.getUpcomingShows(showTimeAfter, PageRequest.of(page, size));
        return ResponseEntity.ok(shows);
    }

    @Operation(summary = "Update a show by ID")
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ShowResponse> updateShow(@PathVariable Long id, @RequestBody @Valid ShowRequest request) {
        ShowResponse response = showService.update(id, request);
        return ResponseEntity.ok(response);
    }
}
