package com.example.back_health_monitor.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.back_health_monitor.heartbeat.Heartbeat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf", unique = true)
    private String cpf;

    private String address;

    private Long emergencyContact;

    @Column(name = "username", unique = true)
    private String username;

    private String password;

    private LocalDate birthDate;

    private UserType type;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_associateds",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "associated_id")
    )
    @JsonIgnore
    private List<User> associateds = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Heartbeat> heartbeats = new ArrayList<>();

    public Collection<GrantedAuthority> getAuthorities() {
        if (this.type == UserType.MONITOR) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", cpf='" + cpf + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", emergencyContact=" + emergencyContact +
                ", birthDate=" + birthDate +
                ", type=" + type +
                '}';
    }

}
