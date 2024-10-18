package com.example.back_health_monitor.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public UserController(UserRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
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
}
