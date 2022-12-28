package com.nofront.plantinggrassbe.service;

import com.nofront.plantinggrassbe.DTO.TokenRefreshRequestDto;
import com.nofront.plantinggrassbe.DTO.TokenRefreshResponseDto;
import com.nofront.plantinggrassbe.Utils.JwtTokenUtils;
import com.nofront.plantinggrassbe.domain.User;
import com.nofront.plantinggrassbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TokenService {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto requestBody){

        try{
            User user = userRepository.findByRefreshToken(requestBody.getRefreshToken()).orElseThrow(() -> new Exception("no refreshToken exists"));
            if (!jwtTokenUtils.validate(requestBody.getRefreshToken())) return null;
            String accessToken = jwtTokenUtils.generateAccessToken(user.getUsername(), user.getProvider());
            String refreshToken = jwtTokenUtils.generateRefreshToken();
            userService.saveToken(user.getUsername(), user.getProvider(), refreshToken);

            return TokenRefreshResponseDto.builder()
                    .refreshToken(refreshToken)
                    .accessToken(accessToken)
                    .build()
                    ;
        }catch (Exception e){
            System.out.println("e = " + e);
        }
        return null;
    }
}
