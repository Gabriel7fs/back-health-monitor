package com.example.back_health_monitor.user;

import java.util.Set;

import lombok.Data;

@Data
public class UserCreateDTO {
    private Long id;

    private String cpf;

    private Long emergencyContact;

    private String username;

    private String password;

    private String birthdate;

    private String address;

    private UserType type;

    private Set<String> cpfAssociateds;
}
