package com.example.back_health_monitor.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDTO {

    private String token;

    private Object user;

}
