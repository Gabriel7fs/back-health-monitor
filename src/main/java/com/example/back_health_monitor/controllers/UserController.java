package com.example.back_health_monitor.controllers;

import com.example.back_health_monitor.heartbeat.HeartbeatDTO;
import com.example.back_health_monitor.heartbeat.HeartbeatService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.back_health_monitor.user.UserRepository;
import com.example.back_health_monitor.user.UserResponseDTO;
import com.example.back_health_monitor.user.UserService;
import com.example.back_health_monitor.user.UserCreateDTO;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository repository;
    private final UserService userService;
    private final HeartbeatService heartbeatService;

    public UserController(UserRepository repository, UserService userService, HeartbeatService heartbeatService) {
        this.repository = repository;
        this.userService = userService;
        this.heartbeatService = heartbeatService;
    }

    @GetMapping
    public List<UserResponseDTO> getAll() {
        return repository.findAll().stream().map(UserResponseDTO::new).toList();
    }

    @PostMapping
    public void createUser(@RequestBody UserCreateDTO dto) {
        this.userService.createUser(dto);
    }

    @PutMapping
    public void updateUser(@RequestBody UserCreateDTO dto) {
        this.userService.updateUser(dto);
    }

    @PutMapping(path = "/change-password")
    public void changePassword(@RequestBody UserCreateDTO dto) {
        this.userService.changePassword(dto);
    }

    @GetMapping(path = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HeartbeatDTO> dashboard(@RequestParam("userId") Long userId) {
        return this.heartbeatService.dashboard(userId);
    }
}
