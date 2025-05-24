package com.RaceForLife.RaceForLife.repository;
import com.RaceForLife.RaceForLife.model.Race;
import com.RaceForLife.RaceForLife.model.Run;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.model.UserRace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRaceRepository extends JpaRepository<UserRace, Long> {
    boolean existsByUserAndRace(User user, Race race);
    Optional<UserRace> findByUserAndRace(User user, Race race);
    List<UserRace> findByUser(User user);
    List<UserRace> findByRace(Race race);
    void deleteByUserAndRace(User user, Race race);
}
