package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.User;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto implements IResponseDto<UserResponseDto, User>{
    private String nickname;

    private boolean isNewbie;

    private String gender;

    private Float height;

    private Float weight;

    private Float bodyFatPer;

    private Float musclePer;

    @Override
    public UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .gender(user.getGender())
                .height(user.getHeight())
                .weight(user.getWeight())
                .bodyFatPer(user.getBodyFatPer())
                .musclePer(user.getMusclePer())
                .nickname(user.getNickname())
                .isNewbie(user.isNewbie())
                .build();
    }
}
