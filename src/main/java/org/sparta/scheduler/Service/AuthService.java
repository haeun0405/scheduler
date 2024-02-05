package org.sparta.scheduler.Service;

import org.sparta.scheduler.Dto.LoginRequestDto;
import org.sparta.scheduler.Dto.SignupRequestDto;
import org.sparta.scheduler.Dto.TokenDto;

public interface AuthService {
    void signup(SignupRequestDto signupRequestDto);
    TokenDto login(LoginRequestDto loginRequestDto);
}
