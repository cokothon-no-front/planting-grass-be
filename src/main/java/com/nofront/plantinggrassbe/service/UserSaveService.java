package com.nofront.plantinggrassbe.service;

import com.nofront.plantinggrassbe.DTO.UserDetails;

import com.nofront.plantinggrassbe.DTO.UserSaveRequestDto;

import com.nofront.plantinggrassbe.DTO.UserSaveResponseDto;
import com.nofront.plantinggrassbe.domain.User;
import com.nofront.plantinggrassbe.domain.UserSave;
import com.nofront.plantinggrassbe.filter.JwtAuthenticationToken;
import com.nofront.plantinggrassbe.repository.UserRepository;
import com.nofront.plantinggrassbe.repository.UserSaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Transactional
@Service
@PropertySource("classpath:application.properties")
public class UserSaveService {

    @Autowired

    private  UserSaveRepository userSaveRepository;
    @Autowired
    private UserRepository userRepository;

    public UserSaveResponseDto returnUserSave(UserSave usersave, User user){
        List<UserSave> userSave = userSaveRepository.findAll();
        return new UserSaveResponseDto().fromEntity(usersave,user);
    }

    public UserSave findById(int id) {
        return userSaveRepository.findById(id).get();
    }

    public List<UserSaveResponseDto> findAllByUserName(JwtAuthenticationToken jwtToken) {
        UserDetails userDetails = (UserDetails) jwtToken.getPrincipal();
        User user =  userRepository.findByUsernameAndProvider(userDetails.getUsername(), userDetails.getProvider()).orElseThrow(() -> new RuntimeException("can not find user!"));
        List<UserSave> userSave = userSaveRepository.findByUsername(user.getUsername());

        List<UserSaveResponseDto> responseList = new ArrayList<>();
        Stream<UserSave> stream = userSave.stream();
        stream.forEach(data -> {
            responseList.add(new UserSaveResponseDto().fromEntity(data, user));
        });
        return responseList;
    }
    public UserSaveResponseDto join(UserSaveRequestDto request, JwtAuthenticationToken jwtToken){
        UserDetails userDetails = (UserDetails) jwtToken.getPrincipal();
        User user =  userRepository.findByUsernameAndProvider(userDetails.getUsername(), userDetails.getProvider()).orElseThrow(() -> new RuntimeException("can not find user!"));
        UserSave userSave = request.toEntity(user);
        userSaveRepository.save(userSave);
        return new UserSaveResponseDto().fromEntity(userSave, user);
    }

}


