package com.example.back_health_monitor.login;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import com.example.back_health_monitor.exceptions.InvalidCredentialsException;
import com.example.back_health_monitor.exceptions.TokenGenerationException;
import com.example.back_health_monitor.exceptions.UserNotFoundException;
import com.example.back_health_monitor.user.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.back_health_monitor.user.User;
import com.example.back_health_monitor.user.UserRepository;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${token.secret}")
    private String secret;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public LoginDTO login(AuthDTO dto) {
        Optional<User> optUser = this.userRepository.findByCpf(dto.cpf());
        if (optUser.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado.");
        }

        User user = optUser.get();
        boolean isPasswordValid = this.passwordEncoder.matches(dto.password(), user.getPassword());
        if (!isPasswordValid) {
            throw new InvalidCredentialsException("Credenciais inválidas.");
        }

        String token = this.generateToken(user);
        UserDTO userDTO = new UserDTO(optUser.get());

        return new LoginDTO(token, userDTO);
    }

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create().withIssuer("auth-api").withSubject(user.getUsername())
                    .withExpiresAt(this.genExpirationDate()).sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new TokenGenerationException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm).withIssuer("auth-api").build().verify(token).getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
