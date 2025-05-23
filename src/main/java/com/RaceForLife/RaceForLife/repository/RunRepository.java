package com.RaceForLife.RaceForLife.repository;

import com.RaceForLife.RaceForLife.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RunRepository extends JpaRepository<Run, Long> {
    List<Run> findByDateAfterOrderByDateAsc(LocalDateTime dateTime);

    List<Run> findByDateBeforeOrderByDateDesc(LocalDateTime dateTime);
}
