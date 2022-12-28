package com.nofront.plantinggrassbe.repository;

import com.nofront.plantinggrassbe.domain.SubSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubSetRepository extends JpaRepository<SubSet, Long> {
    SubSet save(SubSet subSet);
    List<SubSet> findAll();
    List<SubSet> findByWorkSet_Id(Long id);
}
