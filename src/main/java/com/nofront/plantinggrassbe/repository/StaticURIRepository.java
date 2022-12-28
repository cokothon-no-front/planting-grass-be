package com.nofront.plantinggrassbe.repository;

import com.nofront.plantinggrassbe.domain.StaticURI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaticURIRepository extends JpaRepository<StaticURI, Long> {
    StaticURI save(StaticURI staticURI);

    Optional<StaticURI> findByName(String name);
}
