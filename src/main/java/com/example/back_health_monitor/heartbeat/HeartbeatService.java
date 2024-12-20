package com.example.back_health_monitor.heartbeat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.back_health_monitor.exceptions.UserNotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.back_health_monitor.user.User;
import com.example.back_health_monitor.user.UserDTO;
import com.example.back_health_monitor.user.UserRepository;

@Service
public class HeartbeatService {

    private final HeartbeatRepository heartbeatRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String USER_NOT_FOUND_MESSAGE = "Usuário não encontrado.";

    public HeartbeatService(HeartbeatRepository heartbeatRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.heartbeatRepository = heartbeatRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void generateHeartbeat(HeartbeatCreateDTO dto) {
        Optional<User> optUser = this.userRepository.findByCpf(dto.getCpf());
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
        }

        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setHeartbeat(dto.getHeartbeat());
        heartbeat.setOxygenQuantity(dto.getOxygenQuantity());
        heartbeat.setUser(optUser.get());
        heartbeat.setDate(LocalDateTime.now());

        this.heartbeatRepository.save(heartbeat);

        List<User> cpfUsersAssociateds = this.getCpfUsersAssociateds(dto.getCpf());
        List<HeartbeatDTO> dashPacient = new ArrayList<>();
        dashPacient.add(
                this.dashboardPacient(dto.getCpf())
        );
        this.messagingTemplate.convertAndSend("/topic/messages/" + optUser.get().getId(), dashPacient);

        cpfUsersAssociateds.forEach(cpf -> {
            List<HeartbeatDTO> dash = this.dashboard(cpf.getCpf());
            this.messagingTemplate.convertAndSend("/topic/messages/" + cpf.getId(), dash);
        });
    }

    public List<User> getCpfUsersAssociateds(String userId) {
        Optional<User> optUser = this.userRepository.findByCpf(userId);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
        }

        return optUser.get().getAssociateds();
    }

    public List<HeartbeatDTO> dashboard(String userId) {
        Optional<User> optUser = this.userRepository.findByCpf(userId);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
        }

        List<User> associateds = optUser.get().getAssociateds();
        return associateds.stream().map(associated -> {
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

    public HeartbeatDTO dashboardPacient(String userId) {
        Optional<User> optUser = this.userRepository.findByCpf(userId);
        if (optUser.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
        }

        User userModel = optUser.get();


            UserDTO user = new UserDTO();
            user.setId(userModel.getId());
            user.setName(userModel.getUsername());

            List<HeartbeatInfoDTO> heartbeats = userModel.getHeartbeats().stream().map(heart -> {
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
    }
}
