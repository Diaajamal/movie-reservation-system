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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {
    private final UserService userService;

    @GetMapping("/user-info")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<UserResponse> userInfo(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserInfo(authentication));
    }

    @Operation(summary = "Get all users")
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<UserResponse> users = userService.getAllUsers(PageRequest.of(page, size));
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Promote a user to admin")
    @PostMapping("/{id}/promote")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> promoteUserToAdmin(@PathVariable @NotNull Long id) {
        userService.promoteUserToAdmin(id);
        return ResponseEntity.ok("User promoted to admin successfully");
    }
}
