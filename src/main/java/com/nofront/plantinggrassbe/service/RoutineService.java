package com.nofront.plantinggrassbe.service;

import com.nofront.plantinggrassbe.DTO.RoutineRegisterRequestDto;
import com.nofront.plantinggrassbe.DTO.RoutineResponseDto;
import com.nofront.plantinggrassbe.DTO.UserDetails;
import com.nofront.plantinggrassbe.Utils.CommonTokenUtils;
import com.nofront.plantinggrassbe.Utils.KakaoTokenUtils;
import com.nofront.plantinggrassbe.domain.Routine;
import com.nofront.plantinggrassbe.domain.User;
import com.nofront.plantinggrassbe.repository.RoutineRepository;
import com.nofront.plantinggrassbe.repository.UserRepository;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Transactional
@Service
public class RoutineService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoutineRepository routineRepository;
    @Autowired
    private CommonTokenUtils tokenUtils;
    @Autowired
    private KakaoTokenUtils kakaoTokenUtils;

    public List<RoutineResponseDto> findRoutines() throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        String provider = userDetails.getProvider();
        List<Routine> routines = routineRepository.findByUser_UsernameAndUser_Provider(username, provider);

        List<RoutineResponseDto> responseRoutine = new ArrayList<>();
        Stream<Routine> stream = routines.stream();
        stream.forEach(routine -> {
            responseRoutine.add(new RoutineResponseDto().fromEntity(routine));
        });
        return responseRoutine;
    }



    public boolean isRoutineNameExist(String routinename, String category) throws Exception{
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        List<Routine> routines = routineRepository.findByUser_UsernameAndNameAndCategory(username, routinename, category);
        return !routines.isEmpty();
    }

    public long join(RoutineRegisterRequestDto request) throws ParseException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsernameAndProvider(userDetails.getUsername(), userDetails.getProvider()).orElseThrow(() -> new RuntimeException("can not find user!"));
        Routine routine = request.toEntity(user);
        routineRepository.save(routine);
        return routine.getId();
    }

    public List<RoutineResponseDto> findRecommendedRoutines() {
        List<Routine> routines = routineRepository.findByIsRecommended(true);
        List<RoutineResponseDto> responseRoutine = new ArrayList<>();
        Stream<Routine> stream = routines.stream();
        stream.forEach(routine -> {
            responseRoutine.add(new RoutineResponseDto().fromEntity(routine));
        });
        return responseRoutine;
    }

    public List<RoutineResponseDto> findRoutinesByDescription(String description) {
        List<Routine> routines = routineRepository.findByDescription(description);
        List<RoutineResponseDto> responseRoutine = new ArrayList<>();
        Stream<Routine> stream = routines.stream();
        stream.forEach(routine -> {
            responseRoutine.add(new RoutineResponseDto().fromEntity(routine));
        });
        return responseRoutine;
    }

    public List<RoutineResponseDto> findRoutinesByCategory(String category) {
        List<Routine> routines = routineRepository.findByCategory(category);
        List<RoutineResponseDto> responseRoutine = new ArrayList<>();
        Stream<Routine> stream = routines.stream();
        stream.forEach(routine -> {
            responseRoutine.add(new RoutineResponseDto().fromEntity(routine));
        });
        return responseRoutine;
    }

}

