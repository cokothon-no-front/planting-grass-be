package com.nofront.plantinggrassbe.repository;

import com.nofront.plantinggrassbe.domain.Routine;
import com.nofront.plantinggrassbe.domain.SubRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubRoutineRepository extends JpaRepository<SubRoutine, Long> {
    SubRoutine save(SubRoutine subRoutine);
    Optional<SubRoutine> findById(Long id);
    Optional<SubRoutine> findByName(String name);
    List<SubRoutine> findAll();
    List<SubRoutine> findByRoutine(Routine routine);
    Optional<SubRoutine> findByRoutineId(Long userId);
    List<SubRoutine> findByRoutine_User_UsernameAndRoutine_User_Provider(String username, String provider);

    List<SubRoutine> findByRoutine_IdAndRoutine_User_UsernameAndRoutine_User_Provider(Long id, String username, String provider);

    List<SubRoutine> findByRoutine_Id(Long routineId);
}
