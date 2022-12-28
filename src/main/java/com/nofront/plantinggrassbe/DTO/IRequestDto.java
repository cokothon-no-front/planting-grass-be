package com.nofront.plantinggrassbe.DTO;

public interface IRequestDto<R, T> {
    default R toEntity(T args){
        return null;
    };
    default R toEntity(){
        return null;
    };
}
