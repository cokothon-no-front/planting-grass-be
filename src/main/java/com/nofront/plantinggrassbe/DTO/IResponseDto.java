package com.nofront.plantinggrassbe.DTO;

public interface IResponseDto<R, T> {
     default R fromEntity() {
          return null;
     }
     default  R fromEntity(T args){
          return null;
     }

}
