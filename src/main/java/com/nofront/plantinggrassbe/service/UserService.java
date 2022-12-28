package com.nofront.plantinggrassbe.service;

import com.nofront.plantinggrassbe.DTO.UserDetails;
import com.nofront.plantinggrassbe.DTO.UserRegisterRequestDto;
import com.nofront.plantinggrassbe.DTO.UserResponseDto;
import com.nofront.plantinggrassbe.DTO.UserSaveResponseDto;
import com.nofront.plantinggrassbe.Utils.CommonTokenUtils;
import com.nofront.plantinggrassbe.Utils.JwtTokenUtils;
import com.nofront.plantinggrassbe.Utils.KakaoTokenUtils;
import com.nofront.plantinggrassbe.domain.RoleType;
import com.nofront.plantinggrassbe.domain.User;

import com.nofront.plantinggrassbe.domain.UserSave;

import com.nofront.plantinggrassbe.filter.JwtAuthenticationToken;
import com.nofront.plantinggrassbe.repository.UserRepository;

import com.auth0.jwk.JwkException;
import com.nofront.plantinggrassbe.repository.UserSaveRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

@Transactional
@Service
@PropertySource("classpath:application.properties")
public class UserService {

    // use env to get kakao native key from application.properties
    @Autowired
    private Environment env;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommonTokenUtils tokenUtils;
    @Autowired
    private KakaoTokenUtils kakaoTokenUtils;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;


    //
    public List<User> findUsers() throws Exception {
        if (userRepository.findAll().isEmpty()){
            throw new Exception("result set null");
        }
        return userRepository.findAll();
    }

    public void storeToken(String tokens) throws ParseException {

        JSONParser parser = new JSONParser();
        JSONObject tokenObj = (JSONObject) parser.parse(tokens);
        String idToken = (String) tokenObj.get("IdToken");

        String[] jwt = idToken.split("\\.");

        byte[] decodedBytes = Base64.getDecoder().decode(jwt[1]);
        String payload = new String(decodedBytes);

//        sessions.put("", idToken);
        System.out.println("payload = " + payload);
    }

    public boolean checkKakaoToken(String token) throws ParseException, JwkException {
        return kakaoTokenUtils.validate(token);
    }
    public UserResponseDto returnUser(JwtAuthenticationToken jwtToken){
        UserDetails userDetails = (UserDetails) jwtToken.getPrincipal();
        User user =  userRepository.findByUsernameAndProvider(userDetails.getUsername(), userDetails.getProvider()).orElseThrow(() -> new RuntimeException("can not find user!"));
        return new UserResponseDto().fromEntity(user);
    }
//    public UserResponseDto join(UserRegisterRequestDto request) throws ParseException {
//        User user = request.toEntity(null);
//        String accessToken = jwtTokenUtils.generateAccessToken(user.getUsername(), user.getProvider());
//        String refreshToken = jwtTokenUtils.generateRefreshToken();
//        userService.saveToken(oauthInfo.getUserId(), oauthInfo.getProvider(), refreshToken);
//
//        User user = request.toEntity(null);
//        user.setProvider(userBefore.getProvider());
//        user.setUsername(userBefore.getUsername());
//        user.setRefreshToken(userBefore.getRefreshToken());
//        user.setId(userBefore.getId());
//
//        userRepository.save(user);
//    }

//    public UserResponseDto findUserByNickname(String nickname) throws Exception {
//        User user = userRepository.findByNickname(nickname).orElseThrow(() -> new Exception("error find by nickname"));
//        return new UserResponseDto().fromEntity(user);
//    }

//    public UserResponseDto findUserByToken(String token) throws Exception {
//        User user = userRepository.findByRefreshToken(token).orElseThrow(() -> new Exception("error find by refreshToken"));
//        return new UserResponseDto().fromEntity(user);
//    }

    public void saveToken(String username, String provider, String refreshToken, String nickname) throws  Exception{
        User user = userRepository.findByUsernameAndProvider(username, provider).isPresent() ?
                userRepository.findByUsernameAndProvider(username, provider).orElseThrow(() -> new Exception("error find by nickname")) :
                User.builder()
                        .username(username)
                        .provider(provider)
                        .roleType(RoleType.Guest)
                        .name(nickname)
                        .build();
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    public UserResponseDto findById(Long id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("error find by nickname"));
        return new UserResponseDto().fromEntity(user);
    }
    public String checkAndToken(String id, String password) throws Exception {
        User user = userRepository.findByUsernameAndProvider(id, password).orElseThrow(() -> new Exception("error find by nickname"));
        String accessToken = jwtTokenUtils.generateAccessToken(user.getUsername(), user.getProvider());
        return accessToken;
    }
    public UserResponseDto join(UserRegisterRequestDto requestBody) throws Exception {
        User user = requestBody.toEntity(null);
        String accessToken = jwtTokenUtils.generateAccessToken(user.getUsername(), user.getProvider());
        String refreshToken = jwtTokenUtils.generateRefreshToken();
        saveToken(user.getUsername(), user.getProvider(), refreshToken, user.getName());
        return new UserResponseDto().fromEntity(user);
    }
}
