package com.example.back_health_monitor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.back_health_monitor.login.AuthDTO;
import com.example.back_health_monitor.login.LoginDTO;
import com.example.back_health_monitor.login.LoginService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(path = "/login")
    public LoginDTO login(@RequestBody AuthDTO dto) {
        return this.loginService.login(dto);
    }

}
