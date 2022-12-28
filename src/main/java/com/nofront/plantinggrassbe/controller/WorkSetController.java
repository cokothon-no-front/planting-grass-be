package com.nofront.plantinggrassbe.controller;

import com.nofront.plantinggrassbe.DTO.IdResponseDto;
import com.nofront.plantinggrassbe.DTO.WorkSetRegisterRequestDto;
import com.nofront.plantinggrassbe.DTO.WorkSetResponseDto;
import com.nofront.plantinggrassbe.service.WorkSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WorkSetController {
    @Autowired
    private WorkSetService workSetService;

    @PostMapping("/workSet")
    public IdResponseDto registerWorkSet(
            @RequestBody WorkSetRegisterRequestDto requestBody
    ) throws Exception {
        long id = workSetService.join(requestBody);
        return new IdResponseDto(id);
    }

    @GetMapping("/workSets")
    public List<WorkSetResponseDto> findWorkSetById(@RequestParam(value = "subRoutineId") Long subRoutineId){
        try {
            return workSetService.findWorkSetById(subRoutineId);
        }
        catch (Exception e){
            System.out.println("e = " + e);
            return null;
        }
    }

}
