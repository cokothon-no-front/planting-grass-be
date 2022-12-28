package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.UserSave;

public interface IResponseDto<R, T> {
     default R fromEntity() {
          return null;
     }
     default  R fromEntity(T args){
          return null;
     }
}
