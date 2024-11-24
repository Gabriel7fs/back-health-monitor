package com.example.back_health_monitor.login;

import com.example.back_health_monitor.user.UserDTO;
import com.example.back_health_monitor.user.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LoginDTOTest {

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(
                1L,
                "User test",
                "12345678900",
                "123 test",
                987654321L,
                "securepassword",
                LocalDate.of(1990, 1, 1),
                UserType.PACIENT
        );
    }

    @Test
    void testLoginDTOConstructor() {
        String token = "sample_token";
        LoginDTO loginDTO = new LoginDTO(token, userDTO);

        assertEquals(token, loginDTO.getToken());
        assertEquals(userDTO, loginDTO.getUser());
    }

    @Test
    void testLoginDTOSettersAndGetters() {
        LoginDTO loginDTO = new LoginDTO("initial_token", userDTO);

        loginDTO.setToken("updated_token");
        assertEquals("updated_token", loginDTO.getToken());

        UserDTO newUserDTO = new UserDTO(
                2L,
                "User test",
                "98765432100",
                "456 test",
                123456789L,
                "newpassword",
                LocalDate.of(1995, 5, 15),
                UserType.MONITOR
        );
        loginDTO.setUser(newUserDTO);
        assertEquals(newUserDTO, loginDTO.getUser());
    }

    @Test
    void testLoginDTOToString() {
        LoginDTO loginDTO = new LoginDTO("token123", userDTO);
        String expected = "LoginDTO(token=token123, user=UserDTO(id=1, name=User test, cpf=12345678900, address=123 test, emergencyContact=987654321, password=securepassword, birthDate=1990-01-01, type=PACIENT))";
        assertEquals(expected, loginDTO.toString());
    }
}
