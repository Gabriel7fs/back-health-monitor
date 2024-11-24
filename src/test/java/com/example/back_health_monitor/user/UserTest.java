package com.example.back_health_monitor.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setCpf("12345678900");
        user.setUsername("test_user");
        user.setPassword("password123");
        user.setAddress("123 test");
        user.setEmergencyContact(987654321L);
        user.setBirthDate(LocalDate.of(2000, 1, 1));
    }

    @Test
    void testUserAuthoritiesForMonitor() {
        user.setType(UserType.MONITOR);
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities();
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testUserAuthoritiesForPacient() {
        user.setType(UserType.PACIENT);
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_USER", authorities.get(0).getAuthority());
    }

    @Test
    void testDefaultValues() {
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testToString() {
        String expected = "User{id=1, cpf='12345678900', username='test_user', address='123 test', " +
                "emergencyContact=987654321, birthDate=2000-01-01, type=null}";
        assertEquals(expected, user.toString());
    }

    @Test
    void testAssociatedsInitialization() {
        assertNotNull(user.getAssociateds());
        assertTrue(user.getAssociateds().isEmpty());
    }

    @Test
    void testHeartbeatsInitialization() {
        assertNotNull(user.getHeartbeats());
        assertTrue(user.getHeartbeats().isEmpty());
    }
}
