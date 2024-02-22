package org.sparta.scheduler.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sparta.scheduler.Domain.User;
import org.sparta.scheduler.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        // 테스트에 사용할 User 인스턴스 초기화
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
    }

    @Test
    void loadUserByUsername_Success() {
        // UserRepository의 findByUsername 메소드가 "testUser"에 대해 user를 반환하도록 설정
        when(userRepository.findByUsername("testUser")).thenReturn(java.util.Optional.of(user));

        // loadUserByUsername 메소드 실행
        UserDetails result = customUserDetailsService.loadUserByUsername("testUser");

        // 결과 검증
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        // UserRepository의 findByUsername 메소드가 "unknownUser"에 대해 빈 Optional을 반환하도록 설정
        when(userRepository.findByUsername("unknownUser")).thenReturn(java.util.Optional.empty());

        // loadUserByUsername 메소드 실행 시 UsernameNotFoundException이 발생하는지 검증
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("unknownUser");
        });
    }
}
