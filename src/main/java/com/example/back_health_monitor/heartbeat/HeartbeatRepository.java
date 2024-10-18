package com.example.back_health_monitor.heartbeat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartbeatRepository extends JpaRepository<Heartbeat, Long> {
}
