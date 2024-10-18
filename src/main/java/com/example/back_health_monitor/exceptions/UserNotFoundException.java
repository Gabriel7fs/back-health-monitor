package com.example.back_health_monitor.exceptions;

public class UserNotFoundException  extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
