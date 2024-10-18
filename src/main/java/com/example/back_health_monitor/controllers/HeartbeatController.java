package com.example.back_health_monitor.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.back_health_monitor.heartbeat.HeartbeatCreateDTO;
import com.example.back_health_monitor.heartbeat.HeartbeatDTO;
import com.example.back_health_monitor.heartbeat.HeartbeatService;

@Controller
@RequestMapping("/heartbeat")
public class HeartbeatController {
    @Autowired
    private HeartbeatService heartbeatService;

    @MessageMapping("/generate")
    public void generateHeartbeat(@RequestBody HeartbeatCreateDTO dto) {
        this.heartbeatService.generateHeartbeat(dto);
    }

    @GetMapping("/dashboard")
    public List<HeartbeatDTO> dashboard(@RequestParam("userId") Long userId) {
        return this.heartbeatService.dashboard(userId);
    }
}
