package com.example.back_health_monitor.login;

import com.example.back_health_monitor.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDTO {

    private String token;

    private UserDTO user;

}
