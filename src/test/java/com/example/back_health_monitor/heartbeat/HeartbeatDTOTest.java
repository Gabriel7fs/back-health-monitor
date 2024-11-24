package com.example.back_health_monitor.heartbeat;

import com.example.back_health_monitor.user.UserDTO;
import com.example.back_health_monitor.user.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeartbeatDTOTest {

    private UserDTO userDTO;
    private List<HeartbeatInfoDTO> heartbeats;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(
                1L,
                "User test",
                "12345678900",
                "123 test",
                987654321L,
                "securepassword",
                LocalDate.of(1990, 1, 1),
                UserType.PACIENT
        );

        HeartbeatInfoDTO heartbeatInfo = new HeartbeatInfoDTO(72.5f, 95.0f, "2024-01-01T12:00");
        heartbeats = List.of(heartbeatInfo);
    }

    @Test
    void testHeartbeatDTOConstructor() {
        HeartbeatDTO heartbeatDTO = new HeartbeatDTO(userDTO, heartbeats);

        assertEquals(userDTO, heartbeatDTO.getUser());
        assertEquals(heartbeats, heartbeatDTO.getHeartbeats());
    }

    @Test
    void testHeartbeatDTOSettersAndGetters() {
        HeartbeatDTO heartbeatDTO = new HeartbeatDTO();

        heartbeatDTO.setUser(userDTO);
        heartbeatDTO.setHeartbeats(heartbeats);

        assertEquals(userDTO, heartbeatDTO.getUser());
        assertEquals(heartbeats, heartbeatDTO.getHeartbeats());
    }

    @Test
    void testDefaultConstructor() {
        HeartbeatDTO heartbeatDTO = new HeartbeatDTO();

        assertNull(heartbeatDTO.getUser());
        assertNull(heartbeatDTO.getHeartbeats());
    }
}
