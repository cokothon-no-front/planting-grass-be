package com.nofront.plantinggrassbe.service;

import com.nofront.plantinggrassbe.domain.SubRoutine;
import com.nofront.plantinggrassbe.domain.WorkOut;
import com.nofront.plantinggrassbe.repository.SubRoutineRepository;
import com.nofront.plantinggrassbe.repository.WorkOutRepository;
import com.nofront.plantinggrassbe.repository.WorkSetRepository;
import com.nofront.plantinggrassbe.DTO.WorkSetRegisterRequestDto;
import com.nofront.plantinggrassbe.DTO.WorkSetResponseDto;
import com.nofront.plantinggrassbe.domain.WorkSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Transactional
@Service
public class WorkSetService {

    @Autowired
    private WorkSetRepository workSetRepository;
    @Autowired
    private SubRoutineRepository subRoutineRepository;

    @Autowired
    private WorkOutRepository workOutRepository;

    public long join(WorkSetRegisterRequestDto request) throws Exception {
        SubRoutine subRoutine = subRoutineRepository.findById(request.getSubRoutineId()).orElseThrow(() -> new Exception("no such subroutine id"));
        WorkOut workOut = workOutRepository.findByName(request.getWorkOutName()).orElseThrow(() -> new Exception("no such workout name"));
        WorkSet workSet = request.toEntity(subRoutine, workOut);
        workSetRepository.save(workSet);
        return workSet.getId();
    }

    public List<WorkSetResponseDto> findWorkSetById(Long subRoutineId) throws Exception{
        List<WorkSet> workSets = workSetRepository.findBySubRoutine_Id(subRoutineId);

        List<WorkSetResponseDto> responseWorkSet = new ArrayList<>();
        Stream<WorkSet> stream = workSets.stream();
        stream.forEach(workSet -> {
            responseWorkSet.add(new WorkSetResponseDto().fromEntity(workSet));
        });
        return responseWorkSet;
    }
}
