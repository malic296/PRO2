package com.RaceForLife.RaceForLife.repository;

import com.RaceForLife.RaceForLife.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RunRepository extends JpaRepository<Run, Long> {
    List<Run> findByDateAfterOrderByDateAsc(LocalDateTime dateTime);

    List<Run> findByDateBeforeOrderByDateDesc(LocalDateTime dateTime);

    @Query(value = """
        SELECT COUNT(*) FROM user_run ur
        JOIN run r ON ur.run_id = r.id
        JOIN users u ON ur.user_id = u.id
        WHERE u.email = :email AND r.date < :now
    """, nativeQuery = true)
    int countCompletedByUserEmail(@Param("email") String email, @Param("now") LocalDateTime now);

    @Query(value = """
        SELECT COUNT(*) FROM user_run ur
        JOIN run r ON ur.run_id = r.id
        JOIN users u ON ur.user_id = u.id
        WHERE u.email = :email AND r.date >= :now
    """, nativeQuery = true)
    int countUpcomingByUserEmail(@Param("email") String email, @Param("now") LocalDateTime now);
}
