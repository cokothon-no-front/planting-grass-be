package com.nofront.plantinggrassbe.controller;

import com.nofront.plantinggrassbe.DTO.*;
import com.nofront.plantinggrassbe.service.RoutineService;
import com.nofront.plantinggrassbe.service.SubRoutineService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoutineController {
    private final RoutineService routineService;

    private final SubRoutineService subRoutineService;

    @Autowired
    public RoutineController(RoutineService routineService, SubRoutineService subRoutineService) {
        this.routineService = routineService;
        this.subRoutineService = subRoutineService;
    }

    @PostMapping("/routine")
    public IdResponseDto postRoutine(
            @RequestBody RoutineRegisterRequestDto requestBody
    ) throws Exception {
        System.out.println("requestBody = " + requestBody);
        if (!routineService.isRoutineNameExist(requestBody.getName(), requestBody.getCategory())){
            long id = routineService.join(requestBody);
            return new IdResponseDto(id);
        }else{
            return new IdResponseDto(-1);
        }
    }

    @GetMapping("/routines")
    public List<RoutineResponseDto> getRoutine(){
        try{
            return routineService.findRoutines();
        }catch (Exception e){
            System.out.println("/routines:e = " + e);
            return null;
        }
    }

    @PostMapping("/subRoutine")
    public IdResponseDto postSubRoutine(
            @RequestBody SubRoutineRegisterRequestDto requestBody
    ) throws ParseException {
        System.out.println("requestBody = " + requestBody);
        long id = subRoutineService.join(requestBody);
        return new IdResponseDto(id);
    }

    @GetMapping("/subRoutines")
    public List<SubRoutineResponseDto> getSubRoutine(){
        try{
            return subRoutineService.findSubRoutines();
        }catch (Exception e){
            System.out.println("/subRoutines:e = " + e);
            return null;
        }
    }
    @GetMapping("/subRoutine")
    public List<SubRoutineResponseDto> findSubroutinesById(@RequestParam(value = "routineId") Long routineId){
        try {
            return subRoutineService.findSubRoutinesById(routineId);
        }
        catch (Exception e){
            System.out.println("e = " + e);
            return null;
        }
    }
    @GetMapping("/routines/recommended")
    public List<RoutineResponseDto> getRecommendRoutines(){
        try{
            return routineService.findRecommendedRoutines();
        }catch (Exception e){
            System.out.println("/routines/recommended:e = " + e);
            return null;
        }
    }
    @GetMapping("/routines/description")
    public List<RoutineResponseDto> getRoutinesByDescription(@RequestParam(value = "description") String description){
        try{
            return routineService.findRoutinesByDescription(description);
        }catch (Exception e){
            System.out.println("/routines/description:e = " + e);
            return null;
        }
    }
    @GetMapping("/routines/category")
    public List<RoutineResponseDto> getRoutinesByCategory(@RequestParam(value = "category") String category){
        try{
            return routineService.findRoutinesByCategory(category);
        }catch (Exception e){
            System.out.println("/routines/description:e = " + e);
            return null;
        }
    }

    @GetMapping("/subRoutines/all")
    public List<SubRoutineResponseAllDto> getAllWorkSetBySubRoutineId(@RequestParam(value = "subRoutineId")long subRoutineId){
        try {
            return subRoutineService.findAllWorkSetById(subRoutineId);
        }
        catch (Exception e){
            System.out.println("e = " + e);
            return null;
        }
    }
}
