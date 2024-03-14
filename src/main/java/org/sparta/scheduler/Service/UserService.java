package org.sparta.scheduler.Service;

import java.util.Optional;
import java.util.regex.Pattern;
import org.sparta.scheduler.Domain.User;
import org.sparta.scheduler.Dto.LoginRequestDto;
import org.sparta.scheduler.Dto.TokenDto;
import org.sparta.scheduler.Exception.InvalidLoginCredentialsException;
import org.sparta.scheduler.Exception.UsernameAlreadyExistsException;
import org.sparta.scheduler.Repository.UserRepository;
import org.sparta.scheduler.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입 로직
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void registerUser(String username, String password) {
        // 비밀번호 유효성 검사 패턴
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,15}$";

        // 비밀번호가 패턴에 맞지 않는 경우
        if (!Pattern.matches(passwordPattern, password)) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 구성되어야 합니다.");
        }

        // 사용자 이름이 이미 존재하는 경우
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("이미 존재하는 사용자 이름입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword);
        userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public TokenDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new InvalidLoginCredentialsException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidLoginCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return new TokenDto(token);
    }

}
