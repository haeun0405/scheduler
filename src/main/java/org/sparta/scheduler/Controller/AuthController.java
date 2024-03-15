package org.sparta.scheduler.Controller;

import jakarta.servlet.http.HttpServletResponse;
import org.sparta.scheduler.Dto.LoginRequestDto;
import org.sparta.scheduler.Dto.SignupRequestDto;
import org.sparta.scheduler.Dto.TokenDto;
import org.sparta.scheduler.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService  authService) {
        this.authService  = authService;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequestDto signupRequestDto) {
        authService.registerUser(signupRequestDto.getUsername(), signupRequestDto.getPassword());
        return ResponseEntity.ok("회원가입 성공");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        TokenDto token = authService.login(loginRequestDto);
        response.setHeader("Authorization", "Bearer " + token.getToken());
        return ResponseEntity.ok(token);
    }
}
