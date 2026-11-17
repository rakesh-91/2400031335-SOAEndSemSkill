package com.example.auth.controller;

import com.example.auth.dto.AuthenticationResponse;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Register a user")
    @ApiResponse(responseCode = "201", description = "User registered")
    @ApiResponse(responseCode = "409", description = "Username already exists")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Login and generate a JWT")
    @ApiResponse(responseCode = "200", description = "JWT generated")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public AuthenticationResponse login(@Valid @RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @Operation(summary = "Validate the current JWT")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/validate")
    public Map<String, Object> validate(@RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")
                || !authenticationService.isTokenValid(authorization.substring(7))) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid or missing token");
        }
        return Map.of("valid", true, "message", "Token is valid");
    }
}
