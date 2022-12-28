package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.WorkSet;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkSetResponseDto implements IResponseDto<WorkSetResponseDto, WorkSet> {

    private Long workSetId;

    private String subRoutineName;

    private String workOutName;

    @Override
    public WorkSetResponseDto fromEntity(WorkSet workSet){
        return WorkSetResponseDto.builder()
                .workSetId(workSet.getId())
                .workOutName(workSet.getWorkOut().getName())
                .subRoutineName(workSet.getSubRoutine().getName())
                .build();
    }

}
