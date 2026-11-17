package com.example.auth.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.example.auth.dto.RegisterRequest;
import com.example.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

class AuthenticationServiceTest {

    @Test
    void duplicateUsernameIsRejected() {
        UserRepository repository = mock(UserRepository.class);
        when(repository.existsByUsername("admin")).thenReturn(true);
        AuthenticationService service = new AuthenticationService(repository, mock(PasswordEncoder.class),
                mock(org.springframework.security.authentication.AuthenticationManager.class),
                mock(com.example.auth.security.CustomUserDetailsService.class),
                mock(com.example.auth.security.JwtService.class));

        assertThrows(ResponseStatusException.class,
                () -> service.register(new RegisterRequest("admin", "Admin@123")));
        verify(repository, never()).save(any());
    }
}
