package org.sparta.scheduler.Service;

import java.util.Optional;
import org.sparta.scheduler.Domain.User;
import org.sparta.scheduler.Dto.LoginRequestDto;
import org.sparta.scheduler.Dto.TokenDto;
import org.sparta.scheduler.Exception.InvalidLoginCredentialsException;
import org.sparta.scheduler.Exception.UsernameAlreadyExistsException;

public interface AuthService {
    /**
     * 사용자를 등록합니다.
     *
     * @param username 사용자 이름
     * @param password 사용자 비밀번호
     * @throws UsernameAlreadyExistsException 이미 존재하는 사용자 이름일 경우
     * @throws IllegalArgumentException 비밀번호가 유효성 검사 패턴에 맞지 않을 경우
     */
    void registerUser(String username, String password) throws UsernameAlreadyExistsException;
    /**
     * 사용자 이름으로 사용자를 찾습니다.
     *
     * @param username 사용자 이름
     * @return Optional<User> 사용자 객체
     */
    Optional<User> findByUsername(String username);
    /**
     * 로그인을 수행하고 토큰을 반환합니다.
     *
     * @param loginRequestDto 로그인 요청 데이터 전송 객체
     * @return TokenDto 토큰 데이터 전송 객체
     * @throws InvalidLoginCredentialsException 로그인 자격 증명이 유효하지 않을 경우
     */
    TokenDto login(LoginRequestDto loginRequestDto);

}
