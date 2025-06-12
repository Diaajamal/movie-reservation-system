package com.diaa.movie_reservation.dto.user;

import com.diaa.movie_reservation.entity.Role;

import java.util.Set;

public record UserResponse(Long id, String username, String email, Set<Role> roles) {
}
