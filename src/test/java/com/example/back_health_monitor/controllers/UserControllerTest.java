package com.example.back_health_monitor.controllers;

import com.example.back_health_monitor.heartbeat.HeartbeatDTO;
import com.example.back_health_monitor.heartbeat.HeartbeatInfoDTO;
import com.example.back_health_monitor.heartbeat.HeartbeatService;
import com.example.back_health_monitor.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.back_health_monitor.login.JwtAuthenticationFilter;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({JwtAuthenticationFilter.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private HeartbeatService heartbeatService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @TestConfiguration
    static class SecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable());
            return http.build();
        }
    }

    @Test
    @WithMockUser
    void testCreateUser_Success() throws Exception {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setCpf("123456789");
        dto.setUsername("User Test");
        dto.setPassword("password");

        Mockito.doNothing().when(userService).createUser(Mockito.any(UserCreateDTO.class));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testUpdateUser_Success() throws Exception {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setId(1L);
        dto.setUsername("User test");

        Mockito.doNothing().when(userService).updateUser(Mockito.any(UserCreateDTO.class));

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testChangePassword_Success() throws Exception {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setCpf("123456789");
        dto.setPassword("newPassword");

        Mockito.doNothing().when(userService).changePassword(Mockito.any(UserCreateDTO.class));

        mockMvc.perform(put("/user/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testDashboard_Success() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "User Test", "123456789", "address", 123L, "password", null, UserType.PACIENT);
        HeartbeatInfoDTO heartbeatInfo = new HeartbeatInfoDTO();
        heartbeatInfo.setHeartbeat(72.5f);
        heartbeatInfo.setOxygenQuantity(98.5f);
        heartbeatInfo.setDate(LocalDateTime.parse("2023-11-18T10:15:30"));

        HeartbeatDTO heartbeatDTO = new HeartbeatDTO(userDTO, List.of(heartbeatInfo));
        List<HeartbeatDTO> dashboard = List.of(heartbeatDTO);

        Mockito.when(heartbeatService.dashboard(1L)).thenReturn(dashboard);
    }
}
