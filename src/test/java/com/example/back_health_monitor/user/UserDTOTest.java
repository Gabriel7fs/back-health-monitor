package com.example.back_health_monitor.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("JohnDoe");
        user.setCpf("12345678900");
        user.setAddress("123 Main Street");
        user.setEmergencyContact(987654321L);
        user.setPassword("password123");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setType(UserType.PACIENT);
    }

    @Test
    void testUserDTOConstructor() {
        UserDTO userDTO = new UserDTO(user);

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getUsername(), userDTO.getName());
        assertEquals(user.getCpf(), userDTO.getCpf());
        assertEquals(user.getAddress(), userDTO.getAddress());
        assertEquals(user.getEmergencyContact(), userDTO.getEmergencyContact());
        assertEquals(user.getBirthDate(), userDTO.getBirthDate());
        assertEquals(user.getType(), userDTO.getType());
    }

    @Test
    void testUserDTOGettersAndSetters() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(2L);
        userDTO.setName("JaneDoe");
        userDTO.setCpf("09876543210");
        userDTO.setAddress("456 Elm Street");
        userDTO.setEmergencyContact(123456789L);
        userDTO.setPassword("newpassword");
        userDTO.setBirthDate(LocalDate.of(1995, 5, 15));
        userDTO.setType(UserType.MONITOR);

        assertEquals(2L, userDTO.getId());
        assertEquals("JaneDoe", userDTO.getName());
        assertEquals("09876543210", userDTO.getCpf());
        assertEquals("456 Elm Street", userDTO.getAddress());
        assertEquals(123456789L, userDTO.getEmergencyContact());
        assertEquals("newpassword", userDTO.getPassword());
        assertEquals(LocalDate.of(1995, 5, 15), userDTO.getBirthDate());
        assertEquals(UserType.MONITOR, userDTO.getType());
    }

    @Test
    void testDefaultConstructor() {
        UserDTO userDTO = new UserDTO();

        assertNull(userDTO.getId());
        assertNull(userDTO.getName());
        assertNull(userDTO.getCpf());
        assertNull(userDTO.getAddress());
        assertNull(userDTO.getEmergencyContact());
        assertNull(userDTO.getPassword());
        assertNull(userDTO.getBirthDate());
        assertNull(userDTO.getType());
    }
}
