package com.example.back_health_monitor.heartbeat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeartbeatCreateDTOTest {

    @Test
    void testHeartbeatCreateDTOSettersAndGetters() {
        HeartbeatCreateDTO dto = new HeartbeatCreateDTO();

        dto.setCpf("12345678900");
        dto.setPacientId(1L);
        dto.setHeartbeat(72.5f);
        dto.setOxygenQuantity(95.0f);

        assertEquals("12345678900", dto.getCpf());
        assertEquals(1L, dto.getPacientId());
        assertEquals(72.5f, dto.getHeartbeat());
        assertEquals(95.0f, dto.getOxygenQuantity());
    }

    @Test
    void testDefaultValues() {
        HeartbeatCreateDTO dto = new HeartbeatCreateDTO();

        assertNull(dto.getCpf());
        assertNull(dto.getPacientId());
        assertEquals(0.0f, dto.getHeartbeat());
        assertEquals(0.0f, dto.getOxygenQuantity());
    }

    @Test
    void testToString() {
        HeartbeatCreateDTO dto = new HeartbeatCreateDTO();
        dto.setCpf("12345678900");
        dto.setPacientId(1L);
        dto.setHeartbeat(72.5f);
        dto.setOxygenQuantity(95.0f);

        String expected = "HeartbeatCreateDTO(cpf=12345678900, pacientId=1, heartbeat=72.5, oxygenQuantity=95.0)";
        assertEquals(expected, dto.toString());
    }
}
