package com.diaa.movie_reservation.security;

import com.diaa.movie_reservation.service.JwtService;
import com.diaa.movie_reservation.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {
    private JwtService jwtService;
    private UserService userService;
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        userService = mock(UserService.class);
        jwtAuthFilter = new JwtAuthFilter(jwtService, userService);
    }

    @Test
    void unknownUserShouldReturn401() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testToken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(jwtService.extractEmail("testToken")).thenReturn("unknown@example.com");
        when(userService.findByEmail("unknown@example.com")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        // Filter chain should not be invoked
        assertNull(filterChain.getRequest());
    }
}