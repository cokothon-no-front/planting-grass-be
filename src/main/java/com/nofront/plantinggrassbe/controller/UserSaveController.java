package com.nofront.plantinggrassbe.controller;

import com.nofront.plantinggrassbe.DTO.StringResponseDto;
import com.nofront.plantinggrassbe.DTO.UserRegisterRequestDto;
import com.nofront.plantinggrassbe.DTO.UserSaveRequestDto;
import com.nofront.plantinggrassbe.DTO.UserSaveResponseDto;
import com.nofront.plantinggrassbe.domain.UserSave;
import com.nofront.plantinggrassbe.filter.JwtAuthenticationToken;
import com.nofront.plantinggrassbe.repository.UserSaveRepository;
import com.nofront.plantinggrassbe.service.UserSaveService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSaveController {

    private final UserSaveService userSaveService;

    @Autowired
    UserSaveController(UserSaveService userSaveService){this.userSaveService = userSaveService;}

    @PostMapping("/user/save")
    public UserSaveResponseDto registerUser(
            JwtAuthenticationToken jwtToken,
            @RequestBody UserSaveRequestDto requestBody
    ) throws ParseException {
        return userSaveService.join(requestBody, jwtToken);

    }
    
    @GetMapping("/user/save")
    public List<UserSave> getUserSave(JwtAuthenticationToken jwtToken){
        return userSaveService.findAllByUserName(jwtToken);
    }
}
