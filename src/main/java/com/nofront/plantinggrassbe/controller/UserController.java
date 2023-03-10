package com.nofront.plantinggrassbe.controller;

import com.nofront.plantinggrassbe.DTO.*;

import com.nofront.plantinggrassbe.domain.User;
import com.nofront.plantinggrassbe.filter.JwtAuthenticationToken;
import com.nofront.plantinggrassbe.service.UserService;
import com.auth0.jwk.JwkException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public StringResponseDto home(){
        return new StringResponseDto("Server Running!!");
    }

    @GetMapping("/session")
    public String session(ObjectMapper mapper) throws JsonProcessingException {
        return mapper.writeValueAsString(SecurityContextHolder.getContext().getAuthentication());
    }

    @GetMapping("/users")
    public List<User> userList(){
        try {
            return userService.findUsers();
        }
        catch (Exception e){
            return null;
        }
    }
    @PostMapping("/user/sign-up")
    public UserResponseDto signUp(
            @RequestBody UserRegisterRequestDto requestBody
    ) throws Exception {
        return userService.join(requestBody);
    }

    @PostMapping("/user/sign-in")
    public TokenResponseDto signIn(
            @RequestBody SignInRequestDto requestBody
    ) throws Exception {
        String token = userService.checkAndToken(requestBody.getId(), requestBody.getPassword());
        return new TokenResponseDto(token);
    }

    @GetMapping("/user")
    public UserResponseDto getUser(JwtAuthenticationToken jwtToken){
        return userService.returnUser(jwtToken);
    }


    @GetMapping("/token")
    public boolean checkToken(@RequestParam("token") String token){
        System.out.println("Token : ");
        System.out.println(token);
        try {
            return userService.checkKakaoToken(token);

        } catch (ParseException | JwkException e) {
            System.out.println("error! = " + e);
            throw new RuntimeException(e);
        }
    }

//    @PostMapping("/register")
//    public StringResponseDto registerUser(
//            @RequestBody UserRegisterRequestDto requestBody
//            ) throws ParseException {
//        userService.join(requestBody);
//        return new StringResponseDto("User Registered!!");
//    }


//    @GetMapping("/user/refreshToken")
//    public UserResponseDto findUserByToken(@RequestParam(value = "refreshToken") String token){
//        try {
//            return userService.findUserByToken(token);
//        }
//        catch (Exception e){
//            System.out.println("e = " + e);
//            return null;
//        }
//    }
//



//    @GetMapping("/user/save/{id}")
//    public UserResponseDto findUserById(@PathVariable Long id){
//        return UserService.findById(id);
//    }



}
