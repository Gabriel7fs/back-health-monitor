package com.example.back_health_monitor.controllers;

import com.example.back_health_monitor.exceptions.InvalidCredentialsException;
import com.example.back_health_monitor.login.AuthDTO;
import com.example.back_health_monitor.login.LoginDTO;
import com.example.back_health_monitor.login.LoginService;
import com.example.back_health_monitor.user.UserDTO;
import com.example.back_health_monitor.user.UserRepository;
import com.example.back_health_monitor.user.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @MockBean
    private UserRepository userRepository;

    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        AuthDTO authDTO = new AuthDTO("12345678901", "password");

        UserDTO userDTO = new UserDTO(1L, "testUser", "12345678901", "testAddress", 123456789L, null, null, UserType.PACIENT);
        loginDTO = new LoginDTO("testToken", userDTO);
    }

    @Test
    void login_ShouldReturnLoginDTO_WhenCredentialsAreValid() throws Exception {
        when(loginService.login(any(AuthDTO.class))).thenReturn(loginDTO);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cpf\": \"12345678901\", \"password\": \"password\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("testToken"))
                .andExpect(jsonPath("$.user.name").value("testUser"));
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {
        when(loginService.login(any(AuthDTO.class))).thenThrow(new InvalidCredentialsException("Credenciais inválidas."));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cpf\": \"12345678901\", \"password\": \"wrongPassword\" }"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenciais inválidas."));
    }
}