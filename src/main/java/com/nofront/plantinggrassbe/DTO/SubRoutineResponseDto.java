package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.SubRoutine;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubRoutineResponseDto implements IResponseDto<SubRoutineResponseDto, SubRoutine> {

    private String routineName;

    private Long subRoutineId;

    private String subRoutineName;

    @Override
    public SubRoutineResponseDto fromEntity(SubRoutine subRoutine) {
        return SubRoutineResponseDto.builder()
                .subRoutineId(subRoutine.getId())
                .routineName(subRoutine.getRoutine().getName())
                .subRoutineName(subRoutine.getName())
                .build()
                ;
    }
}
