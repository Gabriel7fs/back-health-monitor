package com.example.back_health_monitor.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(repository);
    }

    @Test
    void createUser_ShouldSavePacientUser() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setCpf("12345678901");
        dto.setPassword("password");
        dto.setUsername("pacientUser");
        dto.setType(UserType.PACIENT);
        dto.setBirthdate("01-01-2000");

        when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(repository.findAllByCpfIn(any())).thenReturn(List.of());

        userService.createUser(dto);

        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldUpdateUserDetails() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setId(1L);
        dto.setUsername("updatedUser");
        dto.setAddress("updatedAddress");

        User user = new User();
        user.setId(1L);
        user.setUsername("oldUser");
        user.setAddress("oldAddress");
        user.setType(UserType.PACIENT);

        when(repository.findById(dto.getId())).thenReturn(Optional.of(user));

        userService.updateUser(dto);

        assertEquals("updatedUser", user.getUsername());
        assertEquals("updatedAddress", user.getAddress());
        verify(repository, times(1)).save(user);
    }

    @Test
    void createUser_ShouldAssociateUsers() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setCpf("12345678901");
        dto.setPassword("password");
        dto.setUsername("pacientUser");
        dto.setType(UserType.PACIENT);
        dto.setCpfAssociateds(Set.of("98765432109"));
        dto.setBirthdate("01-01-2000");

        User associatedUser = new User();
        associatedUser.setCpf("98765432109");
        associatedUser.setType(UserType.MONITOR);

        when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(repository.findAllByCpfIn(dto.getCpfAssociateds())).thenReturn(List.of(associatedUser));

        userService.createUser(dto);

        verify(repository, times(3)).save(any(User.class));
    }
}
