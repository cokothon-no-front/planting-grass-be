package com.nofront.plantinggrassbe.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringResponseDto {
    private final String response;
    public StringResponseDto(String response) {
        this.response = response;
    }
}
