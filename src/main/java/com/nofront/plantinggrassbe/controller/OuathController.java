package com.nofront.plantinggrassbe.controller;

import com.nofront.plantinggrassbe.DTO.TokenRefreshRequestDto;
import com.nofront.plantinggrassbe.DTO.TokenRefreshResponseDto;
import com.nofront.plantinggrassbe.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OuathController {
    @Autowired
    private TokenService tokenService;

    @PostMapping("/refresh")
    public TokenRefreshResponseDto refreshToken(
            @RequestBody TokenRefreshRequestDto requestBody
    ) throws Exception {
        return tokenService.refreshToken(requestBody);
    }

}
