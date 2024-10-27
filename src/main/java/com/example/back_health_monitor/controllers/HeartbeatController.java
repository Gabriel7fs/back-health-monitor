package com.example.back_health_monitor.controllers;

import com.example.back_health_monitor.heartbeat.HeartbeatCreateDTO;
import com.example.back_health_monitor.heartbeat.HeartbeatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/heartbeat")
public class HeartbeatController {

    private final HeartbeatService heartbeatService;

    public HeartbeatController(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }

    @PostMapping("/generate")
    public void generateHeartbeat(@RequestBody HeartbeatCreateDTO dto) {
        this.heartbeatService.generateHeartbeat(dto);
    }

}
