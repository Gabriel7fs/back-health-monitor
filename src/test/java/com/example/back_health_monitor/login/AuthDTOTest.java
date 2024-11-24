package com.example.back_health_monitor.login;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDTOTest {

    @Test
    void testGetters() {
        AuthDTO authDTO = new AuthDTO("12345678901", "password123");

        assertEquals("12345678901", authDTO.cpf());
        assertEquals("password123", authDTO.password());
    }

    @Test
    void testEqualsAndHashCode() {
        AuthDTO authDTO1 = new AuthDTO("12345678901", "password123");
        AuthDTO authDTO2 = new AuthDTO("12345678901", "password123");

        assertEquals(authDTO1, authDTO2);
        assertEquals(authDTO1.hashCode(), authDTO2.hashCode());

        AuthDTO authDTO3 = new AuthDTO("98765432100", "password123");

        assertNotEquals(authDTO1, authDTO3);
        assertNotEquals(authDTO1.hashCode(), authDTO3.hashCode());
    }

    @Test
    void testToString() {
        AuthDTO authDTO = new AuthDTO("12345678901", "password123");

        String expectedToString = "AuthDTO[cpf=12345678901, password=password123]";
        assertEquals(expectedToString, authDTO.toString());
    }
}
