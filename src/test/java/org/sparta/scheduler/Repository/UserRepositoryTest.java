//package org.sparta.scheduler.Repository;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.Optional;
//import org.junit.jupiter.api.Test;
//import org.sparta.scheduler.Domain.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//@DataJpaTest
//public class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void findByUsername_WhenUserExists_ReturnsUser() {
//        // Given
//        String username = "testUser";
//        String password = "testPass";
//        User newUser = new User(username, password);
//        userRepository.save(newUser);
//
//        // When
//        Optional<User> foundUser = userRepository.findByUsername(username);
//
//        // Then
//        assertTrue(foundUser.isPresent());
//        assertEquals(username, foundUser.get().getUsername());
//    }
//
//    @Test
//    public void findByUsername_WhenUserDoesNotExist_ReturnsEmpty() {
//        // Given
//        String username = "nonExistingUser";
//
//        // When
//        Optional<User> foundUser = userRepository.findByUsername(username);
//
//        // Then
//        assertFalse(foundUser.isPresent());
//    }
//
//    @Test
//    public void existsByUsername_WhenUserExists_ReturnsTrue() {
//        // Given
//        String username = "testUser";
//        String password = "testPass";
//        User newUser = new User(username, password);
//        userRepository.save(newUser);
//
//        // When
//        boolean exists = userRepository.existsByUsername(username);
//
//        // Then
//        assertTrue(exists);
//    }
//
//    @Test
//    public void existsByUsername_WhenUserDoesNotExist_ReturnsFalse() {
//        // Given
//        String username = "nonExistingUser";
//
//        // When
//        boolean exists = userRepository.existsByUsername(username);
//
//        // Then
//        assertFalse(exists);
//    }
//}
