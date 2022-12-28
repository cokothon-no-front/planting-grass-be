package com.nofront.plantinggrassbe.DTO;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StaticResponseDto implements IResponseDto<StaticResponseDto, String>{
    private String uri;

    @Override
    public StaticResponseDto fromEntity(String uri){
        return StaticResponseDto.builder()
                .uri(uri)
                .build()
                ;
    }
}
