package com.RaceForLife.RaceForLife.repository;

import com.RaceForLife.RaceForLife.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunRepository extends JpaRepository<Run, Long> {
}
