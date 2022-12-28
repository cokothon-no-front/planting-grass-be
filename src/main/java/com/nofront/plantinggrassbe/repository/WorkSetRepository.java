package com.nofront.plantinggrassbe.repository;

import com.nofront.plantinggrassbe.domain.WorkSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkSetRepository extends JpaRepository<WorkSet, Long> {
    WorkSet save(WorkSet workSet);
    List<WorkSet> findAll();
    List<WorkSet> findBySubRoutine_Id(Long id);

}
