package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.auth.AuthResponse;
import com.diaa.movie_reservation.dto.auth.LoginRequest;
import com.diaa.movie_reservation.dto.auth.RegisterRequest;
import com.diaa.movie_reservation.entity.Role;
import com.diaa.movie_reservation.entity.User;
import com.diaa.movie_reservation.exception.user.InvalidCredentialsException;
import com.diaa.movie_reservation.exception.user.UserAlreadyExistsException;
import com.diaa.movie_reservation.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    public void register(RegisterRequest request) {
        log.info("Processing registration request for user: {}", request.email());

        if (userExists(request.email())) {
            log.warn("User with email {} already exists", request.email());
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.USER));
        userService.save(user);
        log.info("Successfully registered user: {}", request.email());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Processing login request for user: {}", request.email());
        validateCredentials(request.email(), request.password());

        User user = userService.findByEmail(request.email());
        String token = jwtService.generateToken(user);

        log.info("Successfully authenticated user: {}", request.email());
        return new AuthResponse(token);
    }

    private void validateCredentials(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (Exception e) {
            log.warn("Invalid credentials attempt for user: {}", email);
            throw new InvalidCredentialsException("Invalid email or password", e);
        }
    }

    private boolean userExists(String email) {
        return userService.findByEmail(email) != null;
    }
}
