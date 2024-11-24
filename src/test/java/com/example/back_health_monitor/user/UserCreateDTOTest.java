package com.example.back_health_monitor.user;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserCreateDTOTest {

    @Test
    void testGettersAndSetters() {
        UserCreateDTO dto = new UserCreateDTO();

        dto.setId(1L);
        dto.setCpf("12345678901");
        dto.setEmergencyContact(987654321L);
        dto.setUsername("Test User");
        dto.setPassword("password123");
        dto.setBirthdate("2000-01-01");
        dto.setAddress("123 Main Street");
        dto.setType(UserType.PACIENT);
        dto.setCpfAssociateds(Set.of("98765432100", "12345678900"));

        assertEquals(1L, dto.getId());
        assertEquals("12345678901", dto.getCpf());
        assertEquals(987654321L, dto.getEmergencyContact());
        assertEquals("Test User", dto.getUsername());
        assertEquals("password123", dto.getPassword());
        assertEquals("2000-01-01", dto.getBirthdate());
        assertEquals("123 Main Street", dto.getAddress());
        assertEquals(UserType.PACIENT, dto.getType());
        assertEquals(Set.of("98765432100", "12345678900"), dto.getCpfAssociateds());
    }

    @Test
    void testEqualsAndHashCode() {
        UserCreateDTO dto1 = new UserCreateDTO();
        dto1.setId(1L);
        dto1.setCpf("12345678901");

        UserCreateDTO dto2 = new UserCreateDTO();
        dto2.setId(1L);
        dto2.setCpf("12345678901");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        dto2.setCpf("98765432100");

        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setId(1L);
        dto.setCpf("12345678901");
        dto.setUsername("Test User");

        String toString = dto.toString();

        assertTrue(toString.contains("12345678901"));
        assertTrue(toString.contains("Test User"));
    }
}
