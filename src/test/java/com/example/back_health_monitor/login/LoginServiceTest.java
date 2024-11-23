package com.example.back_health_monitor.login;

import com.example.back_health_monitor.exceptions.InvalidCredentialsException;
import com.example.back_health_monitor.exceptions.UserNotFoundException;
import com.example.back_health_monitor.user.User;
import com.example.back_health_monitor.user.UserRepository;
import com.example.back_health_monitor.user.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    private User testUser;
    private AuthDTO authDTO;
    private String encodedPassword = "$2a$10$ccoySt32ruSeLrsnwwnjve574VVMRIB/BsZXcFg8N4JNaIcxrOFvm";

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setCpf("12345678901");
        testUser.setUsername("testUser");
        testUser.setPassword(encodedPassword);
        testUser.setType(UserType.PACIENT);

        authDTO = new AuthDTO("12345678901", "password");

        when(passwordEncoder.matches("password", encodedPassword)).thenReturn(true);
        when(userRepository.findByCpf("12345678901")).thenReturn(Optional.of(testUser));

        Field passwordEncoderField = LoginService.class.getDeclaredField("passwordEncoder");
        passwordEncoderField.setAccessible(true);
        passwordEncoderField.set(loginService, passwordEncoder);

        Field secretField = LoginService.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(loginService, "test_secret");
    }

    @Test
    void login_ShouldReturnLoginDTO_WhenCredentialsAreValid() {
        LoginDTO loginDTO = loginService.login(authDTO);

        assertNotNull(loginDTO.getToken());
        assertEquals("testUser", loginDTO.getUser().getName());

        verify(userRepository, times(1)).findByCpf("12345678901");
        verify(passwordEncoder, times(1)).matches("password", encodedPassword);
    }

    @Test
    void login_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        when(userRepository.findByCpf("invalid_cpf")).thenReturn(Optional.empty());

        AuthDTO invalidAuthDTO = new AuthDTO("invalid_cpf", "password");

        assertThrows(UserNotFoundException.class, () -> loginService.login(invalidAuthDTO));
    }

    @Test
    void login_ShouldThrowInvalidCredentialsException_WhenPasswordIsIncorrect() {
        when(passwordEncoder.matches("wrong_password", encodedPassword)).thenReturn(false);

        AuthDTO invalidAuthDTO = new AuthDTO("12345678901", "wrong_password");

        assertThrows(InvalidCredentialsException.class, () -> loginService.login(invalidAuthDTO));
    }

    @Test
    void generateToken_ShouldReturnToken_WhenUserIsValid() {
        String token = loginService.generateToken(testUser);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void validateToken_ShouldReturnUsername_WhenTokenIsValid() {
        String token = loginService.generateToken(testUser);
        String username = loginService.validateToken(token);

        assertEquals("testUser", username);
    }

    @Test
    void validateToken_ShouldReturnEmptyString_WhenTokenIsInvalid() {
        String invalidToken = "invalid_token";
        String result = loginService.validateToken(invalidToken);

        assertEquals("", result);
    }
}
