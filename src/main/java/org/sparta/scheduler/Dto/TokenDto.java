package org.sparta.scheduler.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TokenDto {
    private String token;


    public TokenDto(String token) { // 생성자 추가
        this.token = token;

    }
}
