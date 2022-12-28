package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.User;
import lombok.Setter;

@Setter
public class UserRegisterRequestDto implements IRequestDto<User, Object>{
    private String nickname;

    private boolean isNewbie;

    private String gender;

    private Float height;

    private Float weight;

    private Float bodyFatPer;

    private Float musclePer;

    @Override
    public User toEntity(Object object) {
        return User.builder()

                .build();
    }
}
/*
닉네임
경력
성별
키
몸무게
체지방율
근육량
 */