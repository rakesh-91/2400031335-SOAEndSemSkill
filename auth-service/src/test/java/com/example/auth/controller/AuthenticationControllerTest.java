package com.example.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TestController.class)
@Import(com.example.auth.config.SecurityConfig.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.example.auth.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void publicEndpointIsAccessibleWithoutToken() throws Exception {
        mockMvc.perform(get("/api/public")).andExpect(status().isOk());
    }
}
