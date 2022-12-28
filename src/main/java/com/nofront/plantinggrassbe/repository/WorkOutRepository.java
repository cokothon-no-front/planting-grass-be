package com.nofront.plantinggrassbe.repository;

import com.nofront.plantinggrassbe.domain.WorkOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkOutRepository extends JpaRepository<WorkOut, Long> {
    WorkOut save(WorkOut workOut);
    List<WorkOut> findAll();
    Optional<WorkOut> findByName(String name);
    List<WorkOut> findByMachineName(String machineName);
    List<WorkOut> findByBodyPart(String bodyPart);
}
