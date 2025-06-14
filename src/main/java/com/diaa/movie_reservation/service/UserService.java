package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.user.UserResponse;
import com.diaa.movie_reservation.entity.Role;
import com.diaa.movie_reservation.entity.User;
import com.diaa.movie_reservation.exception.user.UserNotFoundException;
import com.diaa.movie_reservation.mapper.UserMapper;
import com.diaa.movie_reservation.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User findByEmail(@NotNull String email) {
        log.info("Finding user by email: {}", email);
        return userRepository.findByEmail(email).orElse(null);
    }

    public void save(User user) {
        log.info("Saving user: {}", user);
        if (user == null) {
            log.warn("User cannot be null");
            throw new IllegalArgumentException("User cannot be null");
        }
        userRepository.save(user);
        log.info("User saved successfully: {}", user);
    }

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
}
