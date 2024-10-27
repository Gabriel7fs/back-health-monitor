package com.example.back_health_monitor.heartbeat;

import java.util.List;

import com.example.back_health_monitor.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeartbeatDTO {

    private UserDTO user;

    private List<HeartbeatInfoDTO> heartbeats;

}
