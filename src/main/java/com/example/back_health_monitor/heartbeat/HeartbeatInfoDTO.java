package com.example.back_health_monitor.heartbeat;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeartbeatInfoDTO {

    private float oxygenQuantity;

    private float heartbeat;

    private LocalDateTime date;

    public HeartbeatInfoDTO(float oxygenQuantity, float heartbeat, String s) {
    }
}
