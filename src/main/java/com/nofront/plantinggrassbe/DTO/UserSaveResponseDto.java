package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.User;
import com.nofront.plantinggrassbe.domain.UserSave;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveResponseDto {
    private LocalDateTime createdDate;

    private String data;

    private String dataKey;

    private int id;

    private boolean isPrivate;

    private String username;



    public UserSaveResponseDto fromEntity(UserSave userSave){

        return UserSaveResponseDto.builder()
                .createdDate(userSave.getCreatedDate())
                .data(userSave.getData())
                .dataKey(userSave.getDataKey())
                .id(userSave.getId())
                .isPrivate(userSave.isPrivate())
                .username(userSave.getUsername())
                .build();

    }
}
