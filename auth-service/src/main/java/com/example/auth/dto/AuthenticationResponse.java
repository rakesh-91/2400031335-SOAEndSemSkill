package com.example.auth.dto;

public record AuthenticationResponse(String token, String tokenType, long expiresIn) {
}
