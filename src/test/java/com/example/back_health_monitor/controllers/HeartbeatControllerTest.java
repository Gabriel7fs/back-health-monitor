package com.example.back_health_monitor.controllers;

import com.example.back_health_monitor.heartbeat.HeartbeatCreateDTO;
import com.example.back_health_monitor.heartbeat.HeartbeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class HeartbeatControllerTest {

    @InjectMocks
    private HeartbeatController heartbeatController;

    @Mock
    private HeartbeatService heartbeatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateHeartbeat_Success() {
        HeartbeatCreateDTO dto = new HeartbeatCreateDTO();
        dto.setPacientId(1L);
        dto.setHeartbeat(72.5f);
        dto.setOxygenQuantity(98.5f);

        String roomId = "testRoom";

        doNothing().when(heartbeatService).generateHeartbeat(any(HeartbeatCreateDTO.class));

        heartbeatController.generateHeartbeat(roomId, dto);

        verify(heartbeatService, times(1)).generateHeartbeat(dto);
    }
}
