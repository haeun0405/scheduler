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
public class UserServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입 로직
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 사용자 등록 로직을 수행합니다. 비밀번호 유효성 검사와 사용자명 중복 검사를 포함합니다.
     *
     * @param username 사용자의 이름.
     * @param password 사용자의 비밀번호.
     * @throws UsernameAlreadyExistsException 사용자 이름이 이미 존재하는 경우 예외 발생.
     * @throws IllegalArgumentException 비밀번호가 정해진 패턴에 맞지 않을 경우 예외 발생.
     */
    @Override
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

    /**
     * 주어진 사용자 이름으로 사용자를 찾습니다.
     *
     * @param username 찾고자 하는 사용자의 이름.
     * @return Optional<User> 사용자 객체를 담고 있는 Optional 객체.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 로그인 요청을 처리합니다. 사용자 인증 후 JWT 토큰을 생성합니다.
     *
     * @param loginRequestDto 로그인 요청 정보를 담은 DTO.
     * @return TokenDto 생성된 JWT 토큰 정보를 담은 DTO.
     * @throws InvalidLoginCredentialsException 사용자 이름이 존재하지 않거나 비밀번호가 일치하지 않는 경우.
     */
    @Override
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
