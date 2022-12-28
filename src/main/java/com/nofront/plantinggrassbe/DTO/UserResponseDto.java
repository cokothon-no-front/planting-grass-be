package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.RoleType;
import com.nofront.plantinggrassbe.domain.User;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto implements IResponseDto<UserResponseDto, User>{

    private String authority;

    private Long id;

    private String name;

    private String password;

    private RoleType roleType;

    @Override
    public UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .authority(user.getName())
                .roleType(user.getRoleType())
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}