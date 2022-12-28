package com.nofront.plantinggrassbe.DTO;

import com.nofront.plantinggrassbe.domain.SubSet;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubSetResponseDto implements IResponseDto<SubSetResponseDto, SubSet> {

    private Long subSetId;

    private Long workSetId;

    private Long setNo;

    private Long count;

    private Long weight;

    @Override
    public SubSetResponseDto fromEntity(SubSet subSet){
        return SubSetResponseDto.builder()
                .subSetId(subSet.getId())
                .workSetId(subSet.getWorkSet().getId())
                .setNo(subSet.getSetNo())
                .count(subSet.getCount())
                .weight(subSet.getWeight())
                .build();
    }

}
