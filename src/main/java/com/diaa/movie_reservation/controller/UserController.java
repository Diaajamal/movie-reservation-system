package com.diaa.movie_reservation.controller;

import com.diaa.movie_reservation.entity.Role;
import com.diaa.movie_reservation.entity.User;
import com.diaa.movie_reservation.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {
    private final UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello User";
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> userInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        String username = user.getDisplayName();
        String roles = user.getRoles().stream()
                .map(Role::getName)
                .reduce((first, second) -> first + ", " + second)
                .orElse("No roles assigned");

        return ResponseEntity.ok("Username: " + username + ", Email: " + email + ", Roles: " + roles);
    }
}
