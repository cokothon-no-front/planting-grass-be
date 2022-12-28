package com.nofront.plantinggrassbe.controller;

import com.nofront.plantinggrassbe.DTO.UserDetails;
import com.nofront.plantinggrassbe.DTO.UserSaveResponseDto;
import com.nofront.plantinggrassbe.domain.User;
import com.nofront.plantinggrassbe.domain.UserSave;
import com.nofront.plantinggrassbe.filter.JwtAuthenticationToken;
import com.nofront.plantinggrassbe.service.UserSaveService;
import com.nofront.plantinggrassbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserSaveController {

    @Autowired
    public UserSaveController(UserSaveService userSaveService) {
        this.userSaveService = userSaveService;
    }

    private final UserSaveService userSaveService;

    @GetMapping("/user/save")
    public List<UserSave> getUserSave(JwtAuthenticationToken jwtToken){
        return userSaveService.findAllByUserName(jwtToken);
    }

}
