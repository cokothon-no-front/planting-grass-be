package com.nofront.plantinggrassbe.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponseDto {
    private final String token;
    public TokenResponseDto(String token) {
        this.token = token;
    }
}

