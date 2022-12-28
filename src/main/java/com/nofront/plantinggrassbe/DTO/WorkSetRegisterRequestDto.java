package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.SubRoutine;
import com.nofront.plantinggrassbe.domain.WorkOut;
import com.nofront.plantinggrassbe.domain.WorkSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkSetRegisterRequestDto implements IRequestDto<WorkSet, SubRoutine> {
    private String workOutName;
    private long subRoutineId;

    public WorkSet toEntity(SubRoutine subRoutine, WorkOut workOut) {
        return WorkSet.builder()
                .workOut(workOut)
                .subRoutine(subRoutine)
                .build();
    }
}
