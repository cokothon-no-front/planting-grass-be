package com.nofront.plantinggrassbe.repository;

import com.nofront.plantinggrassbe.domain.UserSave;
import com.nofront.plantinggrassbe.domain.UserSaveData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSaveRepository extends JpaRepository<UserSave, Integer> {

    UserSave save(UserSave data);


    List<UserSave> findAll();

    List<UserSave> findByUsername(String username);

}
