package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.RoleType;
import com.nofront.plantinggrassbe.domain.User;
import lombok.Setter;

@Setter
public class UserRegisterRequestDto implements IRequestDto<User, Object>{
    String id;
    String name;
    String password;
    String roleType;

    @Override
    public User toEntity(Object object){
        return User.builder()
                .username(id)
                .provider(password)
                .name(name)
                .roleType(RoleType.Guest)
                .build()
                ;
    }
}
