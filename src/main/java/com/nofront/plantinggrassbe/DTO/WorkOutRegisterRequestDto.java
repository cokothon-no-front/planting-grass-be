package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.WorkOut;
import lombok.Setter;

@Setter
public class WorkOutRegisterRequestDto implements IRequestDto<WorkOut, Object> {
    private String bodyPart;

    private String machineName;

    private String name;

    @Override
    public WorkOut toEntity(){
        return WorkOut.builder()
                .bodyPart(bodyPart)
                .machineName(machineName)
                .name(name)
                .build();
    }
}
