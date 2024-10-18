package com.example.back_health_monitor.heartbeat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.back_health_monitor.exceptions.UserNotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.back_health_monitor.user.User;
import com.example.back_health_monitor.user.UserDTO;
import com.example.back_health_monitor.user.UserRepository;

@Service
public class HeartbeatService {

    private final HeartbeatRepository heartbeatRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public HeartbeatService(HeartbeatRepository heartbeatRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.heartbeatRepository = heartbeatRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void generateHeartbeat(HeartbeatCreateDTO dto) {
        Optional<User> optUser = this.userRepository.findById(dto.getPacientId());
        if (optUser.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado.");
        }

        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setHeartbeat(dto.getHeartbeat());
        heartbeat.setOxygenQuantity(dto.getOxygenQuantity());
        heartbeat.setUser(optUser.get());
        heartbeat.setDate(LocalDateTime.now());

        this.heartbeatRepository.save(heartbeat);

        List<HeartbeatDTO> dash = this.dashboard(dto.getPacientId());

        this.messagingTemplate.convertAndSend("/topic/messages", dash);
    }

    @Transactional
    public List<HeartbeatDTO> dashboard(Long userId) {
        Optional<User> optUser = this.userRepository.findById(userId);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado.");
        }

        return optUser.get().getAssociateds().stream().map(associated -> {
            UserDTO user = new UserDTO();
            user.setId(associated.getId());
            user.setName(associated.getUsername());

            List<HeartbeatInfoDTO> heartbeats = associated.getHeartbeats().stream().map(heart -> {
                HeartbeatInfoDTO dto = new HeartbeatInfoDTO();
                dto.setHeartbeat(heart.getHeartbeat());
                dto.setOxygenQuantity(heart.getOxygenQuantity());
                dto.setDate(heart.getDate());

                return dto;
            }).toList();

            HeartbeatDTO heartDTO = new HeartbeatDTO();
            heartDTO.setUser(user);
            heartDTO.setHeartbeats(heartbeats);

            return heartDTO;
        }).toList();
    }
}
