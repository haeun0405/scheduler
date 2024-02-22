package org.sparta.scheduler.Controller;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sparta.scheduler.Dto.LoginRequestDto;
import org.sparta.scheduler.Dto.SignupRequestDto;
import org.sparta.scheduler.Dto.TokenDto;
import org.sparta.scheduler.SecurityConfig.TestSecurityConfig;
import org.sparta.scheduler.Service.UserService;
import org.sparta.scheduler.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUserSuccess() throws Exception {
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername("username");
        signupRequestDto.setPassword("password");

        Mockito.doNothing().when(userService).registerUser(signupRequestDto.getUsername(), signupRequestDto.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("회원가입 성공"));
    }

    @Test
    void loginSuccess() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("username", "password");
        TokenDto tokenDto = new TokenDto("dummy-token"); // 수정된 부분: 예상 값 "dummy-token"으로 변경

        Mockito.when(userService.login(any(LoginRequestDto.class))).thenReturn(tokenDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("dummy-token")); // 수정된 부분: 예상 값 "dummy-token"으로 변경
    }
}
