package com.example.back_health_monitor.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(summary = "Realiza o login do usuário", description = "Endpoint para autenticação de usuários.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"error\": \"Credenciais inválidas\" }")))
    })
    @PostMapping(path = "/login")
    public LoginDTO login(@RequestBody AuthDTO dto) {
        return this.loginService.login(dto);
    }

}
