package com.example.back_health_monitor.user;

import java.time.LocalDate;

public record UserResponseDTO(String cpf, String patient_name, LocalDate birth_date, Integer emergency_contact, String password, String address) {

    public UserResponseDTO(User patient){
        this(
                patient.getCpf(),
                patient.getUsername(),
                patient.getBirthDate(),
                patient.getEmergencyContact(),
                patient.getPassword(),
                patient.getAddress()
        );
    }
}
