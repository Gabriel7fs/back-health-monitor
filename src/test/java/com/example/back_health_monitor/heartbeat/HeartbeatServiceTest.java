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

        heartbeatService = spy(new HeartbeatService(heartbeatRepository, userRepository, messagingTemplate));
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
        heartbeat.setHeartbeat(80.0f);
        heartbeat.setOxygenQuantity(96.0f);
        heartbeat.setDate(LocalDateTime.now());

        associatedUser.setHeartbeats(List.of(heartbeat));
        testUser.setAssociateds(List.of(associatedUser));

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        List<HeartbeatDTO> result = heartbeatService.dashboard(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getHeartbeats().size());
        assertEquals("associatedUser", result.get(0).getUser().getName());
        assertEquals(80.0f, result.get(0).getHeartbeats().get(0).getHeartbeat());
        assertEquals(96.0f, result.get(0).getHeartbeats().get(0).getOxygenQuantity());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void dashboard_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            heartbeatService.dashboard(1L);
        });

        assertEquals("Usuário não encontrado.", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
    }


    @Test
    void generateHeartbeat_ShouldSendMessagesToAssociatedUsers_WhenAssociatedUsersExist() {
        User associatedUser = new User();
        associatedUser.setId(2L);
        associatedUser.setUsername("associatedUser");
        associatedUser.setCpf("98765432100");

        testUser.setAssociateds(List.of(associatedUser));

        when(userRepository.findById(2L)).thenReturn(Optional.of(associatedUser));

        List<HeartbeatDTO> associatedDash = List.of(new HeartbeatDTO());
        doReturn(associatedDash).when(heartbeatService).dashboard(2L);

        heartbeatService.generateHeartbeat(heartbeatCreateDTO);

        verify(messagingTemplate, times(1))
                .convertAndSend("/topic/messages/98765432100", associatedDash);
    }

    @Test
    void getCpfUsersAssociateds_ShouldReturnAssociatedUsers_WhenUserExists() {
        User associatedUser = new User();
        associatedUser.setId(2L);
        associatedUser.setUsername("associatedUser");

        testUser.setAssociateds(List.of(associatedUser));

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        List<User> result = heartbeatService.getCpfUsersAssociateds(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("associatedUser", result.get(0).getUsername());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getCpfUsersAssociateds_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            heartbeatService.getCpfUsersAssociateds(1L);
        });

        assertEquals("Usuário não encontrado.", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
    }
}
