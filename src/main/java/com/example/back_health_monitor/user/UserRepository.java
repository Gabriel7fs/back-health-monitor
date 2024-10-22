package com.example.back_health_monitor.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByCpfIn(Set<String> cpfAssociateds);

    Optional<User> findByUsername(String user);

    Optional<User> findByCpf(String cpf);
}

