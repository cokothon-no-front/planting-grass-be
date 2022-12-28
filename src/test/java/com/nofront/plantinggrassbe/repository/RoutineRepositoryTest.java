package com.nofront.plantinggrassbe.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class RoutineRepositoryTest {

    @Autowired
    private RoutineRepository repo;

    @Transactional
    @Test
    void findByUser_Username() {
        Assertions.assertThat(repo.findByUser_UsernameAndUser_Provider("2362092730", "kakao"));
    }
}