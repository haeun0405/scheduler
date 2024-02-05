package org.sparta.scheduler.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginRequestDto {
    private String username;
    private String password;

    // 기본 생성자
    public LoginRequestDto() {}

    // 생성자
    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
