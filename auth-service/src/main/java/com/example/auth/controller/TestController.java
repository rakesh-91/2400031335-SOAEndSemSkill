package com.example.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Operation(summary = "Public health-style endpoint")
    @GetMapping("/api/public")
    public Map<String, String> publicEndpoint() {
        return Map.of("message", "Public endpoint is accessible");
    }

    @Operation(summary = "Protected endpoint")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/api/secure")
    public Map<String, String> secureEndpoint(Authentication authentication) {
        return Map.of("message", "Secure endpoint accessed", "username", authentication.getName());
    }
}
