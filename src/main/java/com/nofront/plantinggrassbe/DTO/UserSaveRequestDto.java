package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.User;
import com.nofront.plantinggrassbe.domain.UserSave;
import com.nofront.plantinggrassbe.domain.UserSaveData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSaveRequestDto {
    UserSaveData data;
    String name;

    public UserSave toEntity(User user){
        return UserSave.builder()
                .data(this.data.getData())
                .dataKey(this.data.getDataKey())
                .isPrivate(this.data.isPrivate())
                .user(user)
                .build()
                ;
    }
}
