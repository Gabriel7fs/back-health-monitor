package com.example.back_health_monitor.controllers;

import com.example.back_health_monitor.heartbeat.HeartbeatCreateDTO;
import com.example.back_health_monitor.heartbeat.HeartbeatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(summary = "Gera dados de batimentos cardíacos", description = "Endpoint para gerar informações de frequência cardíaca e nível de oxigênio para um paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados de batimentos cardíacos gerados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"error\": \"Dados inválidos\" }")))
    })
    @PostMapping("/generate")
    public void generateHeartbeat(@RequestBody HeartbeatCreateDTO dto) {
        this.heartbeatService.generateHeartbeat(dto);
    }

}
