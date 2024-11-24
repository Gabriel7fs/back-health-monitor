package com.example.back_health_monitor.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setCpf("12345678900");
        user.setUsername("User test");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setEmergencyContact(987654321L);
        user.setPassword("securepassword");
        user.setAddress("123 Main Street");
    }

    @Test
    void testUserResponseDTOConstructorFromUser() {
        UserResponseDTO userResponseDTO = new UserResponseDTO(user);

        assertEquals(user.getCpf(), userResponseDTO.cpf());
        assertEquals(user.getUsername(), userResponseDTO.patient_name());
        assertEquals(user.getBirthDate(), userResponseDTO.birth_date());
        assertEquals(user.getEmergencyContact(), userResponseDTO.emergency_contact());
        assertEquals(user.getPassword(), userResponseDTO.password());
        assertEquals(user.getAddress(), userResponseDTO.address());
    }

    @Test
    void testUserResponseDTOFields() {
        UserResponseDTO userResponseDTO = new UserResponseDTO(
                "98765432100",
                "User test",
                LocalDate.of(1995, 5, 15),
                123456789L,
                "anotherpassword",
                "456 test"
        );

        assertEquals("98765432100", userResponseDTO.cpf());
        assertEquals("User test", userResponseDTO.patient_name());
        assertEquals(LocalDate.of(1995, 5, 15), userResponseDTO.birth_date());
        assertEquals(123456789L, userResponseDTO.emergency_contact());
        assertEquals("anotherpassword", userResponseDTO.password());
        assertEquals("456 test", userResponseDTO.address());
    }
}
