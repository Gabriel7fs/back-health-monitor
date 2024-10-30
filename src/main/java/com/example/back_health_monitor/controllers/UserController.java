package com.example.back_health_monitor.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(summary = "Retorna todos os usuários", description = "Busca todos os usuários registrados e retorna como uma lista de UserResponseDTO.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class)))
    @GetMapping
    public List<UserResponseDTO> getAll() {
        return repository.findAll().stream().map(UserResponseDTO::new).toList();
    }

    @Operation(summary = "Cria um novo usuário", description = "Cria um novo usuário com as informações fornecidas.")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    @PostMapping
    public void createUser(@RequestBody UserCreateDTO dto) {
        this.userService.createUser(dto);
    }

    @Operation(summary = "Atualiza um usuário existente", description = "Atualiza as informações de um usuário com base nos dados fornecidos.")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    @PutMapping
    public void updateUser(@RequestBody UserCreateDTO dto) {
        this.userService.updateUser(dto);
    }

    @Operation(summary = "Altera a senha do usuário", description = "Permite a alteração da senha do usuário.")
    @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso")
    @PutMapping(path = "/change-password")
    public void changePassword(@RequestBody UserCreateDTO dto) {
        this.userService.changePassword(dto);
    }

    @Operation(summary = "Exibe o dashboard do usuário", description = "Retorna informações de batimentos cardíacos do usuário especificado pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados do dashboard retornados com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HeartbeatDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"error\": \"Usuário não encontrado\" }")))
    })
    @GetMapping(path = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HeartbeatDTO> dashboard(@RequestParam("userId") Long userId) {
        return this.heartbeatService.dashboard(userId);
    }
}
