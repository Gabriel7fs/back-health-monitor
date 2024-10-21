package com.example.back_health_monitor.heartbeat;

import lombok.Data;

@Data
public class HeartbeatCreateDTO {
    
    private Long pacientId;

    private float heartbeat;

    private float oxygenQuantity;
}
