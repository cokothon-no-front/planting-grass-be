package com.nofront.plantinggrassbe.service;

import com.nofront.plantinggrassbe.DTO.*;
import com.nofront.plantinggrassbe.Utils.CommonTokenUtils;
import com.nofront.plantinggrassbe.Utils.KakaoTokenUtils;
import com.nofront.plantinggrassbe.domain.*;
import com.nofront.plantinggrassbe.repository.*;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Transactional
@Service
public class SubRoutineService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubRoutineRepository subRoutineRepository;
    @Autowired
    private RoutineRepository routineRepository;
    @Autowired
    private CommonTokenUtils tokenUtils;
    @Autowired
    private KakaoTokenUtils kakaoTokenUtils;
    @Autowired
    private WorkSetRepository workSetRepository;
    @Autowired
    private SubSetRepository subSetRepository;
    public List<SubRoutineResponseDto> findSubRoutinesById(Long routineId) {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = userDetails.getUsername();
//        String provider = userDetails.getProvider();

        List<SubRoutine> subRoutines = subRoutineRepository.findByRoutine_Id(routineId);

        List<SubRoutineResponseDto> responseSubRoutine = new ArrayList<>();
        Stream<SubRoutine> stream = subRoutines.stream();
        stream.forEach(subRoutine -> {
            responseSubRoutine.add(new SubRoutineResponseDto().fromEntity(subRoutine));
        });
        return responseSubRoutine;
    }

    public List<SubRoutineResponseDto> findSubRoutines() throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        String provider = userDetails.getProvider();

        List<SubRoutine> subRoutines = subRoutineRepository.findByRoutine_User_UsernameAndRoutine_User_Provider(username, provider);

        List<SubRoutineResponseDto> responseSubRoutine = new ArrayList<>();
        Stream<SubRoutine> stream = subRoutines.stream();
        stream.forEach(subRoutine -> {
            responseSubRoutine.add(new SubRoutineResponseDto().fromEntity(subRoutine));
        });
        return responseSubRoutine;
    }

    public long join(SubRoutineRegisterRequestDto request) throws ParseException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsernameAndProvider(userDetails.getUsername(), userDetails.getProvider()).orElseThrow(() -> new RuntimeException("can not find user!"));
        Routine routine = routineRepository.findByIdAndUser(request.getRoutineId(), user).orElseThrow(() -> new UsernameNotFoundException("cannot find such user that own specific routine"));
        SubRoutine subRoutine = request.toEntity(routine);
        subRoutineRepository.save(subRoutine);
        return subRoutine.getId();
    }

    public List<SubRoutineResponseAllDto> findAllWorkSetById(long subRoutineId) {
        List<WorkSet> workSetList = workSetRepository.findBySubRoutine_Id(subRoutineId);

        List<SubRoutineResponseAllDto> responseWorkSets = new ArrayList<>();
        Stream<WorkSet> stream = workSetList.stream();
        stream.forEach(workSet -> {
            List<SubSet> subSetList = subSetRepository.findByWorkSet_Id(workSet.getId());
            List<SubSetResponseDto> responseSubSetList = new ArrayList<>();
            Stream<SubSet> tmpStream = subSetList.stream();
            tmpStream.forEach(subSet ->{
                responseSubSetList.add(new SubSetResponseDto().fromEntity(subSet));
            });

            responseWorkSets.add(new SubRoutineResponseAllDto().fromEntity(workSet.getWorkOut().getName(),responseSubSetList));
        });
        return responseWorkSets;
    }
}
