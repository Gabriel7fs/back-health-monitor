package com.example.back_health_monitor.controllers;

import com.example.back_health_monitor.heartbeat.HeartbeatCreateDTO;
import com.example.back_health_monitor.heartbeat.HeartbeatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class HeartbeatController {

    private final HeartbeatService heartbeatService;

    @Operation(summary = "Gera dados de batimentos cardíacos", description = "Endpoint para gerar informações de frequência cardíaca e nível de oxigênio para um paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados de batimentos cardíacos gerados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"error\": \"Dados inválidos\" }"))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"error\": \"Usuário não encontrado.\" }")))
    })
    @MessageMapping("/chat/{roomId}")
    public void generateHeartbeat(@DestinationVariable String roomId, @RequestBody HeartbeatCreateDTO dto) {
        this.heartbeatService.generateHeartbeat(dto);
    }

}
