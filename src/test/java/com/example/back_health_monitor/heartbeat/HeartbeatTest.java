package com.example.back_health_monitor.heartbeat;

import com.example.back_health_monitor.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HeartbeatTest {

    private Heartbeat heartbeat;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("User test");

        heartbeat = new Heartbeat();
        heartbeat.setId(1L);
        heartbeat.setHeartbeat(72.5f);
        heartbeat.setOxygenQuantity(95.0f);
        heartbeat.setDate(LocalDateTime.of(2024, 1, 1, 12, 0));
        heartbeat.setUser(user);
    }

    @Test
    void testHeartbeatGetters() {
        assertEquals(1L, heartbeat.getId());
        assertEquals(72.5f, heartbeat.getHeartbeat());
        assertEquals(95.0f, heartbeat.getOxygenQuantity());
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 0), heartbeat.getDate());
        assertEquals(user, heartbeat.getUser());
    }

    @Test
    void testHeartbeatSetters() {
        Heartbeat newHeartbeat = new Heartbeat();
        newHeartbeat.setId(2L);
        newHeartbeat.setHeartbeat(80.0f);
        newHeartbeat.setOxygenQuantity(98.0f);
        newHeartbeat.setDate(LocalDateTime.of(2024, 2, 1, 15, 30));
        newHeartbeat.setUser(user);

        assertEquals(2L, newHeartbeat.getId());
        assertEquals(80.0f, newHeartbeat.getHeartbeat());
        assertEquals(98.0f, newHeartbeat.getOxygenQuantity());
        assertEquals(LocalDateTime.of(2024, 2, 1, 15, 30), newHeartbeat.getDate());
        assertEquals(user, newHeartbeat.getUser());
    }

    @Test
    void testToString() {
        String expected = "Heartbeat(id=1, heartbeat=72.5, oxygenQuantity=95.0, date=2024-01-01T12:00, user=null)";
        heartbeat.setUser(null); // Avoid printing the full user object
        assertEquals(expected, heartbeat.toString());
    }
}
