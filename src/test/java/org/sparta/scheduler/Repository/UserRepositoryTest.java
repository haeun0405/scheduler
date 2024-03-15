package org.sparta.scheduler.Repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sparta.scheduler.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private TestEntityManager entityManager;

    private String uniqueUsername;

    @BeforeEach
    void setUp() {
        uniqueUsername = "testUser_" + UUID.randomUUID().toString();
    }

    @Test
    void findByUsername_WhenUserExists_ReturnsUser() {
        // given
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueUsername = "user" + timestamp.substring(timestamp.length() - 5); // 마지막 5자리만 사용
        User newUser = User.builder().username(uniqueUsername).password("testPass").build();
        entityManager.persistAndFlush(newUser);

        // when
        User foundUser = userRepository.findByUsername(uniqueUsername).orElse(null);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(uniqueUsername);
    }

    @Test
    public void findByUsername_WhenUserDoesNotExist_ReturnsEmpty() {
        // Given
        String uniqueUsername = "nonExisting" + System.currentTimeMillis();

        // When
        Optional<User> foundUser = userRepository.findByUsername(uniqueUsername);

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void existsByUsername_WhenUserExists_ReturnsTrue() {
        // Given
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueUsername = "user" + timestamp.substring(timestamp.length() - 5);
        String password = "testPass";
        User newUser = User.builder().username(uniqueUsername).password(password).build();
        userRepository.save(newUser);

        // When
        boolean exists = userRepository.existsByUsername(uniqueUsername);

        // Then
        assertTrue(exists);
    }

    @Test
    public void existsByUsername_WhenUserDoesNotExist_ReturnsFalse() {
        // Given
        String uniqueUsername = "userDoesNotExist" + System.currentTimeMillis();

        // When
        boolean exists = userRepository.existsByUsername(uniqueUsername);

        // Then
        assertFalse(exists);
    }
}
