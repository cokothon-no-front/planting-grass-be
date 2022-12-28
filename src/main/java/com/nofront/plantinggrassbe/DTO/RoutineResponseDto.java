package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.Routine;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoutineResponseDto implements IResponseDto<RoutineResponseDto, Routine>{

    private String username;

    private String routineName;

    private Long routineId;

    private String description;

    private String category;

    @Override
    public RoutineResponseDto fromEntity(Routine routine) {
        return RoutineResponseDto.builder()
                .description(routine.getDescription())
                .routineId(routine.getId())
                .username(routine.getUser().getNickname())
                .routineName(routine.getName())
                .category(routine.getCategory())
                .build()
                ;
    }

}
