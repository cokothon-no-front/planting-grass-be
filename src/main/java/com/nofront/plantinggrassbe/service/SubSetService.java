package com.nofront.plantinggrassbe.service;

import com.nofront.plantinggrassbe.DTO.SubSetRegisterRequestDto;
import com.nofront.plantinggrassbe.DTO.SubSetResponseDto;
import com.nofront.plantinggrassbe.domain.SubSet;
import com.nofront.plantinggrassbe.domain.WorkSet;
import com.nofront.plantinggrassbe.repository.SubSetRepository;
import com.nofront.plantinggrassbe.repository.WorkSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Transactional
@Service
public class SubSetService {

    @Autowired
    private SubSetRepository subSetRepository;

    @Autowired
    private WorkSetRepository workSetRepository;

    public long join(SubSetRegisterRequestDto request) throws Exception {
        WorkSet workSet = workSetRepository.findById(request.getWorkSetId()).orElseThrow(() -> new Exception("no such workset id"));
        SubSet subSet = request.toEntity(workSet);
        subSetRepository.save(subSet);
        return subSet.getId();
    }

    public List<SubSetResponseDto> findSubSetById(Long workSetId) throws Exception{
        List<SubSet> subSets = subSetRepository.findByWorkSet_Id(workSetId);

        List<SubSetResponseDto> responseSubSet = new ArrayList<>();
        Stream<SubSet> stream = subSets.stream();
        stream.forEach(subSet -> {
            responseSubSet.add(new SubSetResponseDto().fromEntity(subSet));
        });
        return responseSubSet;
    }
}
