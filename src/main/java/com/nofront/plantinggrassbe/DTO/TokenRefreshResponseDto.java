package com.nofront.plantinggrassbe.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TokenRefreshResponseDto implements IResponseDto<Object, Object>{

    private String accessToken;

    private String refreshToken;

}
