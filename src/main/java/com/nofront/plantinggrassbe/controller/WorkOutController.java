package com.nofront.plantinggrassbe.controller;

import com.nofront.plantinggrassbe.DTO.IdResponseDto;
import com.nofront.plantinggrassbe.DTO.WorkOutRegisterRequestDto;
import com.nofront.plantinggrassbe.domain.WorkOut;
import com.nofront.plantinggrassbe.service.WorkOutService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WorkOutController {

    @Autowired
    private WorkOutService workOutService;

    @GetMapping("/workouts")
    public List<WorkOut> workOutList(){
        try {
            return workOutService.findWorkOuts();
        }catch (Exception e){
            return null;
        }
    }

    @PostMapping("/workout")
    public IdResponseDto registerWorkOut(
            @RequestBody WorkOutRegisterRequestDto requestBody
            ) throws ParseException {
        long id = workOutService.join(requestBody);
        return new IdResponseDto(id);
    }

    @GetMapping("/workout/bodypart")
    public List<WorkOut> findWorkOutByBodyPart(@RequestParam(value = "bodyPart") String bodyPart){
        try {
            return workOutService.findWorkOutsByBodyPart(bodyPart);
        }
        catch (Exception e){
            System.out.println("e = " + e);
            return null;
        }
    }

    @GetMapping("/workout/machine")
    public List<WorkOut> findWorkOutByMachine(@RequestParam(value = "machine") String machine){
        try {
            return workOutService.findWorkOutsByMachine(machine);
        }
        catch (Exception e){
            System.out.println("e = " + e);
            return null;
        }
    }
}
