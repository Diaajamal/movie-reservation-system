package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.user.UserResponse;
import com.diaa.movie_reservation.entity.Role;
import com.diaa.movie_reservation.entity.User;
import com.diaa.movie_reservation.exception.user.UserException;
import com.diaa.movie_reservation.exception.user.UserNotFoundException;
import com.diaa.movie_reservation.mapper.UserMapper;
import com.diaa.movie_reservation.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserResponse getUserInfo(Authentication authentication) {
        log.info("Fetching user info for authentication: {}", authentication);
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Authentication is null or not authenticated");
            throw new AuthenticationException("User is not authenticated") {};
        }

        User user = (User) authentication.getPrincipal();


        return userMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.info("Fetching all users");
        Page<User> users = userRepository.findAll(pageable);
        if (users.isEmpty()) {
            log.warn("No users found");
        }
        log.info("Found {} users", users.getNumberOfElements());
        return users.map(userMapper::toDTO);
    }

    @Transactional
    public void promoteUserToAdmin(@NotNull Long userId) {
        log.info("Promoting user with ID {} to admin", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        if (user.getRoles().contains(Role.ADMIN)) {
            log.warn("User with ID {} is already an admin", userId);
            throw new UserException("User is already an admin");
        }
        user.getRoles().add(Role.ADMIN);
        userRepository.save(user);
        log.info("User with ID {} promoted to admin successfully", userId);
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        log.info("Fetching user with ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    public void save(User user) {
        log.info("Saving user: {}", user);
        if (user == null) {
            log.warn("User cannot be null");
            throw new IllegalArgumentException("User cannot be null");
        }
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.error("Error saving user: {}", e.getMessage());
            throw new UserException("User with this email already exists");
        }
        log.info("User saved successfully: {}", user);
    }

    public User findByEmail(@NotNull String email) {
        log.info("Finding user by email: {}", email);
        return userRepository.findByEmail(email).orElse(null);
    }

}
