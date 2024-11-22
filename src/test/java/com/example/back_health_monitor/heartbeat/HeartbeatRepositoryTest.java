package com.example.back_health_monitor.heartbeat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class HeartbeatRepositoryTest {

    @Autowired
    private HeartbeatRepository heartbeatRepository;

    @Test
    void shouldSaveAndFindHeartbeat() {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setHeartbeat(75.5f);
        heartbeat.setOxygenQuantity(98.0f);
        heartbeat.setDate(LocalDateTime.now());

        Heartbeat savedHeartbeat = heartbeatRepository.save(heartbeat);

        assertNotNull(savedHeartbeat.getId());

        Optional<Heartbeat> foundHeartbeat = heartbeatRepository.findById(savedHeartbeat.getId());

        assertTrue(foundHeartbeat.isPresent());
        assertEquals(75.5f, foundHeartbeat.get().getHeartbeat());
        assertEquals(98.0f, foundHeartbeat.get().getOxygenQuantity());
    }

    @Test
    void shouldDeleteHeartbeat() {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setHeartbeat(72.0f);
        heartbeat.setOxygenQuantity(96.0f);
        heartbeat.setDate(LocalDateTime.now());
        Heartbeat savedHeartbeat = heartbeatRepository.save(heartbeat);

        heartbeatRepository.deleteById(savedHeartbeat.getId());

        Optional<Heartbeat> deletedHeartbeat = heartbeatRepository.findById(savedHeartbeat.getId());
        assertFalse(deletedHeartbeat.isPresent());
    }
}
