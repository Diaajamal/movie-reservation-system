package com.diaa.movie_reservation.controller;

import com.diaa.movie_reservation.dto.user.UserResponse;
import com.diaa.movie_reservation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin API")
public class AdminController {
    private final UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello Admin";
    }

    @Operation(summary = "Get all users")
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<UserResponse> users = userService.getAllUsers(PageRequest.of(page, size));
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Promote a user to admin")
    @PostMapping("/promote")
    public ResponseEntity<String> promoteUserToAdmin(@RequestParam @NotNull Long userId) {
        userService.promoteUserToAdmin(userId);
        return ResponseEntity.ok("User promoted to admin successfully");
    }
}
