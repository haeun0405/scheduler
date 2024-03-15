package org.sparta.scheduler.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.sparta.scheduler.Dto.LoginRequestDto;
import org.sparta.scheduler.Dto.SignupRequestDto;
import org.sparta.scheduler.Dto.TokenDto;
import org.sparta.scheduler.Repository.UserRepository;
import org.sparta.scheduler.Service.UserServiceImpl;
import org.sparta.scheduler.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUserSuccess() throws Exception {
        // Given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername("newuser");
        signupRequestDto.setPassword("password123");

        // When & Then
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestDto)))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("회원가입 성공"));
    }

    @Test
    void loginSuccess() throws Exception {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto("username", "password");
        TokenDto tokenDto = new TokenDto("dummy-token"); // 수정된 부분: 예상 값 "dummy-token"으로 변경

        Mockito.when(userService.login(any(LoginRequestDto.class))).thenReturn(tokenDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.token")
                .value("dummy-token")); // 수정된 부분: 예상 값 "dummy-token"으로 변경
    }
}

