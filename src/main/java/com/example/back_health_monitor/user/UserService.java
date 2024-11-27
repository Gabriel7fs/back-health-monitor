package com.example.back_health_monitor.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.example.back_health_monitor.exceptions.UserNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void createUser(UserCreateDTO dto) {
        User user;

        switch (dto.getType()) {
            case PACIENT:
                user = buildPacientUser(dto);
                break;
            case MONITOR:
                user = buildMonitorUser(dto);
                break;
            default:
                throw new IllegalArgumentException("Invalid user type");
        }

        user = this.repository.save(user);

        List<User> users = this.repository.findAllByCpfIn(dto.getCpfAssociateds());

        if (!CollectionUtils.isEmpty(users)) {
            user.setAssociateds(users);

            for (User associatedUser : users) {
                List<User> associatedsList = associatedUser.getAssociateds();
                if (!associatedsList.contains(user)) {
                    associatedsList.add(user);
                }
            }

            this.repository.save(user);
            users.forEach(this.repository::save);
        }
    }

    private User buildMonitorUser(UserCreateDTO dto) {
        User user = new User();

        user.setCpf(dto.getCpf());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUsername(dto.getUsername());
        user.setType(dto.getType());
        user.setAddress(dto.getAddress());

        return user;
    }

    private User buildPacientUser(UserCreateDTO dto) {
        User user = new User();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDate = LocalDate.parse(dto.getBirthdate(), formatter);

        user.setBirthDate(birthDate);
        user.setCpf(dto.getCpf());
        user.setEmergencyContact(dto.getEmergencyContact());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUsername(dto.getUsername());
        user.setType(dto.getType());
        user.setAddress(dto.getAddress());

        return user;
    }

    public void updateUser(UserCreateDTO dto) {
        Optional<User> optUser = this.repository.findById(dto.getId());
        if (optUser.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado.");
        }

        User user = optUser.get();

        if ("PACIENT".equals(user.getType().name())) {
            if (dto.getBirthdate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate birthDate = LocalDate.parse(dto.getBirthdate(), formatter);
                user.setBirthDate(birthDate);
            }

            if (dto.getEmergencyContact() != null) {
                user.setEmergencyContact(dto.getEmergencyContact());
            }
        }

        if (dto.getCpf() != null) {
            user.setCpf(dto.getCpf());
        }

        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }

        if (dto.getAddress() != null) {
            user.setAddress(dto.getAddress());
        }

        this.repository.save(user);
    }

    public void changePassword(UserCreateDTO dto) {
        Optional<User> optUser = this.repository.findByCpf(dto.getCpf());
        if(optUser.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado.");
        }

        User user = optUser.get();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
    }
}
