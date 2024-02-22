package org.sparta.scheduler.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sparta.scheduler.Domain.User;
import org.sparta.scheduler.Dto.LoginRequestDto;
import org.sparta.scheduler.Dto.TokenDto;
import org.sparta.scheduler.Repository.UserRepository;
import org.sparta.scheduler.Util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void registerUser_Success() {
        // Given
        String username = "testUser";
        String password = "Password123!";
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        // When
        userService.registerUser(username, password);

        // Then
        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_Success() {
        // Given
        String username = "testUser";
        String password = "Password123!";
        User user = new User(username, passwordEncoder.encode(password));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(username)).thenReturn("token");

        // When
        TokenDto result = userService.login(new LoginRequestDto(username, password));

        // Then
        assertNotNull(result);
        assertEquals("token", result.getToken());
    }
}
