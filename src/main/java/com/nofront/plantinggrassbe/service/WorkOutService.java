package com.nofront.plantinggrassbe.service;

import com.nofront.plantinggrassbe.DTO.WorkOutRegisterRequestDto;
import com.nofront.plantinggrassbe.domain.WorkOut;
import com.nofront.plantinggrassbe.repository.WorkOutRepository;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class WorkOutService {

    @Autowired
    private WorkOutRepository workOutRepository;

    public List<WorkOut> findWorkOuts() throws  Exception{
        if (workOutRepository.findAll().isEmpty()){
            throw new Exception("No Workouts");
        }
        return workOutRepository.findAll();
    }
    public List<WorkOut> findWorkOutsByBodyPart(String bodyPart) throws  Exception{
        if (workOutRepository.findAll().isEmpty()){
            throw new Exception("No Workouts by bodypart");
        }
        return workOutRepository.findByBodyPart(bodyPart);
    }

    public List<WorkOut> findWorkOutsByMachine(String machine) throws  Exception{
        if (workOutRepository.findAll().isEmpty()){
            throw new Exception("No Workouts by machinename");
        }
        return workOutRepository.findByMachineName(machine);
    }
    public long join(WorkOutRegisterRequestDto request) throws ParseException{
        WorkOut workOut = request.toEntity();
        workOutRepository.save(workOut);
        return workOut.getId();
    }
}
