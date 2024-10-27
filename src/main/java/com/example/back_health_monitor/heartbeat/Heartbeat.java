package com.example.back_health_monitor.heartbeat;

import java.time.LocalDateTime;

import com.example.back_health_monitor.user.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "heartbeat")
public class Heartbeat {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float heartbeat;

    private float oxygenQuantity;

    private LocalDateTime date;

    @ManyToOne
    @JsonIgnore
    private User user;
}
