package com.nofront.plantinggrassbe.controller;

import com.nofront.plantinggrassbe.DTO.StringResponseDto;
import com.nofront.plantinggrassbe.DTO.TokenRefreshRequestDto;
import com.nofront.plantinggrassbe.DTO.TokenRefreshResponseDto;
import com.nofront.plantinggrassbe.Utils.JwtTokenUtils;
import com.nofront.plantinggrassbe.Utils.KakaoTokenUtils;
import com.nofront.plantinggrassbe.Utils.OauthInfo;
import com.nofront.plantinggrassbe.service.OAuthService;
import com.nofront.plantinggrassbe.service.TokenService;
import com.nofront.plantinggrassbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OuathController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private KakaoTokenUtils kakaoTokenUtils;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserService userService;

    @PostMapping("/refresh")
    public TokenRefreshResponseDto refreshToken(
            @RequestBody TokenRefreshRequestDto requestBody
    ) throws Exception {
        return tokenService.refreshToken(requestBody);
    }

    @ResponseBody
    @GetMapping("/oauth2/kakao")
    public StringResponseDto GetKakaoToken(
            @RequestParam String code
    ) throws Exception {
//        System.out.println("code = " + code);
        String token = oAuthService.getKakaoAccessToken(code);
//        System.out.println("token = " + token);
        OauthInfo oauthInfo = kakaoTokenUtils.getOauthInfo(token);

        String accessToken = jwtTokenUtils.generateAccessToken(oauthInfo.getUserId(), oauthInfo.getProvider());
        String refreshToken = jwtTokenUtils.generateRefreshToken();
        userService.saveToken(oauthInfo.getUserId(), oauthInfo.getProvider(), refreshToken);
        return new StringResponseDto(accessToken);
    }

}
//kauth.kakao.com/oauth/authorize?client_id=6c9dfbb8dc3c0a2f2056d8bc5ffdd0ef&redirect_uri=http://localhost:8080/oauth2/kakao&response_type=code