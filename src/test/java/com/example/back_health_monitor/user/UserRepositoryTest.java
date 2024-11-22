package com.example.back_health_monitor.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setCpf("12345678901");
        user1.setUsername("user1");
        user1.setType(UserType.PACIENT);

        user2 = new User();
        user2.setCpf("98765432109");
        user2.setUsername("user2");
        user2.setType(UserType.MONITOR);

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void testFindAllByCpfIn() {
        Set<String> cpfSet = Set.of("12345678901", "98765432109");
        List<User> foundUsers = userRepository.findAllByCpfIn(cpfSet);

        assertEquals(2, foundUsers.size());
        assertTrue(foundUsers.contains(user1));
        assertTrue(foundUsers.contains(user2));
    }

    @Test
    void testFindByUsername() {
        Optional<User> foundUser = userRepository.findByUsername("user1");

        assertTrue(foundUser.isPresent());
        assertEquals("12345678901", foundUser.get().getCpf());
    }

    @Test
    void testFindByCpf() {
        Optional<User> foundUser = userRepository.findByCpf("12345678901");

        assertTrue(foundUser.isPresent());
        assertEquals("user1", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsername_NotFound() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindByCpf_NotFound() {
        Optional<User> foundUser = userRepository.findByCpf("00000000000");

        assertFalse(foundUser.isPresent());
    }
}
