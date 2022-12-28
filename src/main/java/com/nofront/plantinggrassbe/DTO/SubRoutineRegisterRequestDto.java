package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.Routine;
import com.nofront.plantinggrassbe.domain.SubRoutine;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubRoutineRegisterRequestDto implements IRequestDto<SubRoutine, Routine>{

    private String subRoutineName;
    private Long routineId;

    @Override
    public SubRoutine toEntity(Routine routine) {
        return SubRoutine.builder()
                .routine(routine)
                .name(subRoutineName)
                .build();
    }
}
