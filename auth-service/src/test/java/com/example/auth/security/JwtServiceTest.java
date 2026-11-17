package com.example.auth.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService(
            "local-development-secret-change-me-please-32-bytes", 3_600_000);

    @Test
    void generatedTokenIsValidForUser() {
        UserDetails user = org.springframework.security.core.userdetails.User.withUsername("admin")
                .password("encoded").authorities("ROLE_USER").build();
        assertTrue(jwtService.isTokenValid(jwtService.generateToken(user), user));
    }

    @Test
    void malformedTokenIsRejected() {
        assertFalse(jwtService.isTokenValid("not-a-jwt"));
    }
}
