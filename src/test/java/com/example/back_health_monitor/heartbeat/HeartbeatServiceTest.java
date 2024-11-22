package com.example.back_health_monitor.heartbeat;

import com.example.back_health_monitor.exceptions.UserNotFoundException;
import com.example.back_health_monitor.user.User;
import com.example.back_health_monitor.user.UserRepository;
import com.example.back_health_monitor.user.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HeartbeatServiceTest {

    @Mock
    private HeartbeatRepository heartbeatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private HeartbeatService heartbeatService;

    private User testUser;
    private HeartbeatCreateDTO heartbeatCreateDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setCpf("12345678901");
        testUser.setType(UserType.PACIENT);
        testUser.setAssociateds(new ArrayList<>());
        testUser.setHeartbeats(new ArrayList<>());

        heartbeatCreateDTO = new HeartbeatCreateDTO();
        heartbeatCreateDTO.setPacientId(1L);
        heartbeatCreateDTO.setHeartbeat(75.5f);
        heartbeatCreateDTO.setOxygenQuantity(98.0f);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    }

    @Test
    void generateHeartbeat_ShouldSaveHeartbeatAndSendMessage_WhenUserExists() {
        heartbeatService.generateHeartbeat(heartbeatCreateDTO);

        verify(userRepository, times(3)).findById(heartbeatCreateDTO.getPacientId()); // Ajuste para 3 invocações
        verify(heartbeatRepository, times(1)).save(any(Heartbeat.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/messages/" + heartbeatCreateDTO.getCpf()), anyList());
    }

    @Test
    void generateHeartbeat_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> heartbeatService.generateHeartbeat(heartbeatCreateDTO));

        verify(heartbeatRepository, never()).save(any(Heartbeat.class));
        verify(messagingTemplate, never()).convertAndSend(any(String.class), any(Object.class));
    }


    @Test
    void dashboard_ShouldReturnHeartbeatDTOList_WhenUserExists() {
        User associatedUser = new User();
        associatedUser.setId(2L);
        associatedUser.setUsername("associatedUser");

        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setHeartbeat(75.5f);
        heartbeat.setOxygenQuantity(98.0f);
        heartbeat.setDate(LocalDateTime.now());

        associatedUser.setHeartbeats(List.of(heartbeat));
        testUser.setAssociateds(List.of(associatedUser));

        List<HeartbeatDTO> result = heartbeatService.dashboard(testUser.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getHeartbeats().size());
        assertEquals("associatedUser", result.get(0).getUser().getName());

        verify(userRepository, times(1)).findById(testUser.getId());
    }

    @Test
    void dashboard_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> heartbeatService.dashboard(testUser.getId()));

        verify(userRepository, times(1)).findById(testUser.getId());
    }
}
