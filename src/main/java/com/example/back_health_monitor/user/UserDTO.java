package com.example.back_health_monitor.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String name;

    private String cpf;

    private String address;

    private Long emergencyContact;

    private String password;

    private LocalDate birthDate;

    private UserType type;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getUsername();
        this.cpf = user.getCpf();
        this.address = user.getAddress();
        this.emergencyContact = user.getEmergencyContact();
        this.birthDate = user.getBirthDate();
        this.type = user.getType();
    }
}
